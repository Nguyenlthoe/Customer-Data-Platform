package bk.edu.controller;

import bk.edu.data.response.base.MyResponse;
import bk.edu.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
    UploadService uploadService;

    @PostMapping(value = "/upload")
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) {
        String url = uploadService.uploadFile(file);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(url)
                .get();

        return ResponseEntity.ok(response);
    }
}
