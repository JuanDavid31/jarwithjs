// Data Processing JavaScript Library - Embedded in JAR
// Advanced data manipulation and processing functions

/**
 * CSV Parser and Processor
 */
function parseCSV(csvText, options = {}) {
    const delimiter = options.delimiter || ',';
    const hasHeader = options.hasHeader !== false;
    const skipEmpty = options.skipEmpty !== false;
    
    const lines = csvText.split('\n').filter(line => !skipEmpty || line.trim());
    if (lines.length === 0) return { headers: [], data: [], error: 'No data found' };
    
    const parseRow = (row) => {
        const result = [];
        let current = '';
        let inQuotes = false;
        
        for (let i = 0; i < row.length; i++) {
            const char = row[i];
            if (char === '"') {
                inQuotes = !inQuotes;
            } else if (char === delimiter && !inQuotes) {
                result.push(current.trim());
                current = '';
            } else {
                current += char;
            }
        }
        result.push(current.trim());
        return result;
    };
    
    const headers = hasHeader ? parseRow(lines[0]) : null;
    const dataStart = hasHeader ? 1 : 0;
    const data = lines.slice(dataStart).map(parseRow);
    
    return {
        headers: headers,
        data: data,
        rowCount: data.length,
        columnCount: headers ? headers.length : (data.length > 0 ? data[0].length : 0)
    };
}

/**
 * JSON Path Query Engine
 */
function queryJSON(data, path) {
    const pathParts = path.replace(/\[(\d+)\]/g, '.$1').split('.').filter(p => p);
    let current = data;
    
    try {
        for (const part of pathParts) {
            if (current === null || current === undefined) {
                return { value: null, found: false };
            }
            
            if (Array.isArray(current) && /^\d+$/.test(part)) {
                const index = parseInt(part);
                current = current[index];
            } else if (typeof current === 'object') {
                current = current[part];
            } else {
                return { value: null, found: false };
            }
        }
        
        return { value: current, found: true, path: path };
    } catch (error) {
        return { value: null, found: false, error: error.message };
    }
}

/**
 * Data aggregation functions
 */
function aggregateData(data, groupBy, aggregations) {
    if (!Array.isArray(data)) return { error: 'Data must be an array' };
    
    const groups = {};
    
    // Group the data
    data.forEach(item => {
        const key = typeof groupBy === 'function' ? groupBy(item) : item[groupBy];
        if (!groups[key]) groups[key] = [];
        groups[key].push(item);
    });
    
    // Apply aggregations
    const result = {};
    Object.keys(groups).forEach(key => {
        const group = groups[key];
        result[key] = { count: group.length };
        
        Object.keys(aggregations).forEach(field => {
            const agg = aggregations[field];
            const values = group.map(item => item[field]).filter(v => typeof v === 'number' && !isNaN(v));
            
            switch (agg.toLowerCase()) {
                case 'sum':
                    result[key][`${field}_sum`] = values.reduce((a, b) => a + b, 0);
                    break;
                case 'avg':
                case 'average':
                    result[key][`${field}_avg`] = values.length > 0 ? values.reduce((a, b) => a + b, 0) / values.length : 0;
                    break;
                case 'min':
                    result[key][`${field}_min`] = values.length > 0 ? Math.min(...values) : null;
                    break;
                case 'max':
                    result[key][`${field}_max`] = values.length > 0 ? Math.max(...values) : null;
                    break;
                case 'count':
                    result[key][`${field}_count`] = values.length;
                    break;
            }
        });
    });
    
    return result;
}

/**
 * Time series data processing
 */
function processTimeSeries(data, options = {}) {
    const timeField = options.timeField || 'timestamp';
    const valueField = options.valueField || 'value';
    const interval = options.interval || 'hour'; // hour, day, week, month
    
    if (!Array.isArray(data)) return { error: 'Data must be an array' };
    
    // Parse and sort by time
    const parsed = data.map(item => ({
        ...item,
        time: new Date(item[timeField]),
        value: parseFloat(item[valueField]) || 0
    })).filter(item => !isNaN(item.time.getTime()))
      .sort((a, b) => a.time - b.time);
    
    if (parsed.length === 0) return { error: 'No valid time series data found' };
    
    // Group by interval
    const getIntervalKey = (date) => {
        switch (interval) {
            case 'minute':
                return `${date.getFullYear()}-${date.getMonth()}-${date.getDate()}-${date.getHours()}-${date.getMinutes()}`;
            case 'hour':
                return `${date.getFullYear()}-${date.getMonth()}-${date.getDate()}-${date.getHours()}`;
            case 'day':
                return `${date.getFullYear()}-${date.getMonth()}-${date.getDate()}`;
            case 'week':
                const weekStart = new Date(date);
                weekStart.setDate(date.getDate() - date.getDay());
                return `${weekStart.getFullYear()}-W${Math.ceil(weekStart.getDate() / 7)}`;
            case 'month':
                return `${date.getFullYear()}-${date.getMonth()}`;
            default:
                return date.toISOString().split('T')[0];
        }
    };
    
    const grouped = {};
    parsed.forEach(item => {
        const key = getIntervalKey(item.time);
        if (!grouped[key]) grouped[key] = [];
        grouped[key].push(item.value);
    });
    
    // Calculate statistics for each interval
    const result = Object.keys(grouped).map(key => {
        const values = grouped[key];
        const sum = values.reduce((a, b) => a + b, 0);
        const avg = sum / values.length;
        const min = Math.min(...values);
        const max = Math.max(...values);
        
        return {
            interval: key,
            count: values.length,
            sum: Math.round(sum * 100) / 100,
            average: Math.round(avg * 100) / 100,
            min: min,
            max: max,
            range: max - min
        };
    });
    
    return {
        intervals: result,
        totalDataPoints: parsed.length,
        timeRange: {
            start: parsed[0].time.toISOString(),
            end: parsed[parsed.length - 1].time.toISOString()
        },
        summary: {
            totalSum: result.reduce((acc, item) => acc + item.sum, 0),
            overallAverage: result.reduce((acc, item) => acc + item.average, 0) / result.length,
            globalMin: Math.min(...result.map(item => item.min)),
            globalMax: Math.max(...result.map(item => item.max))
        }
    };
}

/**
 * Data cleaning and transformation
 */
function cleanData(data, rules = {}) {
    if (!Array.isArray(data)) return { error: 'Data must be an array' };
    
    const cleaned = data.map((item, index) => {
        const cleanedItem = { ...item };
        const issues = [];
        
        // Apply cleaning rules
        Object.keys(rules).forEach(field => {
            const rule = rules[field];
            let value = cleanedItem[field];
            
            // Handle missing values
            if (value === null || value === undefined || value === '') {
                if (rule.required) {
                    issues.push(`Missing required field: ${field}`);
                } else if (rule.default !== undefined) {
                    cleanedItem[field] = rule.default;
                }
                return;
            }
            
            // Type conversion
            if (rule.type) {
                switch (rule.type) {
                    case 'number':
                        const num = parseFloat(value);
                        if (isNaN(num)) {
                            issues.push(`Invalid number in field: ${field}`);
                        } else {
                            cleanedItem[field] = num;
                        }
                        break;
                    case 'integer':
                        const int = parseInt(value);
                        if (isNaN(int)) {
                            issues.push(`Invalid integer in field: ${field}`);
                        } else {
                            cleanedItem[field] = int;
                        }
                        break;
                    case 'string':
                        cleanedItem[field] = String(value);
                        break;
                    case 'boolean':
                        cleanedItem[field] = Boolean(value);
                        break;
                    case 'date':
                        const date = new Date(value);
                        if (isNaN(date.getTime())) {
                            issues.push(`Invalid date in field: ${field}`);
                        } else {
                            cleanedItem[field] = date.toISOString();
                        }
                        break;
                }
            }
            
            // Range validation
            if (rule.min !== undefined && cleanedItem[field] < rule.min) {
                issues.push(`Value below minimum (${rule.min}) in field: ${field}`);
            }
            if (rule.max !== undefined && cleanedItem[field] > rule.max) {
                issues.push(`Value above maximum (${rule.max}) in field: ${field}`);
            }
            
            // Pattern validation
            if (rule.pattern && typeof cleanedItem[field] === 'string') {
                const regex = new RegExp(rule.pattern);
                if (!regex.test(cleanedItem[field])) {
                    issues.push(`Pattern mismatch in field: ${field}`);
                }
            }
            
            // String transformations
            if (rule.trim && typeof cleanedItem[field] === 'string') {
                cleanedItem[field] = cleanedItem[field].trim();
            }
            if (rule.lowercase && typeof cleanedItem[field] === 'string') {
                cleanedItem[field] = cleanedItem[field].toLowerCase();
            }
            if (rule.uppercase && typeof cleanedItem[field] === 'string') {
                cleanedItem[field] = cleanedItem[field].toUpperCase();
            }
        });
        
        return {
            originalIndex: index,
            data: cleanedItem,
            issues: issues,
            isValid: issues.length === 0
        };
    });
    
    const validData = cleaned.filter(item => item.isValid).map(item => item.data);
    const invalidData = cleaned.filter(item => !item.isValid);
    
    return {
        originalCount: data.length,
        validCount: validData.length,
        invalidCount: invalidData.length,
        cleanedData: validData,
        rejectedData: invalidData,
        cleaningRate: (validData.length / data.length) * 100
    };
}

// Utility functions
const utils = {
    deepClone: (obj) => JSON.parse(JSON.stringify(obj)),
    
    flatten: (arr) => arr.reduce((flat, item) => 
        flat.concat(Array.isArray(item) ? utils.flatten(item) : item), []),
    
    unique: (arr) => [...new Set(arr)],
    
    chunk: (arr, size) => {
        const chunks = [];
        for (let i = 0; i < arr.length; i += size) {
            chunks.push(arr.slice(i, i + size));
        }
        return chunks;
    },
    
    intersection: (arr1, arr2) => arr1.filter(x => arr2.includes(x)),
    
    difference: (arr1, arr2) => arr1.filter(x => !arr2.includes(x))
};

// Export for Java usage
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        parseCSV,
        queryJSON,
        aggregateData,
        processTimeSeries,
        cleanData,
        utils
    };
}
