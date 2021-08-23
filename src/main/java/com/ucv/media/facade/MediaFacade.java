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
            return fileService.getFileAsResource(folderName, fileName);
        } catch (IOException ioException) {
            log.error("Error while retrieving the file {} from the folder {}", fileName, folderName, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Resource zipFiles(String folderName, List<String> fileNames) {
        try {
            log.info("Zipping {} files from folder {}", fileNames.size(), folderName);
            return zipService.zipFiles(fileNames, folderName);
        } catch (IOException ioException) {
            log.error("Error while zipping the {} files from the folder {}", fileNames.size(), folderName, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteFiles(String folder, List<String> fileNames) {
        try {
            fileService.deleteFiles(folder, fileNames);
            log.info("Deleted {} files from folder {}", fileNames.size(), folder);
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
            fileService.deleteFile(folder, fileName);
            log.info("Deleting the file {} from the folder {}", fileName, folder);
        } catch (IOException ioException) {
            log.error("Error while deleting the file {} from the folder {}", fileName, folder, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean renameFolder(String oldFolderName, String newFolderName) {
        log.info("Renaming the folder {} to {}", oldFolderName, newFolderName);
        return fileService.renameFile(oldFolderName, newFolderName);
    }

    public void deleteFolder(String folderName) {
        try {
            fileService.deleteFolder(folderName);
            log.info("Deleted the folder {}", folderName);
        } catch (IOException ioException) {
            log.error("Error while deleting the folder {}", folderName, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String move(String folder, MultipartFile multipartFile) {
        try {
            log.info("Moving the file {} to folder {}", multipartFile.getName(), folder);
            return fileService.moveFile(folder, multipartFile);
        } catch (IOException ioException) {
            log.error("Error while moving the file {} to the folder {}", multipartFile.getName(), folder, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
