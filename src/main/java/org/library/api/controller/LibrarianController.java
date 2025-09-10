package org.library.api.controller;

import lombok.AllArgsConstructor;
import org.library.api.dto.LibrarianDTO;
import org.library.api.mapper.LibrarianDTOMapper;
import org.library.api.security.LibraryUserDetails;
import org.library.api.security.annotation.IsLibrarian;
import org.library.domain.model.Librarian;
import org.library.domain.service.LibrarianService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.USER)
@AllArgsConstructor
public class LibrarianController {

    static final String LIBRARIAN_INFO = "me/librarian";

    private final LibrarianService librarianService;
    private final LibrarianDTOMapper mapper;

    @IsLibrarian
    @GetMapping(LIBRARIAN_INFO)
    public LibrarianDTO getLoggerLibrarian(@AuthenticationPrincipal LibraryUserDetails userDetails) {
        Librarian librarian = librarianService.findByUserId(userDetails.userId());
        return mapper.mapToDto(librarian);
    }
}
