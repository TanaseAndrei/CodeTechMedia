package com.ucv.media.facade;

import com.ucv.media.CodeTechMediaApplication.Facade;
import com.ucv.media.controller.exception.AppException;
import com.ucv.media.service.FileService;
import com.ucv.media.service.ZipService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Facade
@AllArgsConstructor
@Slf4j
public class MediaFacade {

    private final FileService fileService;
    private final ZipService zipService;

    public Resource getFile(String folderName, String fileName) {
        try {
            log.info("Retrieving from the folder {}, the file with the name {}", folderName, fileName);
            Resource file = fileService.getFileAsResource(folderName, fileName);
            log.info("Retrieved from the folder {}, the file with the name {}", folderName, fileName);
            return file;
        } catch (IOException ioException) {
            log.error("Error while retrieving the file {} from the folder {}", fileName, folderName, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Resource zipFiles(String folderName, List<String> fileNames) {
        try {
            log.info("Zipping {} files from the folder {}", fileNames.size(), folderName);
            Resource zipFile = zipService.zipFiles(fileNames, folderName);
            log.info("Zipped {} files from the folder {}", fileNames.size(), folderName);
            return zipFile;
        } catch (IOException ioException) {
            log.error("Error while zipping the {} files from the folder {}", fileNames.size(), folderName, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteFiles(String folder, List<String> fileNames) {
        try {
            log.info("Deleting {} files from folder {}", fileNames.size(), folder);
            fileService.deleteFiles(folder, fileNames);
            log.info("Deleted {} files from the folder {}", fileNames.size(), folder);
        } catch (IOException ioException) {
            log.error("Error while deleting {} files from folder {}", fileNames.size(), folder, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String createCourseFolder(String courseFolderName) {
        try {
            log.info("Creating a new folder for the course {}", courseFolderName);
            return fileService.createCourseFolder(courseFolderName);
        } catch (IOException ioException) {
            log.error("Error while creating a new folder for the course {}", courseFolderName, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteFile(String folder, String fileName) {
        try {
            log.info("Deleting the file {} from the folder {}", fileName, folder);
            fileService.deleteFile(folder, fileName);
            log.info("Deleting the file {} from the folder {}", fileName, folder);
        } catch (IOException ioException) {
            log.error("Error while deleting the file {} from the folder {}", fileName, folder, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean renameFolder(String oldFolderName, String newFolderName) {
        log.info("Renaming the folder {} to {}", oldFolderName, newFolderName);
        return fileService.renameFolder(oldFolderName, newFolderName);
    }

    public void deleteFolder(String folderName) {
        try {
            log.info("Deleting the folder {}", folderName);
            fileService.deleteFolder(folderName);
            log.info("Deleted the folder {}", folderName);
        } catch (IOException ioException) {
            log.error("Error while deleting the folder {}", folderName, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String move(String folder, MultipartFile multipartFile) {
        try {
            checkFile(multipartFile);
            String fileName = fileService.moveFile(folder, multipartFile);
            log.info("Moved the file {} to the folder {}, having a new name {}", multipartFile.getOriginalFilename(), folder, fileName);
            return fileName;
        } catch (IOException ioException) {
            log.error("Error while moving the file {} to the folder {}", multipartFile.getOriginalFilename(), folder, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void checkFile(MultipartFile multipartFile) {
        if(multipartFile == null || multipartFile.getSize() == 0 || multipartFile.getName().isEmpty()) {
            throw new AppException("The file must not be null", HttpStatus.BAD_REQUEST);
        }
    }
}
