package com.unicamp.urbcrowd.services;

import com.unicamp.urbcrowd.controllers.dto.LoginRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.LoginResponseDTO;
import com.unicamp.urbcrowd.models.Role;
import com.unicamp.urbcrowd.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public LoginService(UserRepository userRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        var user = userRepository.findByUsername(loginRequestDTO.username());
        if (user.isEmpty() || !user.get().passwordMatches(loginRequestDTO, bCryptPasswordEncoder))
            throw new BadCredentialsException("Username or password is invalid.");

        var expiresIn = 3600L;
        var roles = user.get().getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        var claims = JwtClaimsSet.builder()
                .issuer("urbcrowd")
                .subject(user.get().getId())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .claim("scope", roles)
                .claim("given_name", user.get().getName())
                .claim("email", user.get().getEmail())
                .build();

        return new LoginResponseDTO(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), expiresIn, roles);
    }
}
