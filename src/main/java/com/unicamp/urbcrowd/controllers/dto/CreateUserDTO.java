package com.unicamp.urbcrowd.controllers.dto;

public record CreateUserDTO (
        String name,
        String username,
        String email,
        String password
){
}
