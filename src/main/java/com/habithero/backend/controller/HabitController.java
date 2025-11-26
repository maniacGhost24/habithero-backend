package com.habithero.backend.controller;


import com.habithero.backend.dto.HabitRequest;
import com.habithero.backend.dto.HabitResponse;
import com.habithero.backend.entity.User;
import com.habithero.backend.repository.UserRepository;
import com.habithero.backend.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;
    private final UserRepository userRepo;

//    CREATE
    @PostMapping
    public ResponseEntity<HabitResponse> createHabit(
            @RequestBody HabitRequest req,
            Authentication auth
    ){
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();
        HabitResponse res = habitService.createHabit(user.getId(), req);

        return ResponseEntity.ok(res);
    }

//    GET ALL
    @GetMapping
    public ResponseEntity<List<HabitResponse>> getHabits(Authentication auth){
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(habitService.getHabits(user.getId()));
    }

//    UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> updateHabit(
            @PathVariable Long id,
            @RequestBody HabitRequest req,
            Authentication auth
    ){
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(habitService.updateHabit(id, user.getId(), req));
    }

//    DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHabit(
            @PathVariable Long id,
            Authentication auth
    ){
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();
        habitService.deleteHabit(id, user.getId());
        return ResponseEntity.ok().build();
    }
}
