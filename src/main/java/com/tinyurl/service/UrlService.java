package com.tinyurl.service;

import com.tinyurl.model.UrlMapping;
import com.tinyurl.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UrlService {
    private final UrlRepository repo;
    private final AtomicLong counter = new AtomicLong(ThreadLocalRandom.current().nextLong(1_000_000L, 9_999_999L));

    @Value("${app.base-domain:http://localhost:8080}")
    private String baseDomain;

    public UrlService(UrlRepository repo) {
        this.repo = repo;
    }

    public String shorten(String rawUrl, String requestedAlias) {
        String normalized = normalizeUrl(rawUrl);

        // If alias provided
        if (requestedAlias != null && !requestedAlias.isBlank()) {
            return repo.findByCode(requestedAlias)
                    .map(existing -> {
                        if (!existing.getLongUrl().equals(normalized)) {
                            throw new IllegalArgumentException("Alias already in use.");
                        }
                        return buildShortUrl(existing.getCode());
                    })
                    .orElseGet(() -> {
                        UrlMapping m = new UrlMapping();
                        m.setCode(requestedAlias);
                        m.setLongUrl(normalized);
                        repo.save(m);
                        return buildShortUrl(requestedAlias);
                    });
        }

        // Already exists?
        return repo.findByLongUrl(normalized)
                .map(m -> buildShortUrl(m.getCode()))
                .orElseGet(() -> {
                    String code;
                    do {
                        long id = counter.getAndIncrement();
                        code = Base62.encode(id);
                    } while (repo.findByCode(code).isPresent());

                    UrlMapping m = new UrlMapping();
                    m.setCode(code);
                    m.setLongUrl(normalized);
                    repo.save(m);

                    return buildShortUrl(code);
                });
    }

    public String resolve(String code) {
        return repo.findByCode(code).map(UrlMapping::getLongUrl).orElse(null);
    }

    private String buildShortUrl(String code) {
        String domain = baseDomain.endsWith("/") ? baseDomain.substring(0, baseDomain.length() - 1) : baseDomain;
        return domain + "/" + code;
    }

    private String normalizeUrl(String url) {
        String u = url.trim();
        if (!u.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*$")) {
            u = "http://" + u;
        }
        try {
            URI parsed = new URI(u);
            if (parsed.getHost() == null) {
                throw new IllegalArgumentException("Invalid URL: missing host");
            }
            return parsed.toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}
