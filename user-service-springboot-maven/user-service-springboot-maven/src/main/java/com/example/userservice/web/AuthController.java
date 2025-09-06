package com.example.userservice.web;

import com.example.userservice.dto.ErrorEnvelope;
import com.example.userservice.dto.SignUpRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;

    @PostMapping(value = "/sign-up", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
        try {
            UserResponse resp = userService.signUp(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorEnvelope.of(HttpStatus.CONFLICT.value(), e.getMessage()));
        }
    }

    @GetMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestHeader(name = "Authorization", required = false) String authorization) {
        try {
            UserResponse resp = userService.login(authorization);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorEnvelope.of(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }
}
