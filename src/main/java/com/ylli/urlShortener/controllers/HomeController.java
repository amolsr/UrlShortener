package com.ylli.urlShortener.controllers;

import com.ylli.urlShortener.dtos.LinkDto;
import com.ylli.urlShortener.models.Link;
import com.ylli.urlShortener.services.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final LinkService linkService;
    private static final String BASE_URL = "https://short.link/";

    @GetMapping("/{shortId}")
    public String redirectToOriginal(@PathVariable String shortId) {
        Link link = linkService.getLinkByShortId(shortId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found"));

        if (link.getExpiresAt() != null && link.getExpiresAt().isBefore(LocalDateTime.now())) {
            linkService.deleteLink(shortId);
            return "notFound";
        }

        linkService.incrementClickCount(link);

        return "redirect:" + link.getOriginalUrl();
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("baseUrl", BASE_URL);

        return "index";
    }


}
