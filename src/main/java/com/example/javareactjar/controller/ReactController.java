package com.example.javareactjar.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class ReactController {

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<Resource> index() {
        return serveReactApp();
    }

    @GetMapping("/api/health")
    @ResponseBody
    public ResponseEntity<String> health() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"status\":\"UP\",\"message\":\"Java backend with embedded React is running\"}");
    }

    @GetMapping("/api/info")
    @ResponseBody
    public ResponseEntity<String> info() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"backend\":\"Spring Boot\",\"frontend\":\"React 17\",\"embedded\":true}");
    }

    // Catch-all handler for React Router (SPA routing)
    @RequestMapping(value = "/{path:^(?!api|static).*}")
    @ResponseBody
    public ResponseEntity<Resource> fallback() {
        return serveReactApp();
    }

    private ResponseEntity<Resource> serveReactApp() {
        try {
            Resource resource = new ClassPathResource("static/index.html");
            if (resource.exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_HTML);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
