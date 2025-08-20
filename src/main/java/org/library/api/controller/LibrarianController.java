package org.library.api.controller;

import lombok.AllArgsConstructor;
import org.library.api.dto.LibrarianDTO;
import org.library.api.mapper.LibrarianDTOMapper;
import org.library.api.security.LibraryUserDetails;
import org.library.domain.model.Librarian;
import org.library.domain.service.LibrarianService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.USER)
@AllArgsConstructor
public class LibrarianController {

    static final String LIBRARIAN = "me/librarian";

    private final LibrarianService librarianService;
    private final LibrarianDTOMapper mapper;

    @GetMapping(LIBRARIAN)
    @PreAuthorize("hasRole('LIBRARIAN')")
    public LibrarianDTO getLoggerLibrarian(@AuthenticationPrincipal LibraryUserDetails userDetails) {
        Librarian librarian = librarianService.findByUserId(userDetails.userId());
        System.out.println("###Authorities for user5: " + userDetails.getAuthorities());
        return mapper.mapToDto(librarian);
    }

}
