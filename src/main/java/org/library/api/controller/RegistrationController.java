package org.library.api.controller;

import lombok.AllArgsConstructor;
import org.library.api.dto.AdminRegistrationRequestDTO;
import org.library.api.dto.RegistrationRequestDTO;
import org.library.api.dto.RegistrationResponseDTO;
import org.library.api.mapper.RegistrationMapper;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.library.domain.service.AccountUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private static final String USER_CREATE_ACCOUNT = "/library/user/account";
    private static final String LIBRARIAN_CREATE_ACCOUNT = "library/librarian/account";
    private final AccountUserService accountUserService;
    private final RegistrationMapper registrationMapper;

    @PostMapping(USER_CREATE_ACCOUNT)
    public ResponseEntity<RegistrationResponseDTO> createAccount(@RequestBody RegistrationRequestDTO registrationRequestDTO) {
        User user = registrationMapper.mapFromDto(registrationRequestDTO)
                .withUserRole(UserRole.USER);
        User savedUser = accountUserService.accountUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrationMapper.mapToDto(savedUser));
    }

    @PostMapping(LIBRARIAN_CREATE_ACCOUNT)
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<RegistrationResponseDTO> createAccount(@RequestBody AdminRegistrationRequestDTO registrationRequestDTO) {
        User user = registrationMapper.mapFromDto(registrationRequestDTO);
        User savedUser = accountUserService.accountUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrationMapper.mapToDto(savedUser));
    }
}
