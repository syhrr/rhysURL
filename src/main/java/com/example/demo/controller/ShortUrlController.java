package com.example.demo.controller;

import com.example.demo.model.ShortUrl;
import com.example.demo.model.ShortUrlResponse;
import com.example.demo.service.ShortUrlService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;


@RestController
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping("/api/urls")
    public ResponseEntity<ShortUrlResponse> create(@Valid @RequestBody CreateShortUrlRequest request) {
        // create a new shortUrl by taking the targetUrl from the request
        ShortUrl shortUrl = shortUrlService.create(request.targetUrl(), request.expiresAt());

        // then combine the "/" with the code generated to get the new shortUrl
        URI location = URI.create("/" + shortUrl.getCode());

        /*  return a HTTP response that looks like this:
         * {
         *   "code": "sde134",
         *   "targetUrl": "https://the-original-long-url.com/...",
         *   "expiresAt": null,
         *   "createdAt": "2026-09-20T..."
         * }
         */

        return ResponseEntity.created(location).body(ShortUrlResponse.from(shortUrl));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        // take the code from the request and then search for it
        ShortUrl shortUrl = shortUrlService.resolve(code);

        // build a 302 response with Location header pointing at targetUrl
        URI location = URI.create(shortUrl.getTargetUrl());

        return ResponseEntity.status(HttpStatus.FOUND).location(location).build();
    }

    public record CreateShortUrlRequest(
            @NotBlank String targetUrl,
            Instant expiresAt
    ) {}
}

