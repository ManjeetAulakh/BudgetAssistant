package com.budgetassistant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.budgetassistant.model.User;

public interface UserRepository extends JpaRepository<User, Integer>  {

    // Crucial method for Spring Security/JWT filter to load a user by name
    Optional<User> findByUsername(String username);

    // Used during registration to prevent duplicate usernames
    Boolean existsByUsername(String username);
}
