package com.tinyurl.repository;

import com.tinyurl.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByCode(String code);
    Optional<UrlMapping> findByLongUrl(String longUrl);
}
