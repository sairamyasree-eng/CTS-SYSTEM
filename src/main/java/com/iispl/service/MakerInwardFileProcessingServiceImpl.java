package com.iispl.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import com.iispl.dao.BatchDao;
import com.iispl.dao.BpxfFileDao;
import com.iispl.dao.InwardChequeDao;
import com.iispl.entity.Batch;
import com.iispl.entity.BpxfFile;
import com.iispl.entity.InwardCheque;
import com.iispl.enums.InwardStatus;

public class MakerInwardFileProcessingServiceImpl implements MakerInwardFileProcessingService {

	private static final Logger LOG = Logger.getLogger(MakerInwardFileProcessingServiceImpl.class.getName());

	private final String watchFolderPath;

	private final BpxfFileDao bpxfFileDao = new BpxfFileDao();

	private final InwardChequeDao inwardChequeDao = new InwardChequeDao();

	private final BatchDao batchDao = new BatchDao();

	private Thread watchThread;

	public MakerInwardFileProcessingServiceImpl(String watchFolderPath) {
		this.watchFolderPath = watchFolderPath;
	}

	@Override
	public List<BpxfFile> scanWatchFolder() {
		List<BpxfFile> result = new ArrayList<>();
		Path folder = Paths.get(watchFolderPath);

		if (!Files.exists(folder)) {
			LOG.warning("Watch folder does not exists: " + watchFolderPath);
			return result;
		}

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.xml")) {
			for (Path filePath : stream) {
				if (Files.isDirectory(filePath))
					continue;
				BpxfFile file = peekBpxfFile(filePath);
				if (file != null)
					result.add(file);
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "scanWatchFolder failed", e);
		}

		return result;
	}

	@Override
	public void startWatchService(Runnable onNewFileDetected) {
		Path folder = Paths.get(watchFolderPath);

		watchThread = new Thread(() -> {
			try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

				folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
				LOG.info("WatchService started on : " + watchFolderPath);

				while (!Thread.currentThread().isInterrupted()) {
					WatchKey key = watchService.take();

					for (WatchEvent<?> event : key.pollEvents()) {
						if (event.kind() == StandardWatchEventKinds.OVERFLOW)
							continue;

						Path fileName = (Path) event.context();
						if (!fileName.toString().toLowerCase().endsWith(".xml"))
							continue;

						Path fullPath = folder.resolve(fileName);

						Thread.sleep(500);

						BpxfFile newFile = peekBpxfFile(fullPath);
						if (newFile == null)
							continue;

						if (bpxfFileDao.existsByBatchNo(newFile.getBatchNo())) {
							LOG.info("Duplicate BPXF ignored: " + fileName);
							continue;
						}

						bpxfFileDao.insert(newFile);
						LOG.info("New BPXF File detected and persisted: " + fileName);

						onNewFileDetected.run();
					}

					if (!key.reset())
						break;
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				LOG.info("WatchService thread interrupted - stopping.");
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "WatchService error", e);
			}
		}, "bpxf-watch-thread");

		watchThread.setDaemon(true);
		watchThread.start();
	}

	@Override
	public void stopWatchService() {
		if (watchThread != null && watchThread.isAlive()) {
			watchThread.interrupt();
		}
	}

	@Override
	public List<InwardCheque> parseAndPersist(List<BpxfFile> selectedFiles) {
		List<InwardCheque> allPersisted = new ArrayList<>();

		for (BpxfFile bpxfFile : selectedFiles) {
			try {
				List<InwardCheque> cheques = parseXml(bpxfFile);
				inwardChequeDao.insertAll(cheques);

				bpxfFileDao.updateStatusParsed(bpxfFile.getId());

				allPersisted.addAll(cheques);
				LOG.info("Parsed and persisted " + cheques.size() + " cheques for batch: " + bpxfFile.getBatchNo());

			} catch (Exception e) {
				LOG.log(Level.SEVERE, "parseAndPersist failed for: " + bpxfFile.getFileName(), e);
				bpxfFileDao.updateStatusFailed(bpxfFile.getId());
			}
		}

		return allPersisted;
	}

	private BpxfFile peekBpxfFile(Path filePath) {
		XMLInputFactory factory = XMLInputFactory.newInstance();

		factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
		factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

		String batchNo = "";
		String branch = "";
		int chequeCount = 0;
		BigDecimal total = BigDecimal.ZERO;
		String currentTag = "";

		try (InputStream is = Files.newInputStream(filePath)) {
			XMLStreamReader reader = factory.createXMLStreamReader(is);

			while (reader.hasNext()) {
				int event = reader.next();

				if (event == XMLStreamConstants.START_ELEMENT) {
					String localName = reader.getLocalName();

					if ("BPXF".equals(localName)) {
						batchNo = reader.getAttributeValue(null, "batchNo");
						branch = reader.getAttributeValue(null, "branch");
					} else if ("cheque".equals(localName)) {
						chequeCount++;
					}
					currentTag = localName;
				} else if (event == XMLStreamConstants.CHARACTERS) {
					if ("amount".equals(currentTag)) {
						String text = reader.getText().trim();
						if (!text.isEmpty()) {
							try {
								total = total.add(new BigDecimal(text));
							} catch (NumberFormatException ignored) {
							}
						}
					}
				}
			}
			reader.close();
		} catch (Exception e) {
			LOG.log(Level.WARNING, "peekBpxfFile failed for: " + filePath.getFileName(), e);
			return null;
		}

		return new BpxfFile(filePath.getFileName().toString(), filePath.toString(), batchNo, branch, chequeCount, total,
				"PENDING", LocalDateTime.now());
	}

	private List<InwardCheque> parseXml(BpxfFile bpxfFile) throws Exception {
		List<InwardCheque> cheques = new ArrayList<>();

		// UPDATED — proceeds even if batch not found, sets null
		Batch batch = batchDao.findById(bpxfFile.getBatchNo());
		if (batch == null) {
		    LOG.warning("Batch not found for batchNo: " + bpxfFile.getBatchNo() + " — setting null");
		}

		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
		factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

		Path filePath = Paths.get(bpxfFile.getFilePath());

		try (InputStream is = Files.newInputStream(filePath)) {
			XMLStreamReader reader = factory.createXMLStreamReader(is);

			InwardCheque current = null;
			String currentTag = "";

			while (reader.hasNext()) {
				int event = reader.next();

				if (event == XMLStreamConstants.START_ELEMENT) {
					currentTag = reader.getLocalName();
					if ("cheque".equals(currentTag)) {
						current = new InwardCheque();
						current.setBatch(batch);
						current.setInwardStatus(InwardStatus.Parsed);
					}
				} else if (event == XMLStreamConstants.CHARACTERS && current != null) {
					String text = reader.getText().trim();
					if (text.isEmpty())
						continue;

					switch (currentTag) {
					case "chequeNumber":
						current.setChequeNumber(text);
						break;

					case "micrCode":
						current.setMicrCode(text);
						break;

					case "originalMicrCode":
						current.setOriginalMicrCode(text);
						break;

					case "amount":
						try {
							current.setAmount(new BigDecimal(text));
						} catch (NumberFormatException e) {
							current.setAmount(BigDecimal.ZERO);
						}
						break;

					case "drawerBank":
						current.setDrawerBank(text);
						break;

					case "payeeName":
						current.setPayeeName(text);
						break;
						
					case "accountNumber":
						current.setAccountNumber(text);
						break;

					case "ifscCode":
						current.setIfscCode(text);
						break;

					case "chequeDate":
						try {
							current.setChequeDate(LocalDate.parse(text));
						} catch (Exception e) {
						}
						break;

					case "destBranch":
						current.setDestBranch(text);
						break;
					}
				} else if (event == XMLStreamConstants.END_ELEMENT) {
					if ("cheque".equals(reader.getLocalName()) && current != null) {
						cheques.add(current);
						current = null;
					}
					currentTag = "";
				}
			}
			reader.close();
		}
		return cheques;
	}
	
	@Override
	public BpxfFile peekAndBuild(Path filePath) {
	    return peekBpxfFile(filePath);
	}

}
