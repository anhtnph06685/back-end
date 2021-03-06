package poly.com.web.rest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.com.client.dto.uploadFile.Base64ConvertToMultipartFile;
import poly.com.client.dto.uploadFile.ImageUploadForm;
import poly.com.client.dto.uploadFile.UploadFileResponse;
import poly.com.client.dto.uploadFile.UploadFilesResponse;
import poly.com.config.common.util.ResponseUtil;
import poly.com.service.UploadFileRoomService;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/api/upload")
public class FileUploadResource {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UploadFileRoomService uploadFileRoomService;

    @GetMapping(value = "/files/{fileName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte [] getFile(@PathVariable("fileName") String fileName){
       try {
           File file = new File(System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "picture-room" + File.separator + "images" + File.separator + fileName);
           InputStream inputStream = new FileInputStream(file);
           return IOUtils.toByteArray(inputStream);
       }
       catch (Exception e){
           log.error(this.getClass().getName(),e);
       }
       return null;
    }
    @PostMapping("/images")
    public ResponseEntity<UploadFilesResponse> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("roomId") String roomId
            ){
        try {
            UploadFilesResponse response = uploadFileRoomService.uploadFiles(files,roomId);
            return ResponseUtil.wrap(response);
        }
        catch (Exception e){
            return ResponseUtil.generateErrorResponse(e);
        }
    }
}
