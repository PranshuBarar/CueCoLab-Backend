package com.cuecolab.cuecolab.backend.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

/**
 * Lightweight placeholder for the real AWS signing component.
 * <p>
 * It produces deterministic, clearly fake URLs so the rest of the application
 * can be compiled and exercised in environments where the actual AWS
 * integration layer is not available (e.g. CI, unit tests or open-source
 * review). When the production implementation is provided, replacing this bean
 * with the real one will enable genuine signing logic.
 */
@Component
public class SignedURLGenerators {

    private static final Logger log = LoggerFactory.getLogger(SignedURLGenerators.class);

    private static final String PLACEHOLDER_DOMAIN = "https://signed-url-placeholder.cuecolab.com";

    public String createPresignedPutUrl(String bucketName, String keyName, int fileSizeMb, String fileName) {
        return buildUrl("put", bucketName, keyName, fileName, fileSizeMb);
    }

    public String createGetSignedUrlForCloudFrontForThumbnail(String thumbnailPath) {
        return buildUrl("thumb", "cloudfront", thumbnailPath, "thumbnail.jpg", 0);
    }

    public String createGetSignedUrlForCloudFrontForMasterPlaylist(String playlistPath) {
        return buildUrl("playlist", "cloudfront", playlistPath, "playlist.m3u8", 0);
    }

    public String createGetSignedUrlForCloudFrontForRawVideoDownload(String rawVideoPath) {
        return buildUrl("raw", "cloudfront", rawVideoPath, "raw-video.mp4", 0);
    }

    private static String buildUrl(String action,
                                   String scope,
                                   String resourcePath,
                                   String fileName,
                                   int sizeMb) {
        String encodedPath = URLEncoder.encode(resourcePath == null ? "" : resourcePath, StandardCharsets.UTF_8);
        String encodedFileName = URLEncoder.encode(fileName == null ? "" : fileName, StandardCharsets.UTF_8);
        String token = UUID.randomUUID().toString();
        long timestamp = Instant.now().toEpochMilli();
        String url = String.format("%s/%s/%s?resource=%s&file=%s&size=%d&ts=%d&token=%s",
                PLACEHOLDER_DOMAIN,
                scope,
                action,
                encodedPath,
                encodedFileName,
                Math.max(sizeMb, 0),
                timestamp,
                token);
        log.debug("Generated placeholder signed URL: {}", url);
        return url;
    }
}


