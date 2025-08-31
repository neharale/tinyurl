package com.tinyurl;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UrlController {

    private final Map<String, String> store = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shorten(@RequestBody UrlRequest request) {
        long id = counter.incrementAndGet();
        String shortCode = Long.toHexString(id);
        store.put(shortCode, request.getUrl());
        String shortUrl = "http://localhost:8080/" + shortCode;
        return ResponseEntity.ok(new UrlResponse(shortUrl));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> redirect(@PathVariable String code) {
        String originalUrl = store.get(code);
        if (originalUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(302).header("Location", originalUrl).build();
    }
}
