package org.library.api.mapper;

import org.library.api.security.LibraryUserDetails;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDetailsMapper {

    @Mapping(target = "authorities", source = "userRole", qualifiedByName = "mapRolesToAuthorities")
    LibraryUserDetails mapFromUser(User user);

    @Named("mapRolesToAuthorities")
    default List<GrantedAuthority> mapRolesToAuthorities(UserRole userRole) {
        return List.of(new SimpleGrantedAuthority(userRole.getValue()));
    }
}
