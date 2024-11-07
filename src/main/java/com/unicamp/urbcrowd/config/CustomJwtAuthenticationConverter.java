package com.unicamp.urbcrowd.config;

import com.unicamp.urbcrowd.models.Role;
import com.unicamp.urbcrowd.models.User;
import com.unicamp.urbcrowd.repositories.RoleRepository;
import com.unicamp.urbcrowd.repositories.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public CustomJwtAuthenticationConverter(UserRepository userRepository,
                                            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);

        String email = jwt.getClaim("email");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            var role = roleRepository.findByName(Role.Values.DEFAULT.name());
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            User user = User.builder()
                    .name(jwt.getClaim("given_name"))
                    .username(email.substring(0, email.indexOf("@")))
                    .email(email)
                    .roles(Set.of(role))
                    .picture(jwt.getClaim("picture"))
                    .password(bCryptPasswordEncoder.encode(jwt.getClaim("sub")))
                    .build();
            userRepository.save(user);
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}