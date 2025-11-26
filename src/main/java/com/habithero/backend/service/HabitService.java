package com.habithero.backend.service;


import com.habithero.backend.dto.HabitRequest;
import com.habithero.backend.dto.HabitResponse;
import com.habithero.backend.entity.Habit;
import com.habithero.backend.entity.User;
import com.habithero.backend.repository.HabitRepository;
import com.habithero.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepo;
    private final UserRepository userRepo;


    public HabitResponse createHabit(Long userId, HabitRequest req){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Habit habit = Habit.builder()
                .title(req.title())
                .description(req.description())
                .user(user)
                .build();

        habit = habitRepo.save(habit);
        return toResponse(habit);
    }

    public List<HabitResponse> getHabits(Long userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return habitRepo.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public HabitResponse updateHabit(Long id, Long userId, HabitRequest req){
        Habit habit = habitRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        if(!habit.getUser().getId().equals(userId)){
            throw new RuntimeException("Unauthorized access to habit");
        }

        habit.setTitle(req.title());
        habit.setDescription(req.description());

        habit = habitRepo.save(habit);
        return toResponse(habit);
    }

    public void deleteHabit(Long id, Long userId){
        Habit habit = habitRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        if(!habit.getUser().getId().equals(userId)){
            throw new RuntimeException("Unauthorized access to habit");
        }

        habitRepo.delete(habit);

    }

    private HabitResponse toResponse(Habit h){
        return new HabitResponse(h.getId(), h.getTitle(), h.getDescription(), h.isCompleteToday());
    }
}