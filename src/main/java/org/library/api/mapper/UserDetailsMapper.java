package org.library.api.mapper;

import org.library.api.security.LibraryUserDetails;
import org.library.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDetailsMapper {

    @Mapping(target = "authorities", source = "user", qualifiedByName = "mapRolesToAuthorities")
    LibraryUserDetails mapFromUser(User user);

    @Named("mapRolesToAuthorities")
    default List<GrantedAuthority> mapRolesToAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        String prefix_role = "ROLE_";
        authorities.add(new SimpleGrantedAuthority(prefix_role + user.getUserRole().name()));
        if (Objects.nonNull(user.getLibrarian())){
            String librarianRole = user.getLibrarian().getLibrarianRole().name();
            authorities.add(new SimpleGrantedAuthority(prefix_role + librarianRole));
        }
        return authorities;
    }
}
