package com.example.urlshortener.service;

import com.example.urlshortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock CodeGenerator codeGenerator;
    @Mock ShortUrlRepository repository;
    @InjectMocks ShortUrlService service;

    // ---- create() ----

    @Test
    void create_savesUrlWithGeneratedCode() {
        // TODO: stub codeGenerator.generate() -> "abc1234"
        // TODO: stub repository.findByCode("abc1234") -> Optional.empty()
        // TODO: stub repository.save(any()) to return its argument
        // TODO: call service.create(...) and assert code/targetUrl/expiresAt
    }

    @Test
    void create_retriesWhenCodeAlreadyTaken() {
        // TODO: generate() returns "taken", then "free"
        // TODO: findByCode("taken") -> Optional.of(existing), findByCode("free") -> empty
        // TODO: assert result code is "free" and generate() was called twice
    }

    @Test
    void create_throwsAfterMaxAttempts() {
        // TODO: findByCode always returns a present Optional
        // TODO: assertThatThrownBy(...).isInstanceOf(IllegalStateException.class)
        // TODO: verify(repository, never()).save(any())
    }

    // ---- resolve() ----

    @Test
    void resolve_unknownCode_throwsNotFound() {
        // TODO: findByCode -> empty; expect ShortUrlNotFoundException
    }

    @Test
    void resolve_expiredCode_throwsExpiredAndDoesNotSave() {
        // TODO: ShortUrl with expiresAt = Instant.now().minusSeconds(60)
        // TODO: expect ShortUrlExpiredException; verify(repository, never()).save(any())
    }

    @Test
    void resolve_noExpiry_incrementsHitsAndSetsLastAccessed() {
        // TODO: ShortUrl with expiresAt = null, hits = 0
        // TODO: after resolve: hits == 1, lastAccessedAt not null, save called once
    }

    @Test
    void resolve_futureExpiry_returnsUrl() {
        // TODO: expiresAt = Instant.now().plusSeconds(3600); assert returned target URL
    }
}
