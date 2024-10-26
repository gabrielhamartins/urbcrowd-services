package com.unicamp.urbcrowd.controllers;

import com.unicamp.urbcrowd.controllers.dto.LoginRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.LoginResponseDTO;
import com.unicamp.urbcrowd.models.Role;
import com.unicamp.urbcrowd.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class LoginController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public LoginController(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        var user = userRepository.findByUsername(loginRequestDTO.username());
        if(user.isEmpty() || !user.get().passwordMatches(loginRequestDTO, bCryptPasswordEncoder))
            throw new BadCredentialsException("Username or password is invalid.");

        var expiresIn = 3600L;
        var roles = user.get().getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining());

        var claims = JwtClaimsSet.builder()
                .issuer("urbcrowd")
                .subject(user.get().getId())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .claim("scope", roles)
                .claim("given_name", user.get().getName())
                .claim("email", user.get().getEmail())
                .build();

        return ResponseEntity.ok(new LoginResponseDTO(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), expiresIn));
    }
}
