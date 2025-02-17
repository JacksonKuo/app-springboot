package com.jkuo.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Value;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping(value = "/captcha", produces = "text/html")
public class CaptchaController {

    //default value so tests can run
    @Value("${hcaptcha.secret}")
    private String hcaptchaSecret;

	@GetMapping("")
	public String captchaform() {
		return """
        <html>
        <head>
            <title>hCaptcha Demo</title>
            <script src="https://js.hcaptcha.com/1/api.js" async defer></script>
        </head>
        <body>
            <form action="" method="POST">
            <div class="h-captcha" data-sitekey="a36e9a82-f33c-4234-be50-533a37283cba"></div>
            <br />
            <input type="submit" value="Submit" />
            </form>
        </body>
        </html>
        """;
	}

    @PostMapping("")
    public String submit(@RequestParam("h-captcha-response") String clientResponse) throws Exception {
        String formData = "secret=" + URLEncoder.encode(hcaptchaSecret, StandardCharsets.UTF_8)
                        + "&response=" + URLEncoder.encode(clientResponse, StandardCharsets.UTF_8);

        System.out.println(formData);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.hcaptcha.com/siteverify"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(formData))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        final String name = jsonObject.get("success").getAsString();

        if (name.equals("true")) {
            return "Captcha successful!";
        }
        else {
            return "Captcha submission failed!";
        }
    }


}
