package com.ucv.media.controller.swagger;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaApi {

    ResponseEntity<Resource> getMedia(String folder, String filename);

    ResponseEntity<Resource> zipFiles(String folderName, List<String> fileNames);

    String moveFile(String folder, MultipartFile multipartFile);

    String createCourseFolder(String courseFolderName);

    boolean renameFolder(String oldFolderName, String newFolderName);

    void deleteFiles(String folder, List<String> fileNames);

    void deleteMedia(String folder, String filename);

    void deleteFolder(String folderName);
}
