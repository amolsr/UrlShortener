package com.urlShortener.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.urlShortener.config.AppConfig;
import com.urlShortener.dtos.LinkDto;
import com.urlShortener.services.LinkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
    private final AppConfig appConfig;

    @Operation(
            summary = "Shorten a URL",
            description = "Generates a unique short link for a given original URL. Optional TTL can be specified in minutes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shortened URL returned"),
                    @ApiResponse(responseCode = "400", description = "Invalid URL format or missing parameters", content = @Content)
            }
    )
    @PostMapping("/shorten")
    public ResponseEntity<String> shortenLink(
            @Parameter(description = "Original URL to shorten", required = true) @RequestParam String originalUrl,
            @Parameter(description = "Time-to-live (TTL) in minutes") @RequestParam(required = false) Long ttlInMinutes) {

        try {
            URI uri = new URI(originalUrl);
            if (uri.getScheme() == null || uri.getHost() == null) {
                return ResponseEntity.badRequest().body("Invalid URL format");
            }
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Invalid URL format");
        }

        Duration ttl = (ttlInMinutes != null) ? Duration.ofMinutes(ttlInMinutes) : null;
        String shortenedUrl = linkService.shortenLink(originalUrl, ttl);
        return ResponseEntity.ok(shortenedUrl);
    }

    @Operation(
            summary = "Get statistics for a short link",
            description = "Returns metadata including original URL, click count, creation and expiration times.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Link statistics returned", content = @Content(schema = @Schema(implementation = LinkDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid shortId", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Link not found", content = @Content)
            }
    )
    @GetMapping("/{shortId}/stats")
    public ResponseEntity<LinkDto> getLinkStats(
            @Parameter(description = "Short link identifier", required = true) @PathVariable String shortId) {
        if (shortId == null || shortId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        LinkDto linkDto = linkService.getLinkStatistics(shortId);
        if (linkDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(linkDto);
    }

    @Operation(
            summary = "Delete a short link",
            description = "Deletes the short link identified by the given shortId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Link deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid shortId", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Link not found", content = @Content)
            }
    )
    @DeleteMapping("/{shortId}")
    public ResponseEntity<String> deleteLink(
            @Parameter(description = "Short link identifier", required = true) @PathVariable String shortId) {
        if (shortId == null || shortId.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid shortId");
        }

        boolean deleted = linkService.deleteLink(shortId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Link deleted successfully");
    }

    @Operation(
            summary = "List all short links",
            description = "Returns all short links with metadata for frontend display.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of links returned", content = @Content(schema = @Schema(implementation = LinkDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<LinkDto>> getAllLinks() {
        List<LinkDto> links = linkService.getAllLinks();
        return ResponseEntity.ok(links);
    }

    @PatchMapping("/increment-count/{shortId}")
    public ResponseEntity<Void> incrementClickCount(
            @PathVariable String shortId) {
        if (shortId == null || shortId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Check if link exists first
        if (linkService.getLinkByShortId(shortId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Use async method for better performance
        linkService.incrementClickCountAsync(shortId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/qr/{shortId}")
    public ResponseEntity<byte[]> generateQr(
            @PathVariable String shortId
    ) throws WriterException, IOException {
        String shortUrl = appConfig.getBaseUrl() + shortId;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix matrix = qrCodeWriter.encode(shortUrl, BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToStream(matrix, "PNG", stream);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(stream.toByteArray());
    }


}
