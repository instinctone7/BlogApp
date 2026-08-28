package com.InstinctOne.BlogApp.dtos;


import jakarta.validation.constraints.*;

public record UserDtoRequest(
        @Email
        @NotBlank
        String email,

        String password,
        @NotBlank
        String name
) {
}
