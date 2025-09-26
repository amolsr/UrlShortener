package com.urlShortener.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.urlShortener.dtos.LinkDto;
import com.urlShortener.models.Link;
import com.urlShortener.services.LinkService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final LinkService linkService;
    private static final String BASE_URL = "http://localhost:8080/";

    @GetMapping("/{shortId}")
    public String redirectToOriginal(@PathVariable String shortId) {
        String originalUrl = linkService.getOriginalUrlByShortId(shortId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found"));

        // TODO: increment click count 
        // linkService.incrementClickCount(link);

        return "redirect:" + originalUrl;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("baseUrl", BASE_URL);

        return "index";
    }


}
