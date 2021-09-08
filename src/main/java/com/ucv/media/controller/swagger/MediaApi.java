package com.ucv.media.controller.swagger;

import com.ucv.media.controller.exception.dto.AppExceptionDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(value = "The media API used by CodeTech")
public interface MediaApi {

    @ApiOperation(value = "Downloads the targeted file", httpMethod = "GET",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully downloaded the file", response = Resource.class),
            @ApiResponse(code = 400, message = "The input is invalid", response = AppExceptionDto.class),
            @ApiResponse(code = 404, message = "File not found", response = AppExceptionDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = void.class)
    })
    ResponseEntity<Resource> downloadFile(@Schema(description = "The target folder", example = "Java101") String folder,
                                          @Schema(description = "The target file", example = "12n3o4-12o3ipv-o1i2c321323r.jpg") String filename);

    @ApiOperation(value = "Downloads the zip containing the targeted files", httpMethod = "GET",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, response = Resource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully downloaded the zip", response = Resource.class),
            @ApiResponse(code = 400, message = "The input is invalid", response = AppExceptionDto.class),
            @ApiResponse(code = 404, message = "The target folder does not exist", response = AppExceptionDto.class),
            @ApiResponse(code = 404, message = "One of the target files does not exist", response = AppExceptionDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = void.class)
    })
    ResponseEntity<Resource> zipFiles(@Schema(description = "The target folder", example = "Java101") String folderName,
                                      @Schema(description = "The wanted files", example = "[file1, file2, file3]") List<String> fileNames);

    @ApiOperation(value = "Creates a new file in the targeted folder", httpMethod = "POST",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the file in the targeted folder and the new name of the file is returned",
                    response = String.class),
            @ApiResponse(code = 400, message = "The input is invalid", response = AppExceptionDto.class),
            @ApiResponse(code = 404, message = "The target folder does not exist", response = AppExceptionDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = void.class)
    })
    String uploadFile(@Schema(description = "The target folder", example = "Java101") String folder,
                      @Schema(description = "The file") MultipartFile multipartFile);

    @ApiOperation(value = "Creates a new folder and returns the name of it", httpMethod = "POST",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the new folder and returns the name of it", response = String.class),
            @ApiResponse(code = 400, message = "The input is invalid", response = AppExceptionDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = void.class)
    })
    String createCourseFolder(@Schema(description = "The name of the folder that we want to create", example = "Java101") String courseFolderName);

    @ApiOperation(value = "Rename a target folder", httpMethod = "PUT",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Boolean if the renaming was done with success or not", response = Boolean.class),
            @ApiResponse(code = 404, message = "The target folder does not exist", response = AppExceptionDto.class),
            @ApiResponse(code = 400, message = "The input is invalid", response = AppExceptionDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = void.class)
    })
    boolean renameFolder(@Schema(description = "The name of the folder that we want to change", example = "Java101") String oldFolderName,
                         @Schema(description = "The name of the new folder", example = "Java102") String newFolderName);

    @ApiOperation(value = "Delete the given files from a targeted folder", httpMethod = "DELETE",
            consumes = MediaType.APPLICATION_JSON_VALUE, response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted the given files", response = void.class),
            @ApiResponse(code = 400, message = "The input is invalid", response = AppExceptionDto.class),
            @ApiResponse(code = 404, message = "The target folder does not exist", response = AppExceptionDto.class),
            @ApiResponse(code = 404, message = "One of the target files does not exist", response = AppExceptionDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = void.class)
    })
    void deleteFiles(@Schema(description = "The target folder", example = "Java101") String folder,
                     @Schema(description = "The files that we want to delete", example = "[12c312v-12v3123-v3242.jpg, 2v12v43-123v4123-v53bk.png]") List<String> fileNames);

    @ApiOperation(value = "Delete the given file from a targeted folder", httpMethod = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted the given files", response = void.class),
            @ApiResponse(code = 400, message = "The input is invalid", response = AppExceptionDto.class),
            @ApiResponse(code = 404, message = "The target folder does not exist", response = AppExceptionDto.class),
            @ApiResponse(code = 404, message = "The target file does not exist", response = AppExceptionDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = void.class)
    })
    void deleteFile(@Schema(description = "The target folder", example = "Java101") String folder,
                    @Schema(description = "The target filename", example = "Java101") String filename);

    @ApiOperation(value = "Delete the given files from a targeted folder", httpMethod = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted the given files", response = void.class),
            @ApiResponse(code = 400, message = "The input is invalid", response = AppExceptionDto.class),
            @ApiResponse(code = 404, message = "The target folder does not exist", response = AppExceptionDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = void.class)
    })
    void deleteFolder(@Schema(description = "The target folder", example = "Java101") String folderName);
}
