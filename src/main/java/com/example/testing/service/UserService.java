package com.example.testing.service;

import com.example.testing.dto.UserChangesRequest;
import com.example.testing.dto.UserResponse;
import com.example.testing.entity.User;
import com.example.testing.repo.UserRepository;
import com.example.testing.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Transactional
    public ResponseEntity<UserResponse> getUserDetails(HttpServletRequest request) {
        User user = getUserByJwt(request);
        var userDto = new UserResponse(user.getName(), user.getEmail());
        return ResponseEntity.ok(userDto);
    }

    @Transactional
    public ResponseEntity<UserResponse> saveUserChanges(HttpServletRequest request, UserChangesRequest userChanges) {
        User user = getUserByJwt(request);
        user.setName(userChanges.name());
        user.setEmail(userChanges.email());
        if (userChanges.password() != null) {
            user.setPassword(passwordEncoder.encode(userChanges.password()));
        }
        return ResponseEntity.ok(new UserResponse(userChanges.name(), userChanges.email()));
    }


    private User getUserByJwt(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);
        return repository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));
    }

}
