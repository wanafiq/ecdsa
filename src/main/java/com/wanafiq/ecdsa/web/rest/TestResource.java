package com.wanafiq.ecdsa.web.rest;

import com.wanafiq.ecdsa.dto.LoginRequest;
import com.wanafiq.ecdsa.dto.LoginResponse;
import com.wanafiq.ecdsa.model.User;
import com.wanafiq.ecdsa.repository.UserRepository;
import com.wanafiq.ecdsa.service.JwtService;
import com.wanafiq.ecdsa.common.SecurityUtils;
import com.wanafiq.ecdsa.web.exception.NotFoundException;
import com.wanafiq.ecdsa.web.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TestResource {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authRequest = UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword());
        Authentication authResponse = authManager.authenticate(authRequest);

        SecurityContextHolder.getContext().setAuthentication(authResponse);

        String token = jwtService.generateToken(authResponse.getName());

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUser() {
        String principalName = SecurityUtils.getPrincipalName();
        if (principalName == null) {
            throw new UnauthorizedException();
        }

        User user = userRepo.findById(principalName)
                .orElseThrow(NotFoundException::new);

        return ResponseEntity.ok(user);
    }

}
