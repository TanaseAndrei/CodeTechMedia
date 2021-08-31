package com.ucv.media.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    private static final String SLASH = File.separator;

    @Value("${application.base-folder}")
    private String applicationBaseFolder;

    public String createCourseFolder(String courseName) throws IOException {
        log.info("Creating new course folder with the name {}", courseName);
        if (!Files.exists(Paths.get(applicationBaseFolder + courseName))) {
            Files.createDirectory(Paths.get(applicationBaseFolder + SLASH + courseName));
            log.info("Created new course folder with the name {}", courseName);
        }
        return courseName;
    }

    public String moveFile(String folder, MultipartFile multipartFile) throws IOException {
        log.info("Moving file with the name {} to the folder {}", multipartFile.getName(), folder);
        byte[] fileBytes = multipartFile.getBytes();
        String newFilename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        String savedFilePath = applicationBaseFolder + SLASH + folder + SLASH + newFilename;
        Files.write(Paths.get(savedFilePath), fileBytes);
        log.info("Moved file with the name {} to the folder {}", multipartFile.getName(), folder);
        return newFilename;
    }

    public boolean renameFolder(String folder, String newName) {
        log.info("Renaming folder from {} to {}", folder, newName);
        File targetFolder = new File(applicationBaseFolder + SLASH + folder);
        File newFolder = new File(applicationBaseFolder + SLASH + newName);
        log.info("Renamed folder from {} to {}", folder, newFolder);
        return targetFolder.renameTo(newFolder);
    }

    public void deleteFiles(String folder, List<String> fileNames) throws IOException {
        log.info("Deleting files {} from the folder {}", fileNames, folder);
        for (String fileName : fileNames) {
            deleteFile(folder, fileName);
        }
        log.info("Successfully deleted files {} from the folder {}", fileNames, folder);
    }

    public void deleteFolder(String folder) throws IOException {
        String pathname = applicationBaseFolder + SLASH + folder;
        FileUtils.deleteDirectory(new File(pathname));
    }

    public void deleteFile(String folder, String fileName) throws IOException {
        Files.delete(Paths.get(applicationBaseFolder + SLASH + folder + SLASH + fileName));
        log.info("Deleted file {} from the folder {}", fileName, folder);
    }

    public Resource getFileAsResource(String folder, String fileName) throws MalformedURLException {
        log.info("Retrieving file {} from the folder {}", fileName, folder);
        Path path = Paths.get(applicationBaseFolder + SLASH + folder + SLASH + fileName);
        return new UrlResource(path.toUri());
    }
}
