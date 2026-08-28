package com.InstinctOne.BlogApp.dtos;

public record TagDto(
        Long id,
        String name) {
    public static record CategoryDto(Long id,
                                     String name
    ) {
    }
}
