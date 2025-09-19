package org.library.api.controller;

import lombok.AllArgsConstructor;
import org.library.api.dto.UserDTO;
import org.library.api.mapper.UserDTOMapper;
import org.library.api.security.LibraryUserDetails;
import org.library.domain.model.User;
import org.library.domain.service.AccountUserService;
import org.library.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(UserController.USER)
@AllArgsConstructor
public class UserController {

    public static final String USER = "/library/user";
    public static final String USER_PROFILE = "/profile";

    private final UserService userService;
    private final AccountUserService accountUserService;
    private final UserDTOMapper userDTOMapper;

    @GetMapping(USER_PROFILE)
    public UserDTO getLoggerUserInfo(@AuthenticationPrincipal LibraryUserDetails userDetails) {
        User user = userService.findById(userDetails.userId());
        return userDTOMapper.mapToDTO(user);
    }

    @PatchMapping(USER_PROFILE)
    public ResponseEntity<?> patchCurrentUser(@AuthenticationPrincipal LibraryUserDetails userDetails, @RequestBody Map<String, Object> update) {
        User user = userService.findById(userDetails.userId());
        User userToSave = updateUserWithPatch(user, update);
        User savedUser = accountUserService.accountUser(userToSave);
        UserDTO body = userDTOMapper.mapToDTO(savedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    private User updateUserWithPatch(User user, Map<String, Object> update) {
        return user.toBuilder()
                .userName(getOrDefault(update, "userName", user.getUserName()))
                .name(getOrDefault(update, "name", user.getName()))
                .surname(getOrDefault(update, "surname", user.getSurname()))
                .email(getOrDefault(update, "email", user.getEmail()))
                .phoneNumber(getOrDefault(update, "phoneNumber", user.getPhoneNumber()))
                .build();
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrDefault(Map<String, Object> update, String key, T currentValue) {
        if ("phoneNumber".equals(key) && update.containsKey(key)) {
            return (T) (update.get(key));
        }
        return update.containsKey(key) ? (T) update.get(key) : currentValue;
    }
}
