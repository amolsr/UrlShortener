package com.urlShortener.services;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import com.urlShortener.dtos.LinkDto;
import com.urlShortener.models.Link;

public interface LinkService {
    String shortenLink(String url, Duration ttl);

    LinkDto getLinkStatistics(String shortId);

    boolean deleteLink(String shortId);

    List<LinkDto> getAllLinks();

    Optional<Link> getLinkByShortId(String shortId);

    Optional<String> getOriginalUrlByShortId(String shortId);

    void incrementClickCount(Link link);

    void cacheShortenedLink(String shortId, String originalUrl, Duration ttl);
}
