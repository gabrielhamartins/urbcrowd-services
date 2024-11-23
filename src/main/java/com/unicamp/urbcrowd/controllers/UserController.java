package com.unicamp.urbcrowd.controllers;

import com.unicamp.urbcrowd.controllers.dto.CreateUserRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.UserInfoResponseDTO;
import com.unicamp.urbcrowd.models.User;
import com.unicamp.urbcrowd.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<UserInfoResponseDTO> create(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
        return new ResponseEntity<>(userService.create(createUserRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(Authentication authentication) throws AccountNotFoundException {
        return ResponseEntity.ok(userService.getUserInfo());
    }
}
