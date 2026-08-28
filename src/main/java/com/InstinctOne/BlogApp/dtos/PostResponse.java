package com.InstinctOne.BlogApp.dtos;

import com.InstinctOne.BlogApp.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponse(
        Long id,
        String title,
        String content,
        PostStatus status,
        Integer readingTime,
        AuthorResponse author,
        CategoryResponse category,
        Set<TagResponse> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record AuthorResponse(Long id, String email, String name) {
    }

    public record CategoryResponse(Long id, String name) {
    }

    public record TagResponse(Long id, String name) {
    }
}

