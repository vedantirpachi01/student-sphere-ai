package com.studentsphere.ai;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class CertificateTextExtractor {

    public String extractText(String pdfPath) throws IOException {

        File pdfFile = new File(pdfPath);

        if (!pdfFile.exists() || !pdfFile.isFile()) {
            throw new IOException("PDF file not found: " + pdfPath);
        }

        try (PDDocument document = Loader.loadPDF(pdfFile)) {

            if (document.isEncrypted()) {
                throw new IOException("The PDF is encrypted.");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String extractedText = stripper.getText(document);

            if (extractedText == null || extractedText.isBlank()) {
                return "NO_TEXT_FOUND";
            }

            return extractedText.trim();
        }
    }
}