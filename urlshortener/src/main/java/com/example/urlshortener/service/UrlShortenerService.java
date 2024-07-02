package com.example.urlshortener.service;

import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.entity.UrlEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlRepository urlRepository;

    public String shortenUrl(String longUrl) {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setLongUrl(longUrl);
        urlEntity.setExpiresAt(LocalDateTime.now().plusMonths(10));

        UrlEntity savedEntity = urlRepository.save(urlEntity);
        String shortUrl = generateShortUrl(savedEntity.getId());

        savedEntity.setShortUrl(shortUrl);
        urlRepository.save(savedEntity);

        return shortUrl;
    }

    public boolean updateLongUrl(String shortUrl, String longUrl) {
        UrlEntity urlEntity = urlRepository.findByShortUrl(shortUrl);
        if (urlEntity != null) {
            urlEntity.setLongUrl(longUrl);
            urlRepository.save(urlEntity);
            return true;
        }
        return false;
    }

    public String getLongUrl(String shortUrl) {
        UrlEntity urlEntity = urlRepository.findByShortUrl(shortUrl);
        if (urlEntity != null) {
            return urlEntity.getLongUrl();
        }
        throw new NoSuchElementException();
    }

    public boolean updateExpiry(String shortUrl, int daysToAdd) {
        UrlEntity urlEntity = urlRepository.findByShortUrl(shortUrl);
        if (urlEntity != null) {
            urlEntity.setExpiresAt(urlEntity.getExpiresAt().plusDays(daysToAdd));
            urlRepository.save(urlEntity);
            return true;
        }
        return false;
    }

    private String generateShortUrl(long id) {
        String base62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortUrl = new StringBuilder();
        while (id > 0) {
            shortUrl.append(base62.charAt((int) (id % 62)));
            id /= 62;
        }
        return shortUrl.reverse().toString();
    }
}
