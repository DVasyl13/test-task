package com.example.testing.controller.rest;

import com.example.testing.dto.UserChangesRequest;
import com.example.testing.dto.UserResponse;
import com.example.testing.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<UserResponse> getUserDetails(@NonNull HttpServletRequest request) {
        return service.getUserDetails(request);
    }

    @PutMapping
    public ResponseEntity<UserResponse> changeUserInfo(@NonNull HttpServletRequest request,
                                                 @RequestBody UserChangesRequest userChanges) {
        return service.saveUserChanges(request, userChanges);
    }
}
