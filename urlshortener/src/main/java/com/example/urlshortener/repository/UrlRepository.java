package com.example.urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.urlshortener.entity.UrlEntity;

@Repository
public interface UrlRepository extends JpaRepository <UrlEntity, Long> {
	UrlEntity findByShortUrl(String shortUrl);

}
