package com.unicamp.urbcrowd.controllers.mapper;

import com.unicamp.urbcrowd.controllers.dto.UserInfoResponseDTO;
import com.unicamp.urbcrowd.models.User;

public class UserInfoMapper {

    public static UserInfoResponseDTO userToUserInfoDTO(User user) {
        return new UserInfoResponseDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPicture());
    }
}
