package com.unicamp.urbcrowd.services;

import com.unicamp.urbcrowd.controllers.dto.CreateUserRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.UserInfoResponseDTO;
import com.unicamp.urbcrowd.exceptions.BusinessException;
import com.unicamp.urbcrowd.models.Role;
import com.unicamp.urbcrowd.models.User;
import com.unicamp.urbcrowd.repositories.RoleRepository;
import com.unicamp.urbcrowd.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.unicamp.urbcrowd.controllers.mapper.UserInfoMapper.userToUserInfoDTO;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       AuthService authService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authService = authService;
    }

    public UserInfoResponseDTO getUserInfo() throws AccountNotFoundException {
        Authentication authentication = authService.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new AccountNotFoundException("User not found in database.");
        }
        return userToUserInfoDTO(userOptional.get());
    }

    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    public UserInfoResponseDTO create(CreateUserRequestDTO createUserRequestDTO){
        if (this.userRepository.findByUsernameOrEmail(createUserRequestDTO.username(), createUserRequestDTO.email()).isPresent())
            throw new BusinessException(HttpStatus.PRECONDITION_FAILED, "Username or email already exists.");

        var role = roleRepository.findByName(Role.Values.DEFAULT.name());

        User newUser = this.userRepository.save(User.builder()
                .name(createUserRequestDTO.name())
                .username(createUserRequestDTO.username())
                .email(createUserRequestDTO.email())
                .roles(Set.of(role))
                .password(bCryptPasswordEncoder.encode(createUserRequestDTO.password())).build());
        return userToUserInfoDTO(newUser);
    }
}
