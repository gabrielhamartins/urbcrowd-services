package com.unicamp.urbcrowd.models;

import com.unicamp.urbcrowd.controllers.dto.LoginRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(def = "{'username': 1, 'email': 1}", unique = true)
@Document
public class User {

    @Id
    private String id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;

    public boolean passwordMatches(LoginRequestDTO loginRequestDTO, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(loginRequestDTO.password(), this.password);
    }
}
