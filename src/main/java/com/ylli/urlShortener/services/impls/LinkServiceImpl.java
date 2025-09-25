package com.ylli.urlShortener.services.impls;

import com.ylli.urlShortener.dtos.LinkDto;
import com.ylli.urlShortener.models.Link;
import com.ylli.urlShortener.repositories.LinkRepository;
import com.ylli.urlShortener.services.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinkRepository linkRepository;
    private static final String BASE_URL = "https://short.link/";
    private static final int SHORT_ID_LENGTH = 6;
    private static final int DEFAULT_TTL_MINUTES = 300;


    @Override
    @Transactional
    public String shortenLink(String originalUrl, Duration ttl) {
        String shortId;
        do {
            shortId = generateRandomShortId();
        } while (linkRepository.existsByShortId(shortId));

        LocalDateTime expiresAt;
        if (ttl == null) {
            expiresAt = LocalDateTime.now().plusMinutes(DEFAULT_TTL_MINUTES);
        } else {
            expiresAt = LocalDateTime.now().plus(ttl);
        }

        Link link = Link.builder()
                .originalUrl(originalUrl)
                .shortId(shortId)
                .createdAt(LocalDateTime.now())
                .expiresAt(expiresAt)
                .clickCount(0)
                .build();

        linkRepository.save(link);

        return BASE_URL + shortId;
    }

    @Override
    public LinkDto getLinkStatistics(String shortId) {
        return linkRepository.findByShortId(shortId)
                .map(link -> LinkDto.builder()
                        .shortLink(BASE_URL + link.getShortId())
                        .shortId(link.getShortId())
                        .originalUrl(link.getOriginalUrl())
                        .clickCount(link.getClickCount())
                        .createdAt(link.getCreatedAt())
                        .expiresAt(link.getExpiresAt())
                        .build())
                .orElse(null);
    }

    @Override
    @Transactional
    public boolean deleteLink(String shortId) {
        return linkRepository.findByShortId(shortId)
                .map(link -> {
                    linkRepository.delete(link);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<LinkDto> getAllLinks() {
        return linkRepository.findAll()
                .stream()
                .map(link -> LinkDto.builder()
                        .shortLink(BASE_URL + link.getShortId())
                        .shortId(link.getShortId())
                        .originalUrl(link.getOriginalUrl())
                        .clickCount(link.getClickCount())
                        .createdAt(link.getCreatedAt())
                        .expiresAt(link.getExpiresAt())
                        .build())
                .toList();
    }

    @Override
    public Optional<Link> getLinkByShortId(String shortId) {
        return linkRepository.findByShortId(shortId);
    }

    @Override
    @Transactional
        public void incrementClickCount(Link link) {
        link.setClickCount(link.getClickCount() + 1);
        linkRepository.save(link);
    }

    private String generateRandomShortId() {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(SHORT_ID_LENGTH);
        for (int i = 0; i < SHORT_ID_LENGTH; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
