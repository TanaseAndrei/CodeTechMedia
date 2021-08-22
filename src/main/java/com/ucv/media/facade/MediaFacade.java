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
            return fileService.getFileAsResource(folderName, fileName);
        } catch (IOException ioException) {
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Resource zipFiles(String folderName, List<String> fileNames) {
        try {
            return zipService.zipFiles(fileNames, folderName);
        } catch (IOException ioException) {
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteFiles(String folder, List<String> fileNames) {
        try {
            fileService.deleteFiles(folder, fileNames);
        } catch (IOException ioException) {
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String createCourseFolder(String courseFolderName) {
        try {
            return fileService.createCourseFolder(courseFolderName);
        } catch (IOException ioException) {
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteFile(String folder, String fileName) {
        try {
            fileService.deleteFile(folder, fileName);
        } catch (IOException ioException) {
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean renameFolder(String oldFolderName, String newFolderName) {
        return fileService.renameFile(oldFolderName, newFolderName);
    }

    public void deleteFolder(String folderName) {
        try {
            fileService.deleteFolder(folderName);
        } catch (IOException ioException) {
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String move(String folder, MultipartFile multipartFile) {
        try {
            return fileService.moveFile(folder, multipartFile);
        } catch (IOException ioException) {
            throw new AppException(ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
