package com.example.controller;

import com.example.urlshortener.service.UrlShortenerService;

import jakarta.servlet.http.HttpServletResponse;

import com.example.urlshortener.dto.UpdateExpiryRequest;
import com.example.urlshortener.dto.UpdateUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String longUrl) {
        String shortUrl = urlShortenerService.shortenUrl(longUrl);
        return ResponseEntity.ok("http://localhost:8080/" + shortUrl);
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateUrl(@RequestBody UpdateUrlRequest request) {
        boolean success = urlShortenerService.updateLongUrl(request.getShortUrl(), request.getLongUrl());
        return ResponseEntity.ok(success);
    }

    @GetMapping("/{shortUrl}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortUrl) {
        try {
            String longUrl = urlShortenerService.getLongUrl(shortUrl);
            response.sendRedirect(longUrl);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }

    @PostMapping("/update-expiry")
    public ResponseEntity<Boolean> updateExpiry(@RequestBody UpdateExpiryRequest request) {
        boolean success = urlShortenerService.updateExpiry(request.getShortUrl(), request.getDaysToAdd());
        return ResponseEntity.ok(success);
    }
}

