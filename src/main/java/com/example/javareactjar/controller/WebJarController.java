package com.example.javareactjar.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/webjars")
public class WebJarController {

    /**
     * Serve WebJAR resources directly
     */
    @GetMapping(value = "/**", produces = {
        "application/javascript",
        "text/css",
        MediaType.TEXT_PLAIN_VALUE
    })
    @ResponseBody
    public ResponseEntity<String> getWebJarResource(HttpServletRequest request) {
        String path = request.getRequestURI().substring("/webjars/".length());
        
        try {
            ClassPathResource resource = new ClassPathResource("META-INF/resources/webjars/" + path);
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            
            // Determine content type based on file extension
            MediaType mediaType = MediaType.TEXT_PLAIN;
            if (path.endsWith(".js")) {
                mediaType = MediaType.valueOf("application/javascript");
            } else if (path.endsWith(".css")) {
                mediaType = MediaType.valueOf("text/css");
            }
            
            return ResponseEntity.ok()
                .contentType(mediaType)
                .body(content);
                
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get information about available WebJARs
     */
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getWebJarInfo() {
        Map<String, Object> webjarInfo = new HashMap<>();
        
        // jQuery information
        Map<String, Object> jquery = new HashMap<>();
        jquery.put("name", "jQuery");
        jquery.put("version", "3.6.0");
        jquery.put("description", "Fast, small, and feature-rich JavaScript library");
        jquery.put("mainFile", "/webjars/jquery/3.6.0/jquery.min.js");
        jquery.put("cdnPath", "https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js");
        
        // Bootstrap information
        Map<String, Object> bootstrap = new HashMap<>();
        bootstrap.put("name", "Bootstrap");
        bootstrap.put("version", "5.1.3");
        bootstrap.put("description", "The world's most popular front-end open source toolkit");
        bootstrap.put("mainFiles", Map.of(
            "css", "/webjars/bootstrap/5.1.3/css/bootstrap.min.css",
            "js", "/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"
        ));
        bootstrap.put("cdnPaths", Map.of(
            "css", "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css",
            "js", "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        ));
        
        // Lodash information
        Map<String, Object> lodash = new HashMap<>();
        lodash.put("name", "Lodash");
        lodash.put("version", "4.17.21");
        lodash.put("description", "A modern JavaScript utility library delivering modularity, performance & extras");
        lodash.put("mainFile", "/webjars/lodash/4.17.21/lodash.min.js");
        lodash.put("securityNote", "Using secure version 4.17.21 - version 4.17.4 was quarantined by Sonatype!");
        lodash.put("cdnPath", "https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.21/lodash.min.js");
        
        // AngularJS information
        Map<String, Object> angularjs = new HashMap<>();
        angularjs.put("name", "AngularJS");
        angularjs.put("version", "1.8.3");
        angularjs.put("description", "The original AngularJS framework - HTML enhanced for web apps!");
        angularjs.put("mainFile", "/webjars/angularjs/1.8.3/angular.min.js");
        angularjs.put("additionalModules", Map.of(
            "animate", "/webjars/angularjs/1.8.3/angular-animate.min.js",
            "route", "/webjars/angularjs/1.8.3/angular-route.min.js",
            "resource", "/webjars/angularjs/1.8.3/angular-resource.min.js"
        ));
        angularjs.put("cdnPath", "https://ajax.googleapis.com/ajax/libs/angularjs/1.8.3/angular.min.js");
        
        webjarInfo.put("jquery", jquery);
        webjarInfo.put("bootstrap", bootstrap);
        webjarInfo.put("lodash", lodash);
        webjarInfo.put("angularjs", angularjs);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "WebJAR JavaScript libraries embedded in this JAR");
        response.put("webjars", webjarInfo);
        response.put("usage", Map.of(
            "directAccess", "Access files via /webjars/{library}/{version}/{file}",
            "htmlExample", "Example: <script src=\"/webjars/jquery/3.6.0/jquery.min.js\"></script>",
            "note", "All JavaScript libraries are packaged inside the JAR file - no external CDN required!"
        ));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Demo page showing WebJAR usage
     */
    @GetMapping("/demo")
    public String webjarDemo() {
        return "webjar-demo"; // Returns webjar-demo.html template
    }

    /**
     * AngularJS specific demo page
     */
    @GetMapping("/angularjs-demo")
    public String angularjsDemo() {
        return "angularjs-demo"; // Returns angularjs-demo.html template
    }

    /**
     * Get WebJAR file sizes and metadata
     */
    @GetMapping("/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getWebJarStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Check file sizes and existence
        String[] libraries = {
            "META-INF/resources/webjars/jquery/3.6.0/jquery.min.js",
            "META-INF/resources/webjars/bootstrap/5.1.3/css/bootstrap.min.css",
            "META-INF/resources/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js",
            "META-INF/resources/webjars/lodash/4.17.21/lodash.min.js",
            "META-INF/resources/webjars/angularjs/1.8.3/angular.min.js"
        };
        
        Map<String, Object> fileStats = new HashMap<>();
        long totalSize = 0;
        
        for (String libPath : libraries) {
            try {
                ClassPathResource resource = new ClassPathResource(libPath);
                if (resource.exists()) {
                    long size = resource.contentLength();
                    totalSize += size;
                    
                    String[] parts = libPath.split("/");
                    String libName = parts[4]; // Extract library name
                    String fileName = parts[parts.length - 1]; // Extract file name
                    
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("exists", true);
                    fileInfo.put("size", size);
                    fileInfo.put("sizeFormatted", formatBytes(size));
                    fileInfo.put("path", "/" + libPath.replace("META-INF/resources/", ""));
                    
                    fileStats.put(libName + "/" + fileName, fileInfo);
                }
            } catch (IOException e) {
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("exists", false);
                fileInfo.put("error", e.getMessage());
                fileStats.put(libPath, fileInfo);
            }
        }
        
        stats.put("totalLibraries", 4);
        stats.put("totalSize", totalSize);
        stats.put("totalSizeFormatted", formatBytes(totalSize));
        stats.put("files", fileStats);
        stats.put("note", "All JavaScript files are embedded in the JAR and served from memory");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "WebJAR statistics and file information");
        response.put("stats", stats);
        
        return ResponseEntity.ok(response);
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return (bytes / (1024 * 1024)) + " MB";
    }
}
