package org.library.api.controller;

import lombok.AllArgsConstructor;
import org.library.api.dto.RegistrationRequestDTO;
import org.library.api.dto.UpdateUserDTO;
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

@RestController
@RequestMapping(ApiPaths.USERS)
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountUserService accountUserService;
    private final UserDTOMapper mapper;

    @PostMapping
    public ResponseEntity<UserDTO> createAccount(@RequestBody RegistrationRequestDTO registrationRequestDTO) {
        User user = mapper.mapFromDto(registrationRequestDTO);
        User savedUser = accountUserService.createAccountUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.mapToDto(savedUser));
    }

    @GetMapping(ApiPaths.PROFILE)
    public UserDTO getLoggerUserInfo(@AuthenticationPrincipal LibraryUserDetails userDetails) {
        User user = userService.findByIdWithDetails(userDetails.userId());
        return mapper.mapToDto(user);
    }

    @PatchMapping(ApiPaths.PROFILE)
    public UserDTO updateLoggedUserInfo(@AuthenticationPrincipal LibraryUserDetails userDetails,
                                        @RequestBody UpdateUserDTO userDTO) {
        User userToUpdate = mapper.mapFromDto(userDTO);
        User updatedUser = accountUserService.updateAccountUser(userToUpdate.withUserId(userDetails.userId()));
        return mapper.mapToDto(updatedUser);
    }

    @DeleteMapping(ApiPaths.PROFILE)
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal LibraryUserDetails userDetails){
        accountUserService.deleteAccountUser(userDetails.userId());
        return ResponseEntity.noContent().build();
    }
}
