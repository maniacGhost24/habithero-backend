package com.habithero.backend.repository;

import com.habithero.backend.entity.Habit;
import com.habithero.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit,Long> {
    List<Habit> findByUser(User user);
}
