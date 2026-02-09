package com.example.Day3SMS.dto;

public record StudentResponseDto<S>(
        String id,
        String name,
        int age,
        String email
) {
    //
}