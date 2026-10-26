
package com.studentsphere.ai;

public class App {

    public static void main(String[] args) {

       String imagePath = "C:\\mysql-reset\\sample-certificate-2.png";
       
        System.out.println("==================================");
        System.out.println("     STUDENT-SPHERE AI MODULE");
        System.out.println("==================================");

        try {
            // Step 1: Extract text from the certificate image
            System.out.println("\nStep 1: Extracting certificate text...");

            CertificateOcrService ocrService =
                    new CertificateOcrService();

            String extractedText =
                    ocrService.extractTextFromImage(imagePath);

            System.out.println("Image text extraction successful.");

            // Step 2: Analyze extracted text using Gemini
            System.out.println("\nStep 2: Analyzing certificate with Gemini...");

            CertificateAnalysisService analysisService =
                    new CertificateAnalysisService();

            String analysisResult =
                    analysisService.analyzeCertificate(extractedText);

            // Step 3: Display the analysis
            System.out.println("\nCertificate Analysis Result:");
            System.out.println("----------------------------");
            System.out.println(analysisResult);
            System.out.println("----------------------------");

            System.out.println("\nCertificate analysis completed.");

        } catch (Exception e) {
            System.out.println("\nCertificate processing failed.");
            System.out.println("Error: " + e.getMessage());
        }
    }
}
