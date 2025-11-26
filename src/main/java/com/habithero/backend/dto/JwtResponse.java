package com.habithero.backend.dto;

public record JwtResponse(String token, String type, String subject) {
}
