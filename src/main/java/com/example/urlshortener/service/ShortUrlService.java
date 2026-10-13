package com.example.urlshortener.service;

import com.example.urlshortener.exception.ShortUrlExpiredException;
import com.example.urlshortener.model.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import com.example.urlshortener.exception.ShortUrlNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class ShortUrlService {
    private static final int MAX_CODE_ATTEMPTS = 5;

    private final CodeGenerator codeGenerator;
    private final ShortUrlRepository repository;

    public ShortUrlService(CodeGenerator codeGenerator, ShortUrlRepository repository) {
        this.codeGenerator = codeGenerator;
        this.repository = repository;
    }

    public ShortUrl create(String targetUrl, Instant expiresAt) {
        for (int attempt = 0; attempt < MAX_CODE_ATTEMPTS; attempt++) {
            String code = codeGenerator.generate();

            if (repository.findByCode(code).isEmpty()) {
                ShortUrl shortUrl = new ShortUrl(code, targetUrl, expiresAt);
                return repository.save(shortUrl);
            }
        }

        throw new IllegalStateException("Failed to generate a unique code after " + MAX_CODE_ATTEMPTS + " attempts");
    }


    public ShortUrl resolve(String code){
        Optional<ShortUrl> found = repository.findByCode(code);

        if (found.isEmpty()) {
            throw new ShortUrlNotFoundException(code);
        }

        ShortUrl shortUrl = found.get();
        Instant expiry = shortUrl.getExpiresAt();
        if (expiry == null || expiry.isAfter(Instant.now())) {

            shortUrl.setHits(shortUrl.getHits()+1);
            shortUrl.setLastAccessedAt(Instant.now());
            repository.save(shortUrl);

            return shortUrl;

        } else {

            throw new ShortUrlExpiredException(code);
        }

    }
}
