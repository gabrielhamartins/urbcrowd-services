package com.unicamp.urbcrowd.config;

import com.unicamp.urbcrowd.models.Role;
import com.unicamp.urbcrowd.models.User;
import com.unicamp.urbcrowd.repositories.RoleRepository;
import com.unicamp.urbcrowd.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoaderConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DataLoaderConfig(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        this.roleRepository.save(Role.builder()
                .id(1L)
                .name(Role.Values.ADMIN.toString())
                .build());
        this.roleRepository.save(Role.builder()
                .id(2L)
                .name(Role.Values.DEFAULT.toString())
                .build());
        this.userRepository.save(User.builder()
                .id("66e517f5a6c75a030ee8349f")
                .email("urbcrowd@urbcrowd.com")
                .name("Urbcrowd Services")
                .username("urbcrowd")
                .roles(Set.of(Role.builder().name(Role.Values.ADMIN.toString()).build()))
                .password(bCryptPasswordEncoder.encode("urbcrowd")).build());

    }
}
