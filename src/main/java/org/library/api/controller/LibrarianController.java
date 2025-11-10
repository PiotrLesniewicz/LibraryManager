package org.library.api.controller;

import lombok.AllArgsConstructor;
import org.library.api.dto.AdminRegistrationRequestDTO;
import org.library.api.dto.AdminUpdateUserDTO;
import org.library.api.dto.UserDTO;
import org.library.api.mapper.UserDTOMapper;
import org.library.api.security.annotation.IsAdmin;
import org.library.api.security.annotation.IsLibrarian;
import org.library.domain.model.User;
import org.library.domain.service.AccountUserService;
import org.library.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@IsLibrarian
@RestController
@RequestMapping(ApiPaths.ADMIN)
@AllArgsConstructor
public class LibrarianController {

    private final AccountUserService accountUserService;
    private final UserService userService;
    private final UserDTOMapper mapper;

    @PostMapping(ApiPaths.USERS)
    @IsAdmin
    public ResponseEntity<UserDTO> createAccountByAdmin(@RequestBody AdminRegistrationRequestDTO registrationRequestDTO) {
        User user = mapper.mapFromDto(registrationRequestDTO);
        User savedUser = accountUserService.createAccountUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.mapToDto(savedUser));
    }

    @IsAdmin
    @PatchMapping(ApiPaths.RELATIVE_USER_BY_IDENTIFIER)
    public UserDTO updateUserInfo(@PathVariable("identifier") String emailOrUserName,
                                  @RequestBody AdminUpdateUserDTO userDTO) {
        User update = mapper.mapFromDto(userDTO);
        User updatedUser = accountUserService.updateAccountUser(update, emailOrUserName);
        return mapper.mapToDto(updatedUser);
    }

    @IsAdmin
    @DeleteMapping(ApiPaths.RELATIVE_USER_BY_IDENTIFIER)
    public ResponseEntity<Void> deleteUser(@PathVariable("identifier") String emailOrUserName) {
        accountUserService.deleteAccountUser(emailOrUserName);
        return ResponseEntity.noContent().build();
    }

    @IsAdmin
    @DeleteMapping(ApiPaths.REMOVE_LIBRARIAN_ROLE)
    public ResponseEntity<Void> removeLibrarian(@PathVariable("identifier") String emailOrUserName) {
        accountUserService.removeLibrarian(emailOrUserName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(ApiPaths.RELATIVE_USER_BY_IDENTIFIER)
    public UserDTO getUserInfo(@PathVariable("identifier") String emailOrUserName) {
        User user = userService.findUserByEmailOrUserName(emailOrUserName);
        return mapper.mapToDto(user);
    }

}
