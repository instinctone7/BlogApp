package com.InstinctOne.BlogApp.dtos;

import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotNull
        String name
) {
}
