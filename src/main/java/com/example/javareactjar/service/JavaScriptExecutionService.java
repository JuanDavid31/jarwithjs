package com.example.javareactjar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JavaScriptExecutionService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Context> contexts = new ConcurrentHashMap<>();
    
    /**
     * Execute JavaScript code and return the result as a string
     */
    public String executeJavaScript(String jsCode) {
        try (Context context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build()) {
            
            // Add Java objects to JS context
            context.getBindings("js").putMember("javaUtils", new JavaUtils());
            
            Value result = context.eval("js", jsCode);
            
            if (result.isString()) {
                return result.asString();
            } else if (result.isNumber()) {
                return String.valueOf(result.asDouble());
            } else if (result.isBoolean()) {
                return String.valueOf(result.asBoolean());
            } else {
                return result.toString();
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * Execute JavaScript with input parameters
     */
    public String executeJavaScriptWithParams(String jsCode, Map<String, Object> params) {
        try (Context context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build()) {
            
            // Add parameters to JS context
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                context.getBindings("js").putMember(entry.getKey(), entry.getValue());
            }
            
            // Add Java utilities
            context.getBindings("js").putMember("javaUtils", new JavaUtils());
            
            Value result = context.eval("js", jsCode);
            return convertValueToString(result);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * Execute JavaScript functions stored as strings
     */
    public String callJavaScriptFunction(String functionName, String functionBody, Object... args) {
        try (Context context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build()) {
            
            // Define the function
            context.eval("js", "function " + functionName + "() { " + functionBody + " }");
            
            // Get the function
            Value function = context.getBindings("js").getMember(functionName);
            
            // Call the function with arguments
            Value result = function.execute(args);
            return convertValueToString(result);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * Process JSON with JavaScript
     */
    public String processJsonWithJS(String jsonData, String jsCode) {
        try (Context context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build()) {
            
            // Parse JSON and make it available
            context.eval("js", "var data = " + jsonData + ";");
            
            // Execute the processing code
            Value result = context.eval("js", jsCode);
            return convertValueToString(result);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * Run JavaScript-based business logic
     */
    public Map<String, Object> executeBusinessLogic(String scriptName, Map<String, Object> inputData) {
        try (Context context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build()) {
            
            // Load predefined business logic scripts
            String script = getBusinessLogicScript(scriptName);
            
            // Make input data available to JavaScript
            context.getBindings("js").putMember("inputData", inputData);
            context.getBindings("js").putMember("result", new HashMap<>());
            
            // Execute the script
            context.eval("js", script);
            
            // Get the result
            Value resultValue = context.getBindings("js").getMember("result");
            
            // Convert to Java Map
            Map<String, Object> result = new HashMap<>();
            if (resultValue.hasMembers()) {
                for (String key : resultValue.getMemberKeys()) {
                    Value value = resultValue.getMember(key);
                    if (value.isString()) {
                        result.put(key, value.asString());
                    } else if (value.isNumber()) {
                        result.put(key, value.asDouble());
                    } else if (value.isBoolean()) {
                        result.put(key, value.asBoolean());
                    } else {
                        result.put(key, value.toString());
                    }
                }
            }
            
            return result;
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", e.getMessage());
            return errorResult;
        }
    }
    
    private String convertValueToString(Value value) {
        if (value.isString()) {
            return value.asString();
        } else if (value.isNumber()) {
            return String.valueOf(value.asDouble());
        } else if (value.isBoolean()) {
            return String.valueOf(value.asBoolean());
        } else if (value.hasMembers()) {
            // Convert object to JSON string
            try {
                Map<String, Object> map = new HashMap<>();
                for (String key : value.getMemberKeys()) {
                    Value memberValue = value.getMember(key);
                    if (memberValue.isString()) {
                        map.put(key, memberValue.asString());
                    } else if (memberValue.isNumber()) {
                        map.put(key, memberValue.asDouble());
                    } else if (memberValue.isBoolean()) {
                        map.put(key, memberValue.asBoolean());
                    } else {
                        map.put(key, memberValue.toString());
                    }
                }
                return objectMapper.writeValueAsString(map);
            } catch (Exception e) {
                return value.toString();
            }
        } else {
            return value.toString();
        }
    }
    
    private String getBusinessLogicScript(String scriptName) {
        // In a real application, you might load these from a database or file system
        switch (scriptName) {
            case "calculateDiscount":
                return "var price = inputData.price; " +
                       "var quantity = inputData.quantity; " +
                       "var customerType = inputData.customerType; " +
                       "var discount = 0; " +
                       "if (customerType === 'premium') { " +
                           "discount = 0.15; " +
                       "} else if (quantity > 10) { " +
                           "discount = 0.10; " +
                       "} else if (quantity > 5) { " +
                           "discount = 0.05; " +
                       "} " +
                       "var total = price * quantity; " +
                       "var discountAmount = total * discount; " +
                       "var finalPrice = total - discountAmount; " +
                       "result.originalTotal = total; " +
                       "result.discount = discount; " +
                       "result.discountAmount = discountAmount; " +
                       "result.finalPrice = finalPrice; " +
                       "result.message = 'Discount applied: ' + (discount * 100) + '%'; ";
            case "validateUserData":
                return "var email = inputData.email; " +
                       "var age = inputData.age; " +
                       "var name = inputData.name; " +
                       "var errors = []; " +
                       "if (!email || !email.includes('@')) { " +
                           "errors.push('Invalid email address'); " +
                       "} " +
                       "if (age < 18) { " +
                           "errors.push('Must be 18 or older'); " +
                       "} " +
                       "if (!name || name.length < 2) { " +
                           "errors.push('Name must be at least 2 characters'); " +
                       "} " +
                       "result.isValid = errors.length === 0; " +
                       "result.errors = errors; " +
                       "result.message = errors.length === 0 ? 'Validation passed' : 'Validation failed'; ";
            default:
                return "result.error = 'Unknown script: " + scriptName + "';";
        }
    }
    
    /**
     * Java utilities that can be called from JavaScript
     */
    public static class JavaUtils {
        public String formatCurrency(double amount) {
            return String.format("$%.2f", amount);
        }
        
        public long getCurrentTimestamp() {
            return System.currentTimeMillis();
        }
        
        public String generateUUID() {
            return java.util.UUID.randomUUID().toString();
        }
        
        public double calculateTax(double amount, double rate) {
            return amount * rate;
        }
    }
}
