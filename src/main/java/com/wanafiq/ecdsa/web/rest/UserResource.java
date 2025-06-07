package com.wanafiq.ecdsa.web.rest;

import com.wanafiq.ecdsa.model.User;
import com.wanafiq.ecdsa.common.SecurityUtils;
import com.wanafiq.ecdsa.repository.UserRepository;
import com.wanafiq.ecdsa.web.exception.NotFoundException;
import com.wanafiq.ecdsa.web.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserRepository userRepo;

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
