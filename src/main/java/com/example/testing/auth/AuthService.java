package com.example.testing.auth;

import com.example.testing.auth.dto.AuthRequest;
import com.example.testing.auth.dto.AuthenticationResponse;
import com.example.testing.auth.dto.RegisterRequest;
import com.example.testing.entity.User;
import com.example.testing.exception.UserAlreadyExistException;
import com.example.testing.exception.WrongPasswordException;
import com.example.testing.repo.UserRepository;
import com.example.testing.security.JwtService;
import com.example.testing.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistException(request.email());
        }
        var user = new User(request.name(),
                passwordEncoder.encode(request.password()),
                request.email(), UserRole.USER);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        System.out.println(jwtToken);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User with [" + request.email() + "] was not found"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new WrongPasswordException(request.email());
        }
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var jwtToken = jwtService.generateToken(user);
        System.out.println(jwtToken);
        return new AuthenticationResponse(jwtToken);
    }
}