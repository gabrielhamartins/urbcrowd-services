package com.unicamp.urbcrowd.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.unicamp.urbcrowd.repositories.RoleRepository;
import com.unicamp.urbcrowd.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.private.key}")
    private RSAPrivateKey jwtPrivateKey;

    @Value("${jwt.public.key}")
    private RSAPublicKey jwtPublicKey;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public SecurityConfig(UserRepository userRepository,
                          RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(authenticationManagerResolver()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public JwtDecoder googleJwtDecoder() {
        String googleIssuer = "https://accounts.google.com";
        String googleJwkSetUri = "https://www.googleapis.com/oauth2/v3/certs";

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(googleJwkSetUri).build();
        OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefaultWithIssuer(googleIssuer);
        jwtDecoder.setJwtValidator(jwtValidator);
        return jwtDecoder;
    }

    //@Bean
    //public JwtIssuerAuthenticationManagerResolver authenticationManagerResolver() {
    //    Map<String, JwtDecoder> jwtDecoders = new HashMap<>();
    //    jwtDecoders.put("urbcrowd", jwtDecoder());
    //    jwtDecoders.put("https://accounts.google.com", googleJwtDecoder());
//
    //    return new JwtIssuerAuthenticationManagerResolver((issuer) -> {
    //        JwtDecoder jwtDecoder = jwtDecoders.get(issuer);
    //        if (jwtDecoder != null) {
    //            JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
    //            authenticationProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
    //            return authenticationProvider::authenticate;
    //        }
    //        throw new JwtException("Unknown issuer: " + issuer);
    //    });
    //}

    @Bean
    public CustomJwtAuthenticationConverter customJwtAuthenticationConverter() {
        return new CustomJwtAuthenticationConverter(userRepository,
                roleRepository);
    }

    @Bean
    public JwtIssuerAuthenticationManagerResolver authenticationManagerResolver() {
        Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

        JwtAuthenticationProvider internalProvider = new JwtAuthenticationProvider(jwtDecoder());
        internalProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        authenticationManagers.put("your-internal-issuer", internalProvider::authenticate);

        JwtAuthenticationProvider googleProvider = new JwtAuthenticationProvider(googleJwtDecoder());
        googleProvider.setJwtAuthenticationConverter(customJwtAuthenticationConverter());
        authenticationManagers.put("https://accounts.google.com", googleProvider::authenticate);

        return new JwtIssuerAuthenticationManagerResolver((issuer) -> {
            AuthenticationManager authenticationManager = authenticationManagers.get(issuer);
            if (authenticationManager == null) {
                throw new JwtException("Unknown issuer: " + issuer);
            }
            return authenticationManager;
        });
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return authenticationConverter;
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(this.jwtPublicKey).privateKey(this.jwtPrivateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(this.jwtPublicKey).build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
