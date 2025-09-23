// Mathematical Utilities JavaScript - Embedded in JAR
// Advanced mathematical functions and calculations

/**
 * Matrix operations
 */
const Matrix = {
    // Create a matrix filled with zeros
    zeros: (rows, cols) => {
        return Array(rows).fill().map(() => Array(cols).fill(0));
    },
    
    // Create identity matrix
    identity: (size) => {
        const matrix = Matrix.zeros(size, size);
        for (let i = 0; i < size; i++) {
            matrix[i][i] = 1;
        }
        return matrix;
    },
    
    // Matrix addition
    add: (a, b) => {
        if (a.length !== b.length || a[0].length !== b[0].length) {
            throw new Error('Matrices must have the same dimensions');
        }
        return a.map((row, i) => row.map((val, j) => val + b[i][j]));
    },
    
    // Matrix subtraction
    subtract: (a, b) => {
        if (a.length !== b.length || a[0].length !== b[0].length) {
            throw new Error('Matrices must have the same dimensions');
        }
        return a.map((row, i) => row.map((val, j) => val - b[i][j]));
    },
    
    // Matrix multiplication
    multiply: (a, b) => {
        if (a[0].length !== b.length) {
            throw new Error('Invalid dimensions for matrix multiplication');
        }
        
        const rows = a.length;
        const cols = b[0].length;
        const result = Matrix.zeros(rows, cols);
        
        for (let i = 0; i < rows; i++) {
            for (let j = 0; j < cols; j++) {
                for (let k = 0; k < a[0].length; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    },
    
    // Matrix transpose
    transpose: (matrix) => {
        return matrix[0].map((_, i) => matrix.map(row => row[i]));
    },
    
    // Matrix determinant (2x2 and 3x3)
    determinant: (matrix) => {
        const size = matrix.length;
        if (size !== matrix[0].length) {
            throw new Error('Matrix must be square');
        }
        
        if (size === 1) return matrix[0][0];
        if (size === 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        if (size === 3) {
            return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) -
                   matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) +
                   matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        }
        
        throw new Error('Determinant calculation only supports up to 3x3 matrices');
    }
};

/**
 * Advanced statistical functions
 */
const Statistics = {
    // Correlation coefficient
    correlation: (x, y) => {
        if (x.length !== y.length) throw new Error('Arrays must have same length');
        
        const n = x.length;
        const sumX = x.reduce((a, b) => a + b, 0);
        const sumY = y.reduce((a, b) => a + b, 0);
        const sumXY = x.reduce((acc, xi, i) => acc + xi * y[i], 0);
        const sumX2 = x.reduce((acc, xi) => acc + xi * xi, 0);
        const sumY2 = y.reduce((acc, yi) => acc + yi * yi, 0);
        
        const numerator = n * sumXY - sumX * sumY;
        const denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
        
        return denominator === 0 ? 0 : numerator / denominator;
    },
    
    // Linear regression
    linearRegression: (x, y) => {
        if (x.length !== y.length) throw new Error('Arrays must have same length');
        
        const n = x.length;
        const sumX = x.reduce((a, b) => a + b, 0);
        const sumY = y.reduce((a, b) => a + b, 0);
        const sumXY = x.reduce((acc, xi, i) => acc + xi * y[i], 0);
        const sumX2 = x.reduce((acc, xi) => acc + xi * xi, 0);
        
        const slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        const intercept = (sumY - slope * sumX) / n;
        
        // Calculate R-squared
        const meanY = sumY / n;
        const totalSS = y.reduce((acc, yi) => acc + (yi - meanY) ** 2, 0);
        const residualSS = x.reduce((acc, xi, i) => {
            const predicted = slope * xi + intercept;
            return acc + (y[i] - predicted) ** 2;
        }, 0);
        const rSquared = 1 - (residualSS / totalSS);
        
        return {
            slope: Math.round(slope * 10000) / 10000,
            intercept: Math.round(intercept * 10000) / 10000,
            rSquared: Math.round(rSquared * 10000) / 10000,
            equation: `y = ${slope.toFixed(4)}x + ${intercept.toFixed(4)}`
        };
    },
    
    // Hypothesis testing (t-test)
    tTest: (sample1, sample2) => {
        const mean1 = sample1.reduce((a, b) => a + b, 0) / sample1.length;
        const mean2 = sample2.reduce((a, b) => a + b, 0) / sample2.length;
        
        const var1 = sample1.reduce((acc, x) => acc + (x - mean1) ** 2, 0) / (sample1.length - 1);
        const var2 = sample2.reduce((acc, x) => acc + (x - mean2) ** 2, 0) / (sample2.length - 1);
        
        const pooledVar = ((sample1.length - 1) * var1 + (sample2.length - 1) * var2) / 
                         (sample1.length + sample2.length - 2);
        
        const standardError = Math.sqrt(pooledVar * (1/sample1.length + 1/sample2.length));
        const tStatistic = (mean1 - mean2) / standardError;
        const degreesOfFreedom = sample1.length + sample2.length - 2;
        
        return {
            mean1: Math.round(mean1 * 10000) / 10000,
            mean2: Math.round(mean2 * 10000) / 10000,
            tStatistic: Math.round(tStatistic * 10000) / 10000,
            degreesOfFreedom: degreesOfFreedom,
            standardError: Math.round(standardError * 10000) / 10000
        };
    }
};

/**
 * Financial calculations
 */
const Finance = {
    // Present Value
    presentValue: (futureValue, rate, periods) => {
        return futureValue / Math.pow(1 + rate, periods);
    },
    
    // Future Value
    futureValue: (presentValue, rate, periods) => {
        return presentValue * Math.pow(1 + rate, periods);
    },
    
    // Net Present Value
    netPresentValue: (rate, cashFlows) => {
        return cashFlows.reduce((npv, cashFlow, period) => {
            return npv + cashFlow / Math.pow(1 + rate, period);
        }, 0);
    },
    
    // Internal Rate of Return (approximation using bisection method)
    internalRateOfReturn: (cashFlows, precision = 0.0001) => {
        let rate = 0.1; // Initial guess
        let low = 0;
        let high = 1;
        
        for (let i = 0; i < 1000; i++) { // Max iterations
            const npv = Finance.netPresentValue(rate, cashFlows);
            
            if (Math.abs(npv) < precision) {
                return Math.round(rate * 10000) / 10000;
            }
            
            if (npv > 0) {
                low = rate;
                rate = (rate + high) / 2;
            } else {
                high = rate;
                rate = (low + rate) / 2;
            }
        }
        
        return Math.round(rate * 10000) / 10000;
    },
    
    // Loan payment calculation
    loanPayment: (principal, rate, periods) => {
        if (rate === 0) return principal / periods;
        const monthlyRate = rate / 12;
        const payment = principal * (monthlyRate * Math.pow(1 + monthlyRate, periods)) / 
                        (Math.pow(1 + monthlyRate, periods) - 1);
        return Math.round(payment * 100) / 100;
    },
    
    // Compound interest
    compoundInterest: (principal, rate, compoundingPeriods, time) => {
        const amount = principal * Math.pow(1 + rate/compoundingPeriods, compoundingPeriods * time);
        const interest = amount - principal;
        return {
            finalAmount: Math.round(amount * 100) / 100,
            interestEarned: Math.round(interest * 100) / 100,
            effectiveRate: Math.round((Math.pow(1 + rate/compoundingPeriods, compoundingPeriods) - 1) * 10000) / 10000
        };
    }
};

/**
 * Numerical methods
 */
const Numerical = {
    // Newton-Raphson method for finding roots
    newtonRaphson: (func, derivative, initialGuess, tolerance = 0.0001, maxIterations = 100) => {
        let x = initialGuess;
        
        for (let i = 0; i < maxIterations; i++) {
            const fx = func(x);
            const fpx = derivative(x);
            
            if (Math.abs(fpx) < tolerance) {
                throw new Error('Derivative too small, cannot continue');
            }
            
            const newX = x - fx / fpx;
            
            if (Math.abs(newX - x) < tolerance) {
                return {
                    root: Math.round(newX * 100000) / 100000,
                    iterations: i + 1,
                    converged: true
                };
            }
            
            x = newX;
        }
        
        return {
            root: Math.round(x * 100000) / 100000,
            iterations: maxIterations,
            converged: false
        };
    },
    
    // Numerical integration using Simpson's rule
    integrate: (func, a, b, n = 1000) => {
        if (n % 2 !== 0) n++; // Ensure n is even
        
        const h = (b - a) / n;
        let sum = func(a) + func(b);
        
        for (let i = 1; i < n; i++) {
            const x = a + i * h;
            sum += (i % 2 === 0 ? 2 : 4) * func(x);
        }
        
        return (h / 3) * sum;
    },
    
    // Solve system of linear equations using Gaussian elimination
    gaussianElimination: (matrix, constants) => {
        const n = matrix.length;
        const augmented = matrix.map((row, i) => [...row, constants[i]]);
        
        // Forward elimination
        for (let i = 0; i < n; i++) {
            // Find pivot
            let maxRow = i;
            for (let k = i + 1; k < n; k++) {
                if (Math.abs(augmented[k][i]) > Math.abs(augmented[maxRow][i])) {
                    maxRow = k;
                }
            }
            
            // Swap rows
            [augmented[i], augmented[maxRow]] = [augmented[maxRow], augmented[i]];
            
            // Make all rows below this one 0 in current column
            for (let k = i + 1; k < n; k++) {
                const factor = augmented[k][i] / augmented[i][i];
                for (let j = i; j <= n; j++) {
                    augmented[k][j] -= factor * augmented[i][j];
                }
            }
        }
        
        // Back substitution
        const solution = new Array(n);
        for (let i = n - 1; i >= 0; i--) {
            solution[i] = augmented[i][n];
            for (let j = i + 1; j < n; j++) {
                solution[i] -= augmented[i][j] * solution[j];
            }
            solution[i] /= augmented[i][i];
        }
        
        return solution.map(x => Math.round(x * 100000) / 100000);
    }
};

/**
 * Utility functions for mathematical operations
 */
const MathUtils = {
    // Greatest Common Divisor
    gcd: (a, b) => b === 0 ? a : MathUtils.gcd(b, a % b),
    
    // Least Common Multiple
    lcm: (a, b) => Math.abs(a * b) / MathUtils.gcd(a, b),
    
    // Check if number is prime
    isPrime: (n) => {
        if (n < 2) return false;
        if (n === 2) return true;
        if (n % 2 === 0) return false;
        
        for (let i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i === 0) return false;
        }
        return true;
    },
    
    // Generate Fibonacci sequence
    fibonacci: (n) => {
        if (n <= 0) return [];
        if (n === 1) return [0];
        if (n === 2) return [0, 1];
        
        const fib = [0, 1];
        for (let i = 2; i < n; i++) {
            fib[i] = fib[i-1] + fib[i-2];
        }
        return fib;
    },
    
    // Factorial
    factorial: (n) => n <= 1 ? 1 : n * MathUtils.factorial(n - 1),
    
    // Combinations (n choose k)
    combinations: (n, k) => {
        if (k > n) return 0;
        if (k === 0 || k === n) return 1;
        return MathUtils.factorial(n) / (MathUtils.factorial(k) * MathUtils.factorial(n - k));
    },
    
    // Permutations
    permutations: (n, k) => {
        if (k > n) return 0;
        return MathUtils.factorial(n) / MathUtils.factorial(n - k);
    }
};

// Export all modules
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        Matrix,
        Statistics,
        Finance,
        Numerical,
        MathUtils
    };
}

// Make available globally for embedded execution
if (typeof globalThis !== 'undefined') {
    globalThis.Matrix = Matrix;
    globalThis.Statistics = Statistics;
    globalThis.Finance = Finance;
    globalThis.Numerical = Numerical;
    globalThis.MathUtils = MathUtils;
}
