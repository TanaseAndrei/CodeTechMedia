package com.ucv.media.controller;

import com.ucv.media.controller.exception.AppException;
import com.ucv.media.facade.MediaFacade;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping
@AllArgsConstructor
public class MediaController {

    private final MediaFacade mediaFacade;

    @GetMapping(path = "/{folder}/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getMedia(@PathVariable("folder") String folder,
                                             @PathVariable("filename") String filename) {
        Resource resource = mediaFacade.getFile(folder, filename);
        MediaType mediaType = getMediaType(resource);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).headers(createHeader(resource)).body(resource);
    }

    @GetMapping(path = "/{folder}/zip-files", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> zipFiles(@PathVariable("folder") String folderName, @RequestBody List<String> fileNames) {
        Resource resource = mediaFacade.zipFiles(folderName, fileNames);
        MediaType mediaType = getMediaType(resource);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).headers(createHeader(resource)).body(resource);
    }

    @PostMapping(path = "/folder/{folder}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String moveFile(@PathVariable("folder") String folder, @RequestParam("file") MultipartFile multipartFile) {
        return mediaFacade.move(folder, multipartFile);
    }

    @PostMapping(path = "/folder", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createCourseFolder(@RequestBody String courseFolderName) {
        return mediaFacade.createCourseFolder(courseFolderName);
    }

    @PutMapping(path = "/folder/{folderName}/rename", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean renameFolder(@PathVariable("folderName") String oldFolderName, @RequestBody String newFolderName) {
        return mediaFacade.renameFolder(oldFolderName, newFolderName);
    }

    @DeleteMapping(path = "/{folder}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFiles(@PathVariable("folder") String folder, @RequestBody List<String> fileNames) {
        mediaFacade.deleteFiles(folder, fileNames);
    }

    @DeleteMapping(path = "/{folder}/{filename}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedia(@PathVariable("folder") String folder,
                            @PathVariable("filename") String filename) {
        mediaFacade.deleteFile(folder, filename);
    }

    @DeleteMapping(path = "/folder/{folderName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFolder(@PathVariable("folderName") String folderName) {
        mediaFacade.deleteFolder(folderName);
    }

    private MediaType getMediaType(Resource resource) {
        return MediaTypeFactory.getMediaType(resource)
                .orElseThrow(() -> new AppException("The media type could not be determined", HttpStatus.BAD_REQUEST));
    }

    private HttpHeaders createHeader(Resource resource) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;filename=" + resource.getFilename());
        return httpHeaders;
    }
}
