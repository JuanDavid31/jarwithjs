// Business Rules JavaScript - Embedded in JAR
// These scripts can be loaded and executed by the JavaScriptExecutionService

/**
 * Advanced discount calculation with multiple rules
 */
function calculateAdvancedDiscount(order) {
    let discount = 0;
    let reasons = [];
    
    // Volume discount
    if (order.quantity >= 100) {
        discount = Math.max(discount, 0.25);
        reasons.push('Bulk order discount (25%)');
    } else if (order.quantity >= 50) {
        discount = Math.max(discount, 0.15);
        reasons.push('Volume discount (15%)');
    } else if (order.quantity >= 10) {
        discount = Math.max(discount, 0.08);
        reasons.push('Multi-item discount (8%)');
    }
    
    // Customer tier discount
    switch (order.customerTier) {
        case 'platinum':
            discount = Math.max(discount, 0.20);
            reasons.push('Platinum member discount (20%)');
            break;
        case 'gold':
            discount = Math.max(discount, 0.15);
            reasons.push('Gold member discount (15%)');
            break;
        case 'silver':
            discount = Math.max(discount, 0.10);
            reasons.push('Silver member discount (10%)');
            break;
    }
    
    // Seasonal promotions
    const now = new Date();
    const month = now.getMonth();
    if (month === 11) { // December
        discount = Math.max(discount, 0.12);
        reasons.push('Holiday season discount (12%)');
    } else if (month === 6 || month === 7) { // July-August
        discount = Math.max(discount, 0.08);
        reasons.push('Summer sale discount (8%)');
    }
    
    // Category-specific discounts
    if (order.category === 'electronics' && order.value > 1000) {
        discount = Math.max(discount, 0.10);
        reasons.push('Electronics discount (10%)');
    } else if (order.category === 'books') {
        discount = Math.max(discount, 0.05);
        reasons.push('Book lover discount (5%)');
    }
    
    const originalTotal = order.price * order.quantity;
    const discountAmount = originalTotal * discount;
    const finalPrice = originalTotal - discountAmount;
    
    return {
        originalTotal: Math.round(originalTotal * 100) / 100,
        discountPercent: Math.round(discount * 100),
        discountAmount: Math.round(discountAmount * 100) / 100,
        finalPrice: Math.round(finalPrice * 100) / 100,
        reasons: reasons,
        savings: Math.round(discountAmount * 100) / 100
    };
}

/**
 * Complex validation with multiple rules
 */
function validateComplexForm(data) {
    const errors = [];
    const warnings = [];
    
    // Email validation
    if (!data.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) {
        errors.push('Valid email address is required');
    }
    
    // Password strength
    if (!data.password) {
        errors.push('Password is required');
    } else {
        if (data.password.length < 8) {
            errors.push('Password must be at least 8 characters');
        }
        if (!/(?=.*[a-z])/.test(data.password)) {
            warnings.push('Password should contain lowercase letters');
        }
        if (!/(?=.*[A-Z])/.test(data.password)) {
            warnings.push('Password should contain uppercase letters');
        }
        if (!/(?=.*\d)/.test(data.password)) {
            warnings.push('Password should contain numbers');
        }
        if (!/(?=.*[@$!%*?&])/.test(data.password)) {
            warnings.push('Password should contain special characters');
        }
    }
    
    // Age validation
    if (data.age < 13) {
        errors.push('Must be at least 13 years old');
    } else if (data.age < 18) {
        warnings.push('Parental consent may be required');
    }
    
    // Phone validation
    if (data.phone && !/^\+?[\d\s\-\(\)]{10,}$/.test(data.phone)) {
        errors.push('Valid phone number is required');
    }
    
    // Address validation
    if (!data.address || data.address.trim().length < 5) {
        errors.push('Complete address is required');
    }
    
    const score = calculateValidationScore(data, errors, warnings);
    
    return {
        isValid: errors.length === 0,
        errors: errors,
        warnings: warnings,
        score: score,
        recommendation: getRecommendation(score)
    };
}

function calculateValidationScore(data, errors, warnings) {
    let score = 100;
    score -= errors.length * 20;
    score -= warnings.length * 5;
    
    // Bonus points for completeness
    if (data.email && data.password && data.name && data.age) score += 10;
    if (data.phone) score += 5;
    if (data.address) score += 5;
    
    return Math.max(0, Math.min(100, score));
}

function getRecommendation(score) {
    if (score >= 90) return 'Excellent! All criteria met.';
    if (score >= 75) return 'Good! Minor improvements suggested.';
    if (score >= 60) return 'Acceptable with some concerns.';
    if (score >= 40) return 'Needs improvement in several areas.';
    return 'Significant issues need to be addressed.';
}

/**
 * Data transformation and analysis
 */
function analyzeData(dataset) {
    if (!Array.isArray(dataset) || dataset.length === 0) {
        return { error: 'Invalid or empty dataset' };
    }
    
    const numbers = dataset.filter(item => typeof item === 'number' && !isNaN(item));
    
    if (numbers.length === 0) {
        return { error: 'No valid numbers found in dataset' };
    }
    
    const sorted = numbers.slice().sort((a, b) => a - b);
    const sum = numbers.reduce((acc, val) => acc + val, 0);
    const mean = sum / numbers.length;
    
    // Calculate median
    const median = sorted.length % 2 === 0
        ? (sorted[sorted.length / 2 - 1] + sorted[sorted.length / 2]) / 2
        : sorted[Math.floor(sorted.length / 2)];
    
    // Calculate mode
    const frequency = {};
    numbers.forEach(num => frequency[num] = (frequency[num] || 0) + 1);
    const maxFreq = Math.max(...Object.values(frequency));
    const modes = Object.keys(frequency).filter(key => frequency[key] === maxFreq).map(Number);
    
    // Calculate standard deviation
    const variance = numbers.reduce((acc, val) => acc + Math.pow(val - mean, 2), 0) / numbers.length;
    const standardDeviation = Math.sqrt(variance);
    
    // Quartiles
    const q1Index = Math.floor(sorted.length * 0.25);
    const q3Index = Math.floor(sorted.length * 0.75);
    const q1 = sorted[q1Index];
    const q3 = sorted[q3Index];
    const iqr = q3 - q1;
    
    // Outliers (using IQR method)
    const lowerBound = q1 - 1.5 * iqr;
    const upperBound = q3 + 1.5 * iqr;
    const outliers = numbers.filter(num => num < lowerBound || num > upperBound);
    
    return {
        count: numbers.length,
        sum: Math.round(sum * 100) / 100,
        mean: Math.round(mean * 100) / 100,
        median: median,
        mode: modes.length === numbers.length ? 'No mode' : modes,
        min: sorted[0],
        max: sorted[sorted.length - 1],
        range: sorted[sorted.length - 1] - sorted[0],
        standardDeviation: Math.round(standardDeviation * 100) / 100,
        variance: Math.round(variance * 100) / 100,
        q1: q1,
        q3: q3,
        iqr: iqr,
        outliers: outliers,
        percentiles: {
            '10th': sorted[Math.floor(sorted.length * 0.1)],
            '25th': q1,
            '50th': median,
            '75th': q3,
            '90th': sorted[Math.floor(sorted.length * 0.9)]
        }
    };
}

// Export functions for use in Java
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        calculateAdvancedDiscount,
        validateComplexForm,
        analyzeData
    };
}
