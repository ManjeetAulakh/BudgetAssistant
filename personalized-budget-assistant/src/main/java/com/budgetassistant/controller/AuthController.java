package com.budgetassistant.controller;

import com.budgetassistant.auth.JwtUtil;
import com.budgetassistant.model.User;
import com.budgetassistant.payload.JwtResponse;
import com.budgetassistant.payload.LoginRequest;
import com.budgetassistant.payload.RegisterRequest;
import com.budgetassistant.repository.UserRepository;
import com.budgetassistant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager; // Used for /login
    private final UserRepository userRepository;             // Used for existence check
    private final UserService userService;                  // Used for saving user (registration)
    private final PasswordEncoder encoder;                  // Used for hashing passwords
    private final JwtUtil jwtUtil;                          // Used for generating the token

    /**
     * PUBLIC ENDPOINT: /api/auth/login
     * Authenticates the user and returns a JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. Authenticate the user credentials (checks against UserService/PasswordEncoder)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Set authentication in the context (optional, but good practice)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 3. Get UserDetails and generate JWT
        User userDetails = (User) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails); // Uses the final, corrected method name

        // 4. Return the JWT to the client
        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", userDetails.getUsername()));
    }

    /**
     * PUBLIC ENDPOINT: /api/auth/register
     * Creates a new user account.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>("Error: Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // 1. Create User entity
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        
        // 2. Hash and set password
        user.setPassword(encoder.encode(registerRequest.getPassword()));

        // 3. Save the user
        userService.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }
}