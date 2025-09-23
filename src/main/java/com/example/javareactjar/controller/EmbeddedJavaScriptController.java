package com.example.javareactjar.controller;

import com.example.javareactjar.service.EmbeddedJavaScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/embedded-js")
public class EmbeddedJavaScriptController {

    @Autowired
    private EmbeddedJavaScriptService embeddedJsService;

    /**
     * Get all available embedded JavaScript functions
     */
    @GetMapping("/scripts")
    public ResponseEntity<Map<String, Object>> getAvailableScripts() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("scripts", embeddedJsService.getAvailableScripts());
        response.put("description", "Embedded JavaScript functions available in this JAR");
        return ResponseEntity.ok(response);
    }

    /**
     * Calculate advanced discount using embedded business rules
     */
    @PostMapping("/business/discount")
    public ResponseEntity<Map<String, Object>> calculateDiscount(@RequestBody Map<String, Object> orderData) {
        try {
            Map<String, Object> result = embeddedJsService.calculateAdvancedDiscount(orderData);
            return ResponseEntity.ok(createResponse(true, result, "Discount calculated successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createResponse(false, null, "Failed to calculate discount: " + e.getMessage()));
        }
    }

    /**
     * Validate complex form using embedded validation rules
     */
    @PostMapping("/business/validate")
    public ResponseEntity<Map<String, Object>> validateForm(@RequestBody Map<String, Object> formData) {
        try {
            Map<String, Object> result = embeddedJsService.validateComplexForm(formData);
            return ResponseEntity.ok(createResponse(true, result, "Validation completed"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createResponse(false, null, "Validation failed: " + e.getMessage()));
        }
    }

    /**
     * Analyze numerical data using embedded statistical functions
     */
    @PostMapping("/business/analyze")
    public ResponseEntity<Map<String, Object>> analyzeData(@RequestBody Map<String, Object> request) {
        try {
            Object dataset = request.get("dataset");
            Map<String, Object> result = embeddedJsService.analyzeData(dataset);
            return ResponseEntity.ok(createResponse(true, result, "Data analysis completed"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createResponse(false, null, "Analysis failed: " + e.getMessage()));
        }
    }

    /**
     * Parse CSV data using embedded parser
     */
    @PostMapping("/data/parse-csv")
    public ResponseEntity<Map<String, Object>> parseCSV(@RequestBody Map<String, Object> request) {
        try {
            String csvData = (String) request.get("csvData");
            @SuppressWarnings("unchecked")
            Map<String, Object> options = (Map<String, Object>) request.getOrDefault("options", new HashMap<>());
            
            Map<String, Object> result = embeddedJsService.parseCSV(csvData, options);
            return ResponseEntity.ok(createResponse(true, result, "CSV parsed successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createResponse(false, null, "CSV parsing failed: " + e.getMessage()));
        }
    }

    /**
     * Query JSON data using path syntax
     */
    @PostMapping("/data/query-json")
    public ResponseEntity<Map<String, Object>> queryJSON(@RequestBody Map<String, Object> request) {
        try {
            Object jsonData = request.get("data");
            String path = (String) request.get("path");
            
            Map<String, Object> result = embeddedJsService.queryJSON(jsonData, path);
            return ResponseEntity.ok(createResponse(true, result, "JSON query executed"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createResponse(false, null, "JSON query failed: " + e.getMessage()));
        }
    }

    /**
     * Clean data using specified rules
     */
    @PostMapping("/data/clean")
    public ResponseEntity<Map<String, Object>> cleanData(@RequestBody Map<String, Object> request) {
        try {
            Object rawData = request.get("data");
            @SuppressWarnings("unchecked")
            Map<String, Object> rules = (Map<String, Object>) request.get("rules");
            
            Map<String, Object> result = embeddedJsService.cleanData(rawData, rules);
            return ResponseEntity.ok(createResponse(true, result, "Data cleaning completed"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createResponse(false, null, "Data cleaning failed: " + e.getMessage()));
        }
    }

    /**
     * Execute custom JavaScript with embedded utilities
     */
    @PostMapping("/custom/execute")
    public ResponseEntity<Map<String, Object>> executeCustomScript(@RequestBody Map<String, Object> request) {
        try {
            String jsCode = (String) request.get("code");
            @SuppressWarnings("unchecked")
            Map<String, Object> context = (Map<String, Object>) request.getOrDefault("context", new HashMap<>());
            
            if (jsCode == null || jsCode.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createResponse(false, null, "JavaScript code is required"));
            }
            
            Map<String, Object> result = embeddedJsService.executeCustomScript(jsCode, context);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createResponse(false, null, "Custom script execution failed: " + e.getMessage()));
        }
    }

    /**
     * Benchmark JavaScript execution performance
     */
    @PostMapping("/benchmark")
    public ResponseEntity<Map<String, Object>> benchmark(@RequestBody Map<String, Object> request) {
        try {
            String scriptName = (String) request.get("scriptName");
            String functionName = (String) request.get("functionName");
            @SuppressWarnings("unchecked")
            Map<String, Object> parameters = (Map<String, Object>) request.getOrDefault("parameters", new HashMap<>());
            Integer iterations = (Integer) request.getOrDefault("iterations", 100);
            
            if (scriptName == null || functionName == null) {
                return ResponseEntity.badRequest()
                    .body(createResponse(false, null, "scriptName and functionName are required"));
            }
            
            Map<String, Object> result = embeddedJsService.benchmarkExecution(scriptName, functionName, parameters, iterations);
            return ResponseEntity.ok(createResponse(true, result, "Benchmark completed"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createResponse(false, null, "Benchmark failed: " + e.getMessage()));
        }
    }

    /**
     * Demo endpoint showing various embedded JavaScript capabilities
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo() {
        Map<String, Object> demos = new HashMap<>();

        try {
            // Demo 1: Advanced discount calculation
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("price", 100.0);
            orderData.put("quantity", 15);
            orderData.put("customerTier", "gold");
            orderData.put("category", "electronics");
            orderData.put("value", 1500.0);
            
            Map<String, Object> discountResult = embeddedJsService.calculateAdvancedDiscount(orderData);
            demos.put("advancedDiscount", discountResult);

            // Demo 2: Form validation
            Map<String, Object> formData = new HashMap<>();
            formData.put("email", "user@example.com");
            formData.put("password", "MyPassword123!");
            formData.put("age", 25);
            formData.put("name", "John Doe");
            formData.put("phone", "+1-555-0123");
            formData.put("address", "123 Main St, Anytown, USA");
            
            Map<String, Object> validationResult = embeddedJsService.validateComplexForm(formData);
            demos.put("formValidation", validationResult);

            // Demo 3: Data analysis
            Object[] dataset = {1.5, 2.3, 3.7, 2.1, 4.5, 3.2, 2.8, 3.9, 4.1, 2.5, 3.3, 4.7, 2.9, 3.1, 4.3};
            Map<String, Object> analysisResult = embeddedJsService.analyzeData(dataset);
            demos.put("dataAnalysis", analysisResult);

            // Demo 4: CSV parsing
            String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,25,Los Angeles\nBob Johnson,35,Chicago";
            Map<String, Object> csvOptions = new HashMap<>();
            csvOptions.put("hasHeader", true);
            csvOptions.put("delimiter", ",");
            
            Map<String, Object> csvResult = embeddedJsService.parseCSV(csvData, csvOptions);
            demos.put("csvParsing", csvResult);

            // Demo 5: JSON querying
            Map<String, Object> jsonData = new HashMap<>();
            Map<String, Object> user = new HashMap<>();
            user.put("name", "Alice");
            user.put("age", 28);
            Map<String, String> address = new HashMap<>();
            address.put("street", "456 Oak Ave");
            address.put("city", "Seattle");
            user.put("address", address);
            jsonData.put("user", user);
            
            Map<String, Object> queryResult = embeddedJsService.queryJSON(jsonData, "user.address.city");
            demos.put("jsonQuerying", queryResult);

        } catch (Exception e) {
            demos.put("error", "Demo execution failed: " + e.getMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Embedded JavaScript demonstrations");
        response.put("demos", demos);
        response.put("note", "All JavaScript code is embedded within the JAR file");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get embedded script source code for inspection
     */
    @GetMapping("/source/{scriptName}")
    public ResponseEntity<Map<String, Object>> getScriptSource(@PathVariable String scriptName) {
        try {
            String scriptContent = embeddedJsService.loadEmbeddedScript(scriptName);
            if (scriptContent == null) {
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("scriptName", scriptName);
            response.put("content", scriptContent);
            response.put("lines", scriptContent.split("\n").length);
            response.put("size", scriptContent.length());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createResponse(false, null, "Failed to load script: " + e.getMessage()));
        }
    }

    // Utility method to create consistent response format
    private Map<String, Object> createResponse(boolean success, Object result, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        
        if (result != null) {
            response.put("result", result);
        }
        
        return response;
    }
}
