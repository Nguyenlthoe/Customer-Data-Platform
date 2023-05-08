package bk.edu.controller;

import bk.edu.data.response.base.MyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @RequestMapping(value = "/api/v1/ping", method = RequestMethod.GET)
    public ResponseEntity<?> ping() {

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("pong")
                .get();
        return ResponseEntity.ok(response);
    }
}
