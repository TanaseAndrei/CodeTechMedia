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
            fileService.deleteFiles(folder, fileNames);
            log.info("Deleted {} files from the folder {}", fileNames.size(), folder);
        } catch (IOException ioException) {
            log.error("Error while deleting {} files from folder {}", fileNames.size(), folder, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String createCourseFolder(String courseFolderName) {
        try {
            return fileService.createCourseFolder(courseFolderName);
        } catch (IOException ioException) {
            log.error("Error while creating a new folder for the course {}", courseFolderName, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteFile(String folder, String fileName) {
        try {
            fileService.deleteFile(folder, fileName);
        } catch (IOException ioException) {
            log.error("Error while deleting the file {} from the folder {}", fileName, folder, ioException);
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean renameFolder(String oldFolderName, String newFolderName) {
        return fileService.renameFolder(oldFolderName, newFolderName);
    }

    public String move(String folder, MultipartFile multipartFile) {
        try {
            checkFile(multipartFile);
            return fileService.moveFile(folder, multipartFile);
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
