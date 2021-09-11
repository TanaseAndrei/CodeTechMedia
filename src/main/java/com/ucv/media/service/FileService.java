package com.ucv.media.service;

import com.ucv.media.controller.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
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
    private static final String FOLDER_DOES_NOT_EXIST_LOG_MESSAGE = "The folder {} does not exist";

    @Value("${application.base-folder}")
    private String applicationBaseFolder;

    public String createCourseFolder(String folder) throws IOException {
        log.info("Creating new course folder with the name {}", folder);
        if (folderExists(folder)) {
            log.warn("The folder {} already exists", folder);
            throw new AppException("The folder " + folder + " already exists", HttpStatus.BAD_REQUEST);
        }
        Files.createDirectory(Paths.get(applicationBaseFolder + SLASH + folder));
        log.info("Created new course folder with the name {}", folder);
        return folder;
    }

    public String moveFile(String folder, MultipartFile multipartFile) throws IOException {
        log.info("Moving file with the name {} to the folder {}", multipartFile.getOriginalFilename(), folder);
        if (!folderExists(folder)) {
            log.warn(FOLDER_DOES_NOT_EXIST_LOG_MESSAGE, folder);
            throw new AppException("The folder " + folder + " does not exist", HttpStatus.NOT_FOUND);
        }
        byte[] fileBytes = multipartFile.getBytes();
        String newFilename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        String savedFilePath = applicationBaseFolder + SLASH + folder + SLASH + newFilename;
        Files.write(Paths.get(savedFilePath), fileBytes);
        log.info("Moved file with the name {} to the folder {}", multipartFile.getOriginalFilename(), folder);
        return newFilename;
    }

    public boolean renameFolder(String folder, String newName) {
        log.info("Renaming folder from {} to {}", folder, newName);
        if (!folderExists(folder)) {
            log.warn(FOLDER_DOES_NOT_EXIST_LOG_MESSAGE, folder);
            throw new AppException("The folder " + folder + " does not exist", HttpStatus.NOT_FOUND);
        }
        File targetFolder = new File(applicationBaseFolder + SLASH + folder);
        File newFolder = new File(applicationBaseFolder + SLASH + newName);
        log.info("Renamed folder from {} to {}", folder, newFolder);
        return targetFolder.renameTo(newFolder);
    }

    public void deleteFiles(String folder, List<String> fileNames) throws IOException {
        if (!folderExists(folder)) {
            log.warn(FOLDER_DOES_NOT_EXIST_LOG_MESSAGE, folder);
            throw new AppException("The folder " + folder + " does not exist", HttpStatus.NOT_FOUND);
        }
        checkIfFilesExistInFolder(folder, fileNames);
        log.info("Deleting files {} from the folder {}", fileNames, folder);
        for (String fileName : fileNames) {
            deleteFile(folder, fileName);
        }
        log.info("Successfully deleted files {} from the folder {}", fileNames, folder);
    }

    public void deleteFile(String folder, String fileName) throws IOException {
        if (!folderExists(folder)) {
            log.warn(FOLDER_DOES_NOT_EXIST_LOG_MESSAGE, folder);
            throw new AppException("The folder " + folder + " does not exist", HttpStatus.NOT_FOUND);
        }
        checkIfFileExistsInFolder(folder, fileName);
        Files.delete(Paths.get(applicationBaseFolder + SLASH + folder + SLASH + fileName));
        log.info("Deleted file {} from the folder {}", fileName, folder);
    }

    public Resource getFileAsResource(String folder, String fileName) throws MalformedURLException {
        if (!folderExists(folder)) {
            log.warn(FOLDER_DOES_NOT_EXIST_LOG_MESSAGE, folder);
            throw new AppException("The folder " + folder + " does not exist", HttpStatus.NOT_FOUND);
        }
        checkIfFileExistsInFolder(folder, fileName);
        log.info("Retrieving file {} from the folder {}", fileName, folder);
        Path path = Paths.get(applicationBaseFolder + SLASH + folder + SLASH + fileName);
        return new UrlResource(path.toUri());
    }

    private void checkIfFilesExistInFolder(String folder, List<String> fileNames) {
        for (String fileName : fileNames) {
            checkIfFileExistsInFolder(folder, fileName);
        }
    }

    private void checkIfFileExistsInFolder(String folder, String fileName) {
        if (!new File(applicationBaseFolder + SLASH + folder, fileName).exists()) {
            throw new AppException("The file " + fileName + " does not exist in the folder " + folder, HttpStatus.NOT_FOUND);
        }
    }

    private boolean folderExists(String courseName) {
        return Files.exists(Paths.get(applicationBaseFolder + SLASH + courseName));
    }
}
