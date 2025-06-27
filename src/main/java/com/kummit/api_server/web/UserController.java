package com.kummit.api_server.web;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.kummit.api_server.dto.UserRegisterRequest;
import com.kummit.api_server.dto.UserInfoResponse;
import com.kummit.api_server.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(
        @Valid @RequestBody UserRegisterRequest req
    ) {
        Long newId = userService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(newId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponse> getUser(
        @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
