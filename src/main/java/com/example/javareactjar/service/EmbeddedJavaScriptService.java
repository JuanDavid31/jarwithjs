package com.example.javareactjar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmbeddedJavaScriptService {
    
    @Autowired
    private JavaScriptExecutionService jsExecutionService;
    
    private final Map<String, String> scriptCache = new ConcurrentHashMap<>();
    
    /**
     * Load embedded JavaScript file from resources
     */
    public String loadEmbeddedScript(String scriptName) {
        if (scriptCache.containsKey(scriptName)) {
            return scriptCache.get(scriptName);
        }
        
        try {
            ClassPathResource resource = new ClassPathResource("javascript/" + scriptName + ".js");
            if (!resource.exists()) {
                return null;
            }
            
            String script = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            scriptCache.put(scriptName, script);
            return script;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load embedded script: " + scriptName, e);
        }
    }
    
    /**
     * Execute business rules from embedded JavaScript
     */
    public Map<String, Object> executeBusinessRules(String functionName, Map<String, Object> parameters) {
        String script = loadEmbeddedScript("business-rules");
        if (script == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Business rules script not found");
            return error;
        }
        
        try {
            // Create execution context with the script and function call
            String executionCode = script + "\n\n" + 
                "var params = " + convertMapToJSON(parameters) + ";\n" +
                "var result = " + functionName + "(params);\n" +
                "JSON.stringify(result);";
            
            String resultJson = jsExecutionService.executeJavaScript(executionCode);
            
            // Parse JSON result back to Map
            return parseJsonToMap(resultJson);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to execute business rules: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * Process data using embedded data processing scripts
     */
    public Map<String, Object> processData(String functionName, Map<String, Object> data, Map<String, Object> options) {
        String script = loadEmbeddedScript("data-processing");
        if (script == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Data processing script not found");
            return error;
        }
        
        try {
            String executionCode = script + "\n\n" + 
                "var inputData = " + convertMapToJSON(data) + ";\n" +
                "var options = " + convertMapToJSON(options) + ";\n" +
                "var result = " + functionName + "(inputData, options);\n" +
                "JSON.stringify(result);";
            
            String resultJson = jsExecutionService.executeJavaScript(executionCode);
            return parseJsonToMap(resultJson);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to process data: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * Parse CSV data using embedded JavaScript
     */
    public Map<String, Object> parseCSV(String csvData, Map<String, Object> options) {
        Map<String, Object> data = new HashMap<>();
        data.put("csvText", csvData);
        return processData("parseCSV", data, options);
    }
    
    /**
     * Query JSON using JSONPath-like syntax
     */
    public Map<String, Object> queryJSON(Object jsonData, String path) {
        Map<String, Object> data = new HashMap<>();
        data.put("data", jsonData);
        data.put("path", path);
        return processData("queryJSON", data, new HashMap<>());
    }
    
    /**
     * Clean data using specified rules
     */
    public Map<String, Object> cleanData(Object rawData, Map<String, Object> rules) {
        Map<String, Object> data = new HashMap<>();
        data.put("data", rawData);
        data.put("rules", rules);
        return processData("cleanData", data, new HashMap<>());
    }
    
    /**
     * Advanced discount calculation
     */
    public Map<String, Object> calculateAdvancedDiscount(Map<String, Object> orderData) {
        return executeBusinessRules("calculateAdvancedDiscount", orderData);
    }
    
    /**
     * Complex form validation
     */
    public Map<String, Object> validateComplexForm(Map<String, Object> formData) {
        return executeBusinessRules("validateComplexForm", formData);
    }
    
    /**
     * Statistical analysis of data
     */
    public Map<String, Object> analyzeData(Object dataset) {
        Map<String, Object> data = new HashMap<>();
        data.put("dataset", dataset);
        return executeBusinessRules("analyzeData", data);
    }
    
    /**
     * Get list of available embedded scripts
     */
    public Map<String, Object> getAvailableScripts() {
        Map<String, Object> scripts = new HashMap<>();
        
        // Business Rules
        Map<String, String> businessRules = new HashMap<>();
        businessRules.put("calculateAdvancedDiscount", "Advanced discount calculation with multiple rules");
        businessRules.put("validateComplexForm", "Complex form validation with warnings and errors");
        businessRules.put("analyzeData", "Statistical analysis of numerical datasets");
        scripts.put("businessRules", businessRules);
        
        // Data Processing
        Map<String, String> dataProcessing = new HashMap<>();
        dataProcessing.put("parseCSV", "Parse CSV data with configurable options");
        dataProcessing.put("queryJSON", "Query JSON data using path syntax");
        dataProcessing.put("aggregateData", "Group and aggregate data with multiple functions");
        dataProcessing.put("processTimeSeries", "Time series data analysis and aggregation");
        dataProcessing.put("cleanData", "Data cleaning and transformation with rules");
        scripts.put("dataProcessing", dataProcessing);
        
        return scripts;
    }
    
    /**
     * Execute custom JavaScript with embedded utility functions
     */
    public Map<String, Object> executeCustomScript(String jsCode, Map<String, Object> context) {
        // Load utility scripts
        String businessRules = loadEmbeddedScript("business-rules");
        String dataProcessing = loadEmbeddedScript("data-processing");
        
        // Combine all scripts
        StringBuilder fullScript = new StringBuilder();
        if (businessRules != null) {
            fullScript.append(businessRules).append("\n\n");
        }
        if (dataProcessing != null) {
            fullScript.append(dataProcessing).append("\n\n");
        }
        
        // Add context variables
        if (context != null) {
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                fullScript.append("var ").append(entry.getKey()).append(" = ")
                    .append(convertMapToJSON(Map.of("value", entry.getValue()))).append(".value;\n");
            }
        }
        
        // Add custom code
        fullScript.append("\n\n").append(jsCode);
        
        try {
            String result = jsExecutionService.executeJavaScript(fullScript.toString());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);
            return response;
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }
    
    /**
     * Benchmark JavaScript execution performance
     */
    public Map<String, Object> benchmarkExecution(String scriptName, String functionName, Map<String, Object> parameters, int iterations) {
        long totalTime = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        int successCount = 0;
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            try {
                if ("business-rules".equals(scriptName)) {
                    executeBusinessRules(functionName, parameters);
                } else if ("data-processing".equals(scriptName)) {
                    processData(functionName, parameters, new HashMap<>());
                }
                successCount++;
            } catch (Exception e) {
                // Continue with benchmark even if some executions fail
            }
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            
            totalTime += duration;
            minTime = Math.min(minTime, duration);
            maxTime = Math.max(maxTime, duration);
        }
        
        Map<String, Object> results = new HashMap<>();
        results.put("iterations", iterations);
        results.put("successCount", successCount);
        results.put("successRate", (double) successCount / iterations * 100);
        results.put("totalTimeMs", totalTime / 1_000_000.0);
        results.put("averageTimeMs", totalTime / 1_000_000.0 / iterations);
        results.put("minTimeMs", minTime / 1_000_000.0);
        results.put("maxTimeMs", maxTime / 1_000_000.0);
        results.put("executionsPerSecond", iterations / (totalTime / 1_000_000_000.0));
        
        return results;
    }
    
    // Utility methods
    private String convertMapToJSON(Map<String, Object> map) {
        try {
            // Simple JSON conversion - in production, use Jackson ObjectMapper
            StringBuilder json = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!first) json.append(",");
                json.append("\"").append(entry.getKey()).append("\":");
                
                Object value = entry.getValue();
                if (value instanceof String) {
                    json.append("\"").append(value.toString().replace("\"", "\\\"")).append("\"");
                } else if (value instanceof Number || value instanceof Boolean) {
                    json.append(value);
                } else if (value == null) {
                    json.append("null");
                } else {
                    json.append("\"").append(value.toString().replace("\"", "\\\"")).append("\"");
                }
                first = false;
            }
            json.append("}");
            return json.toString();
        } catch (Exception e) {
            return "{}";
        }
    }
    
    private Map<String, Object> parseJsonToMap(String json) {
        try {
            // Simple JSON parsing - in production, use Jackson ObjectMapper
            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = json.substring(1, json.length() - 1);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("result", json);
            return result;
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to parse JSON result");
            return error;
        }
    }
}
