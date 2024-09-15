package com.unicamp.urbcrowd.controllers;

import com.unicamp.urbcrowd.controllers.dto.CreateUserDTO;
import com.unicamp.urbcrowd.models.Role;
import com.unicamp.urbcrowd.models.User;
import com.unicamp.urbcrowd.repositories.RoleRepository;
import com.unicamp.urbcrowd.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<String> create(@RequestBody CreateUserDTO createUserDTO) throws BadRequestException {

        if(this.userRepository.findByUsernameOrEmail(createUserDTO.username(), createUserDTO.email()).isPresent())
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Username or email already exists.");

        var role = roleRepository.findByName(Role.Values.DEFAULT.name());

        User newUser = this.userRepository.save(User.builder()
                .name(createUserDTO.name())
                .username(createUserDTO.username())
                .email(createUserDTO.email())
                .roles(Set.of(role))
                .password(bCryptPasswordEncoder.encode(createUserDTO.password())).build());

        return new ResponseEntity<>(newUser.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> findAll(){

        return ResponseEntity.ok(this.userRepository.findAll());
    }
}
