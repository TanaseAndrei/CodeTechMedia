package com.ucv.media.controller;

import com.ucv.media.controller.exception.AppException;
import com.ucv.media.controller.swagger.MediaApi;
import com.ucv.media.facade.MediaFacade;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping(path = "/media")
@AllArgsConstructor
public class MediaController implements MediaApi {

    private final MediaFacade mediaFacade;

    @PostMapping(path = "/folder", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createCourseFolder(@RequestBody @Valid @NotEmpty(message = "The name of the folder should not be empty") String courseFolderName) {
        return mediaFacade.createCourseFolder(courseFolderName);
    }

    @PostMapping(path = "/folder/{folder}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadFile(@PathVariable("folder") String folder,
                             @RequestParam("file") MultipartFile multipartFile) {
        return mediaFacade.move(folder, multipartFile);
    }

    @GetMapping(path = "/folder/{folder}/file/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable("folder") String folder,
                                                 @PathVariable("filename") String filename) {
        Resource resource = mediaFacade.getFile(folder, filename);
        MediaType mediaType = getMediaType(resource);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).headers(createHeader(resource)).body(resource);
    }

    @GetMapping(path = "/folder/{folder}/zip-files", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> zipFiles(@PathVariable("folder") String folderName,
                                             @RequestBody @Valid @NotEmpty(message = "The list of files should not be empty") List<String> fileNames) {
        Resource resource = mediaFacade.zipFiles(folderName, fileNames);
        MediaType mediaType = getMediaType(resource);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).headers(createHeader(resource)).body(resource);
    }

    @PutMapping(path = "/folder/{folderName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean renameFolder(@PathVariable("folderName") String oldFolderName,
                                @RequestBody @Valid @NotEmpty(message = "The name of the folder should not be empty") String newFolderName) {
        return mediaFacade.renameFolder(oldFolderName, newFolderName);
    }

    @DeleteMapping(path = "/folder/{folder}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFiles(@PathVariable("folder") String folder,
                            @RequestBody @Valid @NotEmpty(message = "The list of files should not be empty") List<String> fileNames) {
        mediaFacade.deleteFiles(folder, fileNames);
    }

    @DeleteMapping(path = "/folder/{folder}/file/{filename}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@PathVariable("folder") String folder,
                           @PathVariable("filename") String filename) {
        mediaFacade.deleteFile(folder, filename);
    }

    @DeleteMapping(path = "/folder/{folderName}")
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
