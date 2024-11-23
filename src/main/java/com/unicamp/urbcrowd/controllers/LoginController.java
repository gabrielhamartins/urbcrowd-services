package com.unicamp.urbcrowd.controllers;

import com.unicamp.urbcrowd.controllers.dto.LoginRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.LoginResponseDTO;
import com.unicamp.urbcrowd.services.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(loginService.login(loginRequestDTO));
    }
}
