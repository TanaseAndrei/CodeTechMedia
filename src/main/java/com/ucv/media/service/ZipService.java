package com.ucv.media.service;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class ZipService {

    private static final String SLASH = File.separator;
    private static final String ZIP_EXTENSION = ".zip";

    @Value("${application.base-folder}")
    private String applicationBaseFolder;

    public Resource zipFiles(List<String> fileNames, String courseName) throws IOException {
        log.info("Zipping files {} from the folder {}", fileNames, courseName);
        String zipPath = applicationBaseFolder + SLASH + courseName + SLASH + System.currentTimeMillis() + ZIP_EXTENSION;
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            for (String filename : fileNames) {
                String filePathToZip = applicationBaseFolder + SLASH + courseName + SLASH + filename;
                zipFile.addFile(filePathToZip);
                log.info("Adding to zip, the file with the name {}", filePathToZip);
            }
        }
        return new UrlResource(Paths.get(zipPath).toUri());
    }
}
