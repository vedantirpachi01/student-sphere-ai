package com.studentsphere.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GeminiService {

    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/interactions";

    private static final String MODEL = "gemini-3.5-flash-lite";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GeminiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public String askGemini(String prompt) throws Exception {

        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GEMINI_API_KEY environment variable is missing."
            );
        }

        // Prepare the request body.
        com.fasterxml.jackson.databind.node.ObjectNode requestBody =
        objectMapper.createObjectNode();
        requestBody.put("model", MODEL);
        requestBody.put("input", prompt);
        requestBody.put("store", false);

        // Build the HTTP request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString(requestBody)
                ))
                .build();

        // Send the request.
        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {
           throw new RuntimeException(
    "Gemini API returned HTTP " + response.statusCode()
    + ". Details: " + response.body()
);
        }

        // Read the model's response.
        JsonNode root = objectMapper.readTree(response.body());
        JsonNode steps = root.path("steps");

        for (JsonNode step : steps) {
            if ("model_output".equals(step.path("type").asText())) {
                JsonNode content = step.path("content");

                for (JsonNode item : content) {
                    if ("text".equals(item.path("type").asText())) {
                        return item.path("text").asText();
                    }
                }
            }
        }

        throw new IllegalStateException(
                "No text response was returned by Gemini."
        );
    }
}