package org.library.api.security;

import org.library.domain.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

public record LibraryUserDetails(
        Integer userId,
        String userName,
        String name,
        String surname,
        String email,
        String password,
        String phoneNumber,
        LocalDate membershipDate,
        UserRole userRole,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}
