
package com.studentsphere.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CertificateOcrService {

    public String extractTextFromImage(String imagePath)
            throws IOException, InterruptedException {

        File imageFile = new File(imagePath);

        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IOException(
                    "Certificate image not found: " + imagePath
            );
        }

        ProcessBuilder processBuilder = new ProcessBuilder(
                "tesseract",
                imageFile.getAbsolutePath(),
                "stdout",
                "-l",
                "eng"
        );

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        process.getInputStream(),
                        StandardCharsets.UTF_8
                ))) {

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException(
                    "Tesseract OCR failed: " + output.toString().trim()
            );
        }

        String extractedText = output.toString().trim();

        if (extractedText.isEmpty()) {
            throw new IOException(
                    "No text could be extracted from the certificate image."
            );
        }

        return extractedText;
    }
}
