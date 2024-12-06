package com.nurijang.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${server.env}")
    private String env;

    @GetMapping("/env")
    public ResponseEntity<?> env() {
        return ResponseEntity.ok(env);
    }

}
