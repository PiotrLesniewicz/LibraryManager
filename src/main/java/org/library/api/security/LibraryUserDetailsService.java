package org.library.api.security;

import lombok.AllArgsConstructor;
import org.library.api.mapper.UserDetailsMapper;
import org.library.domain.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LibraryUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public LibraryUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserByUserName(username)
                .map(userDetailsMapper::mapFromUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
