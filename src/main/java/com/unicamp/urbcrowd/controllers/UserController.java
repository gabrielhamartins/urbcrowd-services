package com.unicamp.urbcrowd.controllers;

import com.unicamp.urbcrowd.controllers.dto.CreateUserRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.UserInfoResponseDTO;
import com.unicamp.urbcrowd.models.Role;
import com.unicamp.urbcrowd.models.User;
import com.unicamp.urbcrowd.repositories.RoleRepository;
import com.unicamp.urbcrowd.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.unicamp.urbcrowd.controllers.mapper.UserInfoMapper.userToUserInfoDTO;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<String> create(@RequestBody CreateUserRequestDTO createUserRequestDTO) throws BadRequestException {

        if(this.userRepository.findByUsernameOrEmail(createUserRequestDTO.username(), createUserRequestDTO.email()).isPresent())
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Username or email already exists.");

        var role = roleRepository.findByName(Role.Values.DEFAULT.name());

        User newUser = this.userRepository.save(User.builder()
                .name(createUserRequestDTO.name())
                .username(createUserRequestDTO.username())
                .email(createUserRequestDTO.email())
                .roles(Set.of(role))
                .password(bCryptPasswordEncoder.encode(createUserRequestDTO.password())).build());

        return new ResponseEntity<>(newUser.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> findAll(){

        return ResponseEntity.ok(this.userRepository.findAll());
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            var role = roleRepository.findByName(Role.Values.DEFAULT.name());
            User user = User.builder()
                    .name(jwt.getClaim("given_name"))
                    .username(email.substring(0, email.indexOf("@")))
                    .email(email)
                    .roles(Set.of(role))
                    .picture(jwt.getClaim("picture"))
                    .password(bCryptPasswordEncoder.encode(jwt.getClaim("sub")))
                    .build();
            userRepository.save(user);
            return ResponseEntity.ok(userToUserInfoDTO(user));
        }
        return ResponseEntity.ok(userToUserInfoDTO(userOptional.get()));
    }
}
