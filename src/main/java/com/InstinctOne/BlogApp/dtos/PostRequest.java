package com.InstinctOne.BlogApp.dtos;

import com.InstinctOne.BlogApp.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record PostRequest(
        @NotBlank
        String title,
        @NotBlank
        String content,
        PostStatus status,
        @Positive
        Integer readingTime,
        @Positive
        Long userId,
        @NotBlank
        String category,
        List<@NotBlank String> tags
) {
}
