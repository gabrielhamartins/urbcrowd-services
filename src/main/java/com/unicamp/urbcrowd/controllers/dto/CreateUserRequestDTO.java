package com.unicamp.urbcrowd.controllers.dto;

public record CreateUserRequestDTO(
        String name,
        String username,
        String email,
        String password
){
}
