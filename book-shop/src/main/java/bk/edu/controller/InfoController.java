package bk.edu.controller;

import bk.edu.data.response.base.MyResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class InfoController {
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<?> ping() {

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("pong")
                .get();
        return ResponseEntity.ok(response);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        return restTemplate;
    }
}
