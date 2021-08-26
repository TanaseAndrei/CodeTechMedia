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
        if (!Files.exists(Paths.get(applicationBaseFolder + courseName))) {
            Files.createDirectory(Paths.get(applicationBaseFolder + SLASH + courseName));
        }
        return courseName;
    }

    public String moveFile(String folder, MultipartFile multipartFile) throws IOException {
        byte[] fileBytes = multipartFile.getBytes();
        String newFilename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        String savedFilePath = applicationBaseFolder + SLASH + folder + SLASH + newFilename;
        Files.write(Paths.get(savedFilePath), fileBytes);
        return newFilename;
    }

    public boolean renameFile(String folder, String newName) {
        File targetFolder = new File(applicationBaseFolder + SLASH + folder);
        File newFolder = new File(applicationBaseFolder + SLASH + newName);
        return targetFolder.renameTo(newFolder);
    }

    public void deleteFiles(String folder, List<String> fileNames) throws IOException {
        for (String fileName : fileNames) {
            deleteFile(folder, fileName);
        }
    }

    public void deleteFolder(String folder) throws IOException {
        String pathname = applicationBaseFolder + SLASH + folder;
        FileUtils.deleteDirectory(new File(pathname));
    }

    public void deleteFile(String folder, String fileName) throws IOException {
        Files.delete(Paths.get(applicationBaseFolder + SLASH + folder + SLASH + fileName));
    }

    public Resource getFileAsResource(String folder, String fileName) throws MalformedURLException {
        Path path = Paths.get(applicationBaseFolder + SLASH + folder + SLASH + fileName);
        return new UrlResource(path.toUri());
    }
}
