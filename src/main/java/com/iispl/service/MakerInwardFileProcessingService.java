package com.iispl.service;

import java.nio.file.Path;
import java.util.List;

import com.iispl.entity.BpxfFile;
import com.iispl.entity.InwardCheque;

public interface MakerInwardFileProcessingService {
    List<BpxfFile> scanWatchFolder();
    void startWatchService(Runnable onNewFileDetected);
    void stopWatchService();
    List<InwardCheque> parseAndPersist(List<BpxfFile> selectedFiles);
    BpxfFile peekAndBuild(Path filePath);
}
