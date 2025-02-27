package com.jkuo.sample.service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class CaptchaService {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaService.class);

    // default value so tests can run
    @Value("${hcaptcha.secret}")
    private String hcaptchaSecret;

    public CaptchaService() {
    }

    public String verifyCaptcha(String clientResponse) {
        String formData = "secret=" + URLEncoder.encode(hcaptchaSecret, StandardCharsets.UTF_8)
                + "&response=" + URLEncoder.encode(clientResponse, StandardCharsets.UTF_8);
        logger.info("formData: " + formData);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.hcaptcha.com/siteverify"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("responseBody: " + response.body());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            if (jsonObject == null) {
                return null;
            }
            return jsonObject.get("success").getAsString();
        } catch (Exception e) {
            return null;
        }

    }

}
