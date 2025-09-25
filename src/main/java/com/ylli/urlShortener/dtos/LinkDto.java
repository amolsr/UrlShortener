package com.ylli.urlShortener.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object representing a shortened URL and its metadata")
public class LinkDto {

    @Schema(
            description = "Full shortened link including domain",
            example = "https://short.link/abc123",
            required = true
    )
    private String shortLink;

    @Schema(
            description = "Unique identifier for the short link",
            example = "abc123",
            required = true
    )
    private String shortId;

    @Schema(
            description = "Original URL before shortening",
            example = "https://www.newswebsite.com/latest-news/politics",
            required = true
    )
    private String originalUrl;

    @Schema(
            description = "Number of times the short link has been clicked",
            example = "5",
            required = true
    )
    private int clickCount;

    @Schema(
            description = "Creation timestamp of the short link",
            example = "2025-09-24T20:00:00",
            required = true
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Expiration timestamp of the short link; null if no expiration is set",
            example = "2025-09-25T20:00:00"
    )
    private LocalDateTime expiresAt;
}
