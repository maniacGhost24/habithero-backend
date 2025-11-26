package com.habithero.backend.dto;

public record HabitResponse(Long id, String title, String description, boolean completedToday) {
}
