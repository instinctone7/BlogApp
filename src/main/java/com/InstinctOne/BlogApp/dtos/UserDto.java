package com.InstinctOne.BlogApp.dtos;

import com.InstinctOne.BlogApp.entities.Post;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
        Long id,
        String email,
        String name,
        LocalDateTime createdAt,
        List<Post> posts
) {
}
