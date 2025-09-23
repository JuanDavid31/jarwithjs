package com.example.javareactjar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import com.google.common.io.Files;

@SpringBootApplication
public class JavaReactJarApplication {

    public static void main(String[] args) {

        File dir = createTempDirectory();
        try {
            System.out.println("Created temporary directory: " + dir);
        }
        finally {
            dir.delete();
        }
        SpringApplication.run(JavaReactJarApplication.class, args);
    }

    private static File createTempDirectory() {
        // https://nvd.nist.gov/vuln/detail/CVE-2023-2976
        return Files.createTempDir();
    }
}
