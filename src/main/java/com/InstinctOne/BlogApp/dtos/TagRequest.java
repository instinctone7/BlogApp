package com.InstinctOne.BlogApp.dtos;

import jakarta.validation.constraints.NotBlank;

public record TagRequest(
        @NotBlank
        String name) {
}
