package org.library.api.controller;

import lombok.AllArgsConstructor;
import org.library.api.dto.UserDTO;
import org.library.api.mapper.UserDTOMapper;
import org.library.api.security.LibraryUserDetails;
import org.library.domain.model.User;
import org.library.domain.service.AccountUserService;
import org.library.domain.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserController.USER)
@AllArgsConstructor
public class UserController {

    public static final String USER = "/users";
    public static final String PROFILE = "/profile";

    private final UserService userService;
    private final UserDTOMapper mapper;

    @GetMapping(PROFILE)
    public UserDTO getLoggerUserInfo(@AuthenticationPrincipal LibraryUserDetails userDetails) {
        User user = userService.findByIdWithDetails(userDetails.userId());
        return mapper.mapToDto(user);
    }
}
