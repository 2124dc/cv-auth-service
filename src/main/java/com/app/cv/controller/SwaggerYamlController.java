package com.app.cv.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class SwaggerYamlController {

//    @GetMapping("/ams/api.yaml")
//    public ResponseEntity<Resource> getSwaggerYaml() {
//        Resource resource = new ClassPathResource("swagger/api.yaml");
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-yaml");
//        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
//    }

    @GetMapping("/ams/swagger/api.yaml")
    public ResponseEntity<String> getSwaggerYaml() {
        try {
            Resource resource = new ClassPathResource("swagger/api.yaml");
            String yamlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8");

            return new ResponseEntity<>(yamlContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
