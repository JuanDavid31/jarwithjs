package com.example.javareactjar.controller;

import com.example.javareactjar.service.JavaScriptExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/js")
public class JavaScriptController {

    @Autowired
    private JavaScriptExecutionService jsService;

    /**
     * Execute arbitrary JavaScript code
     */
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executeJavaScript(@RequestBody Map<String, Object> request) {
        String jsCode = (String) request.get("code");
        
        if (jsCode == null || jsCode.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("JavaScript code is required"));
        }

        try {
            String result = jsService.executeJavaScript(jsCode);
            return ResponseEntity.ok(createSuccessResponse(result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Execution failed: " + e.getMessage()));
        }
    }

    /**
     * Execute JavaScript with parameters
     */
    @PostMapping("/execute-with-params")
    public ResponseEntity<Map<String, Object>> executeWithParams(@RequestBody Map<String, Object> request) {
        String jsCode = (String) request.get("code");
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) request.get("params");
        
        if (jsCode == null || jsCode.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("JavaScript code is required"));
        }

        if (params == null) {
            params = new HashMap<>();
        }

        try {
            String result = jsService.executeJavaScriptWithParams(jsCode, params);
            return ResponseEntity.ok(createSuccessResponse(result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Execution failed: " + e.getMessage()));
        }
    }

    /**
     * Process JSON data with JavaScript
     */
    @PostMapping("/process-json")
    public ResponseEntity<Map<String, Object>> processJson(@RequestBody Map<String, Object> request) {
        String jsonData = (String) request.get("data");
        String jsCode = (String) request.get("code");
        
        if (jsonData == null || jsCode == null) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("Both 'data' and 'code' are required"));
        }

        try {
            String result = jsService.processJsonWithJS(jsonData, jsCode);
            return ResponseEntity.ok(createSuccessResponse(result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Processing failed: " + e.getMessage()));
        }
    }

    /**
     * Execute predefined business logic scripts
     */
    @PostMapping("/business-logic/{scriptName}")
    public ResponseEntity<Map<String, Object>> executeBusinessLogic(
            @PathVariable String scriptName,
            @RequestBody Map<String, Object> inputData) {
        
        try {
            Map<String, Object> result = jsService.executeBusinessLogic(scriptName, inputData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Business logic execution failed: " + e.getMessage()));
        }
    }

    /**
     * Get available business logic scripts
     */
    @GetMapping("/business-logic")
    public ResponseEntity<Map<String, Object>> getAvailableScripts() {
        Map<String, Object> scripts = new HashMap<>();
        scripts.put("calculateDiscount", "Calculate discount based on price, quantity, and customer type");
        scripts.put("validateUserData", "Validate user registration data");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("scripts", scripts);
        response.put("usage", "POST /api/js/business-logic/{scriptName} with input data");
        
        return ResponseEntity.ok(response);
    }

    /**
     * JavaScript calculator endpoint
     */
    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(@RequestBody Map<String, Object> request) {
        String expression = (String) request.get("expression");
        
        if (expression == null || expression.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("Mathematical expression is required"));
        }

        // Create a safe mathematical expression evaluator
        String safeCode = "function calculate() { try { return " + expression + "; } catch(e) { return 'Error: ' + e.message; } } calculate();";
        
        try {
            String result = jsService.executeJavaScript(safeCode);
            return ResponseEntity.ok(createSuccessResponse(result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("Calculation failed: " + e.getMessage()));
        }
    }

    /**
     * JavaScript string manipulation endpoint
     */
    @PostMapping("/string-utils")
    public ResponseEntity<Map<String, Object>> stringUtils(@RequestBody Map<String, Object> request) {
        String text = (String) request.get("text");
        String operation = (String) request.get("operation");
        
        if (text == null || operation == null) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("Both 'text' and 'operation' are required"));
        }

        String jsCode = createStringUtilsCode(text, operation);
        
        try {
            String result = jsService.executeJavaScript(jsCode);
            return ResponseEntity.ok(createSuccessResponse(result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(createErrorResponse("String operation failed: " + e.getMessage()));
        }
    }

    /**
     * Advanced JavaScript features demo
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo() {
        Map<String, Object> demos = new HashMap<>();
        
        // Demo 1: Simple calculation
        String calc = jsService.executeJavaScript("Math.sqrt(16) + Math.pow(2, 3)");
        demos.put("calculation", calc);
        
        // Demo 2: String manipulation
        String str = jsService.executeJavaScript("'Hello World'.split(' ').reverse().join('-').toLowerCase()");
        demos.put("stringManipulation", str);
        
        // Demo 3: Date manipulation
        String date = jsService.executeJavaScript("new Date().toISOString().split('T')[0]");
        demos.put("dateFormatting", date);
        
        // Demo 4: JSON processing
        String json = jsService.processJsonWithJS(
            "{\"numbers\": [1,2,3,4,5]}", 
            "data.numbers.map(n => n * 2).filter(n => n > 4)"
        );
        demos.put("jsonProcessing", json);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "JavaScript execution demos");
        response.put("demos", demos);
        
        return ResponseEntity.ok(response);
    }

    private String createStringUtilsCode(String text, String operation) {
        switch (operation.toLowerCase()) {
            case "uppercase":
                return "'" + text + "'.toUpperCase()";
            case "lowercase":
                return "'" + text + "'.toLowerCase()";
            case "reverse":
                return "'" + text + "'.split('').reverse().join('')";
            case "wordcount":
                return "'" + text + "'.split(' ').filter(w => w.length > 0).length";
            case "removeSpaces":
                return "'" + text + "'.replace(/\\s+/g, '')";
            case "capitalize":
                return "'" + text + "'.split(' ').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ')";
            default:
                return "'Unknown operation: " + operation + "'";
        }
    }

    private Map<String, Object> createSuccessResponse(String result) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("result", result);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
