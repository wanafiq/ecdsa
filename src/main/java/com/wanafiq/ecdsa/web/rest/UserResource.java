package com.wanafiq.ecdsa.web.rest;

import com.wanafiq.ecdsa.model.User;
import com.wanafiq.ecdsa.repository.UserRepository;
import com.wanafiq.ecdsa.web.exception.NotFoundException;
import com.wanafiq.ecdsa.web.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserResource extends BaseResource {

    private final UserRepository userRepo;

    @GetMapping("/users")
    public ResponseEntity<?> getUser() {
        if (!loggedIn()) {
            throw new UnauthorizedException();
        }

        User user = userRepo.findById(getPrincipalName())
                .orElseThrow(NotFoundException::new);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/activate/{userId}")
    public ResponseEntity<?> activateUser(@PathVariable String userId) {
        if (!loggedIn()) {
            throw new UnauthorizedException();
        }

        User user = userRepo.findById(userId)
                .orElseThrow(NotFoundException::new);

        user.setActivatedAt(Instant.now());
        user.setEnabled(true);
        user = userRepo.save(user);

        return ResponseEntity.ok(user);
    }

}
