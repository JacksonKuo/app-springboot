package com.jkuo.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jkuo.sample.component.WebProxy;

import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping(value = "/smokescreen")
public class SmokescreenController {

    // doesn't need @Autowired annotation since only one constructor and autowiring is implicit
    private final WebProxy webProxy;

    public SmokescreenController(WebProxy webProxy) {
        this.webProxy = webProxy;
    }

    @GetMapping(value = "", produces = "text/html")
	public String smokescreenForm() {
		return """
        <html>
        <head>
            <title>Smokescreen Demo</title>
            <script src="https://bakacore.com:8087/smokescreen" async defer></script>
        </head>
        <body>
            <form action="/smokescreen" method="POST">
                <label for="url">URL:</label>
                <input type="text" id="url" name="url" required />
                <br />
                <input type="submit" value="Submit" />
            </form>
        </body>
        </html>
        """;
	}

    @PostMapping(value = "")
    public Mono<ResponseEntity<String>> retrieveUrl(@RequestParam String url) {
        return webProxy.retrieveUrl(url)
            .map(rawHtml -> ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(rawHtml)
            );
    }

}