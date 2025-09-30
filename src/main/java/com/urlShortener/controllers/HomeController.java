package com.urlShortener.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.urlShortener.config.AppConfig;
import com.urlShortener.services.LinkService;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final LinkService linkService;
    private final AppConfig appConfig;

    @GetMapping("/{shortId}")
    public String redirectToOriginal(@PathVariable String shortId) {
        String originalUrl = linkService.getOriginalUrlByShortId(shortId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found"));

        // Use async method for better performance
        linkService.incrementClickCountAsync(shortId);

        return "redirect:" + originalUrl;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("baseUrl", appConfig.getBaseUrl());

        return "index";
    }


}
