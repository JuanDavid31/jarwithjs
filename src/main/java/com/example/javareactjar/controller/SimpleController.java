package com.example.javareactjar.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body("Simple Java Application is running!");
    }

    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"status\":\"UP\",\"message\":\"Java backend is running\"}");
    }

    @GetMapping("/api/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"application\":\"Simple Java Application\",\"framework\":\"Spring Boot\"}");
    }
}
