package org.library.api.controller;

import lombok.AllArgsConstructor;
import org.library.api.dto.UserDTO;
import org.library.api.mapper.UserDTOMapper;
import org.library.api.security.LibraryUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.USER)
@AllArgsConstructor
class UserController {

    static final String USER = "/library/user";
    static final String INFO_USER_NAME = "/me";

    private final UserDTOMapper userDTOMapper;

    @GetMapping(INFO_USER_NAME)
    public UserDTO getLoggerUserInfo(@AuthenticationPrincipal LibraryUserDetails userDetails) {
        return userDTOMapper.mapToDTO(userDetails);
    }
}
