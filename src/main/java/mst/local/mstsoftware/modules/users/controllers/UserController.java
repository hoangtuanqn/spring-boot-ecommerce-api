package mst.local.mstsoftware.modules.users.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mst.local.mstsoftware.modules.users.entities.User;
import mst.local.mstsoftware.modules.users.repositories.UserRepository;
import mst.local.mstsoftware.modules.users.resources.UserResource;
import mst.local.mstsoftware.resources.SuccessResource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @GetMapping("me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        UserResource userResource = new UserResource(user.getId(), user.getEmail(), user.getName());
        SuccessResource<UserResource> successResource = new SuccessResource("SUCCESS", userResource);
        return ResponseEntity.ok(successResource);
    }
}
