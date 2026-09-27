package com.example.urlshortener.model;

import java.time.Instant;

public record ShortUrlResponse(String code, String targetUrl, Instant expiresAt, Instant createdAt) {
    public static ShortUrlResponse from(ShortUrl shortUrl) {
        return new ShortUrlResponse(
                shortUrl.getCode(),
                shortUrl.getTargetUrl(),
                shortUrl.getExpiresAt(),
                shortUrl.getCreatedAt()
        );
    }
}