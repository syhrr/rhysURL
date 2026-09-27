package com.example.urlshortener.model;

import jakarta.persistence.*;

import java.time.Instant;


// this represents each row in the database
@Entity
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String code; // the actual code used in the short URL

    @Column(name = "target_url",nullable = false, length = 2048)
    private String targetUrl; // the original destination/ url

    private Instant expiresAt;
    private long hits = 0; 
    private Instant lastAccessedAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public ShortUrl() {}

    public ShortUrl(String code, String targetUrl, Instant expiresAt) {
        this.code = code;
        this.targetUrl = targetUrl;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public Instant getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(Instant lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
