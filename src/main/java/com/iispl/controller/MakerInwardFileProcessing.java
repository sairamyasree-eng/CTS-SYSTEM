package com.iispl.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.iispl.dao.BpxfFileDao;
import com.iispl.entity.BpxfFile;
import com.iispl.entity.InwardCheque;
import com.iispl.service.MakerInwardFileProcessingService;
import com.iispl.service.MakerInwardFileProcessingServiceImpl;

public class MakerInwardFileProcessing extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	private static final String BPXF_WATCH_FOLDER = "/home/administrator/Documents/IISPLTraining/FinalProject";

	// ── wired components ─────────────────────────────────────────────────────
	@Wire("#chkBpxfSelectAll")
	private Checkbox chkBpxfSelectAll;
	
	@Wire("#lblBpxfSelCount")
	private Label lblBpxfSelCount;
	
	@Wire("#btnParseBpxf")
	private Button btnParseBpxf;
	
	@Wire("#txtBpxfSearch")
	private Textbox txtBpxfSearch;
	
	@Wire("#lblBpxfCount")
	private Label lblBpxfCount;
	
	@Wire("#tblBpxf")
	private Listbox tblBpxf;
	
	@Wire("#divBpxfHint")
	private Div divBpxfHint;
	
	//@Wire("#dlgParsing")
	private Window dlgParsing;
	
	//@Wire("#lblParsingTitle")
	private Label lblParsingTitle;
	
	//@Wire("#lblParsingSub")
	private Label lblParsingSub;
	
	@Wire("#btnUploadBpxf")
	private Button btnUploadBpxf;

	// ── service & dao ─────────────────────────────────────────────────────────
	private final MakerInwardFileProcessingService service = new MakerInwardFileProcessingServiceImpl(
			BPXF_WATCH_FOLDER);

	private final BpxfFileDao bpxfFileDao = new BpxfFileDao();

	// ── state ─────────────────────────────────────────────────────────────────
	private List<BpxfFile> allRows = new ArrayList<>();

	// ═════════════════════════════════════════════════════════════════════════
	// lifecycle
	// ═════════════════════════════════════════════════════════════════════════

	@Override
	public void doAfterCompose(Component comp) throws Exception {
	    super.doAfterCompose(comp);

	    // manual wire for Window modal components
	    // @Wire fails for components inside <window> because
	    // ZK treats Window as a separate ID space
	    dlgParsing      = (Window) comp.getFellow("dlgParsing");
	    lblParsingTitle = (Label)  dlgParsing.getFellow("lblParsingTitle");
	    lblParsingSub   = (Label)  dlgParsing.getFellow("lblParsingSub");

	    loadTable();

	    Desktop desktop = comp.getDesktop();
	    desktop.enableServerPush(true);

	    service.startWatchService(() -> {
	        Executions.schedule(desktop, e -> {
	            loadTable();
	            Clients.showNotification(
	                "New BPXF file received — table refreshed.",
	                "info", null, "top_right", 4000);
	        }, new Event("onBpxfDetected", null, null));
	    });

	    comp.getPage().addEventListener("onDestroy", e -> service.stopWatchService());
	}

	// ═════════════════════════════════════════════════════════════════════════
	// table
	// ═════════════════════════════════════════════════════════════════════════

	private void loadTable() {
		allRows = service.scanWatchFolder();
		renderRows(allRows);
	}

	private void renderRows(List<BpxfFile> rows) {
		tblBpxf.getItems().clear();

		for (BpxfFile file : rows) {
			Listitem item = new Listitem();
			item.setValue(file);

			// col 0 — checkbox (disabled for already PARSED or FAILED rows)
			Listcell cellChk = new Listcell();
			Checkbox chk = new Checkbox();
			chk.setSclass("dem-row-chk");
			chk.setDisabled(!"PENDING".equals(file.getStatus()));
			chk.addEventListener("onCheck", ev -> refreshSelectionState());
			cellChk.appendChild(chk);
			item.appendChild(cellChk);

			// col 1 — filename
			item.appendChild(new Listcell(file.getFileName()));

			// col 2 — batch no.
			item.appendChild(new Listcell(file.getBatchNo()));

			// col 3 — branch
			item.appendChild(new Listcell(file.getBranch()));

			// col 4 — cheque count
			item.appendChild(new Listcell(String.valueOf(file.getChequeCount())));

			// col 5 — total amount
			item.appendChild(new Listcell(formatAmount(file.getTotalAmount())));

			// col 6 — status badge
			Listcell cellStatus = new Listcell();
			Label badge = new Label(file.getStatus());
			badge.setSclass(badgeSclass(file.getStatus()));
			cellStatus.appendChild(badge);
			item.appendChild(cellStatus);

			// col 7 — parsed at
			String parsedAt = file.getParsedAt() != null ? file.getParsedAt().toString() : "—";
			item.appendChild(new Listcell(parsedAt));

			tblBpxf.appendChild(item);
		}

		lblBpxfCount.setValue(rows.size() + "/" + rows.size());
		resetSelection();
	}

	// ═════════════════════════════════════════════════════════════════════════
	// event handlers
	// ═════════════════════════════════════════════════════════════════════════

	@Listen("onCheck = #chkBpxfSelectAll")
	public void onSelectAll(CheckEvent e) {
		boolean checked = e.isChecked();
		for (Listitem item : tblBpxf.getItems()) {
			Checkbox chk = getRowCheckbox(item);
			if (chk != null && !chk.isDisabled())
				chk.setChecked(checked);
		}
		refreshSelectionState();
	}

	@Listen("onChanging = #txtBpxfSearch; onChange = #txtBpxfSearch")
	public void onSearch() {
		String term = txtBpxfSearch.getValue().trim().toLowerCase();
		if (term.isEmpty()) {
			renderRows(allRows);
			return;
		}
		List<BpxfFile> filtered = new ArrayList<>();
		for (BpxfFile f : allRows) {
			if (f.getFileName().toLowerCase().contains(term) || f.getBatchNo().toLowerCase().contains(term)
					|| f.getBranch().toLowerCase().contains(term)) {
				filtered.add(f);
			}
		}
		renderRows(filtered);
	}

	@Listen("onClick = #btnParseBpxf")
	public void onParseBpxf() {
		List<BpxfFile> selected = getSelectedFiles();
		if (selected.isEmpty())
			return;

		lblParsingTitle.setValue("Parsing BPXF Files");
		lblParsingSub.setValue("Reading XML and persisting inward cheques...");
		dlgParsing.setVisible(true);
		dlgParsing.doModal();

		Desktop desktop = getSelf().getDesktop();

		new Thread(() -> {
			String message;
			try {
				List<InwardCheque> persisted = service.parseAndPersist(selected);
				message = "✓ Parsed " + persisted.size() + " cheques successfully.";
			} catch (Exception ex) {
				message = "✗ Parse failed: " + ex.getMessage();
			}

			final String finalMsg = message;
			Executions.schedule(desktop, e -> {
				dlgParsing.setVisible(false);
				loadTable();
				Clients.showNotification(finalMsg, finalMsg.startsWith("✓") ? "info" : "error", null, "top_right",
						4000);
			}, new Event("onParseComplete", null, null));

		}, "bpxf-parse-thread").start();
	}

	/**
	 * Upload handler — user picks a .xml file from the OS file picker.
	 *
	 * Only saves the file to BPXF_WATCH_FOLDER via NIO. Does NOT call
	 * bpxfFileDao.insert() or loadTable() directly. WatchService detects the new
	 * file → persists BpxfFile → refreshes table. This keeps one consistent flow
	 * for both NPCI drops and manual uploads.
	 */
	@Listen("onUpload = #btnUploadBpxf")
	public void onUploadBpxf(org.zkoss.zk.ui.event.UploadEvent e) {
		org.zkoss.util.media.Media media = e.getMedia();

		if (media == null) {
			Clients.showNotification("No file selected.", "warning", null, "top_right", 3000);
			return;
		}

		if (!media.getName().toLowerCase().endsWith(".xml")) {
			Clients.showNotification("Only .xml files are supported.", "warning", null, "top_right", 3000);
			return;
		}

		// duplicate check before saving
		BpxfFile existing = bpxfFileDao.findByFileName(media.getName());
		if (existing != null) {
			Clients.showNotification("File already loaded: " + media.getName(), "warning", null, "top_right", 3000);
			return;
		}

		// save to watch folder — WatchService takes it from here
		Path targetPath = Paths.get(BPXF_WATCH_FOLDER, media.getName());
		try {
			Files.writeString(targetPath, media.getStringData(), java.nio.charset.StandardCharsets.UTF_8);

			Clients.showNotification("File saved — loading shortly...", "info", null, "top_right", 3000);

		} catch (Exception ex) {
			ex.printStackTrace();
			Clients.showNotification("Failed to save file: " + ex.getMessage(), "error", null, "top_right", 3000);
		}
	}

	// ═════════════════════════════════════════════════════════════════════════
	// helpers
	// ═════════════════════════════════════════════════════════════════════════

	private void refreshSelectionState() {
		int count = countChecked();
		lblBpxfSelCount.setValue(count + " selected");
		btnParseBpxf.setLabel("Parse Selected BPXF (" + count + ")");
		btnParseBpxf.setDisabled(count == 0);
		divBpxfHint.setVisible(count == 0);
		chkBpxfSelectAll.setChecked(tblBpxf.getItemCount() > 0 && count == countPending());
	}

	private void resetSelection() {
		chkBpxfSelectAll.setChecked(false);
		refreshSelectionState();
	}

	private int countChecked() {
		int n = 0;
		for (Listitem item : tblBpxf.getItems()) {
			Checkbox chk = getRowCheckbox(item);
			if (chk != null && chk.isChecked())
				n++;
		}
		return n;
	}

	private int countPending() {
		int n = 0;
		for (Listitem item : tblBpxf.getItems()) {
			Checkbox chk = getRowCheckbox(item);
			if (chk != null && !chk.isDisabled())
				n++;
		}
		return n;
	}

	private List<BpxfFile> getSelectedFiles() {
		List<BpxfFile> list = new ArrayList<>();
		for (Listitem item : tblBpxf.getItems()) {
			Checkbox chk = getRowCheckbox(item);
			if (chk != null && chk.isChecked()) {
				list.add((BpxfFile) item.getValue());
			}
		}
		return list;
	}

	private Checkbox getRowCheckbox(Listitem item) {
		if (item.getFirstChild() instanceof Listcell cell) {
			if (cell.getFirstChild() instanceof Checkbox chk)
				return chk;
		}
		return null;
	}

	private String badgeSclass(String status) {
		return switch (status) {
		case "PARSED" -> "badge badge-verified";
		case "FAILED" -> "badge badge-failed";
		default -> "badge badge-created";
		};
	}

	private String formatAmount(java.math.BigDecimal amount) {
		if (amount == null)
			return "—";
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("en", "IN"));
		nf.setMinimumFractionDigits(0);
		return "₹" + nf.format(amount);
	}
}