package com.ylli.urlShortener.services;

import com.ylli.urlShortener.dtos.LinkDto;
import com.ylli.urlShortener.models.Link;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface LinkService {
    String shortenLink(String url, Duration ttl);

    LinkDto getLinkStatistics(String shortId);

    boolean deleteLink(String shortId);

    List<LinkDto> getAllLinks();

    Optional<Link> getLinkByShortId(String shortId);

    void incrementClickCount(Link link);

}
