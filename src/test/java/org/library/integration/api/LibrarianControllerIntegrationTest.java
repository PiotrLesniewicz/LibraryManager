package org.library.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.library.api.controller.ApiPaths;
import org.library.api.dto.AdminRegistrationRequestDTO;
import org.library.api.dto.AdminUpdateUserDTO;
import org.library.api.dto.UserDTO;
import org.library.configuration.TestContainerConfig;
import org.library.domain.model.LibrarianRole;
import org.library.domain.model.User;
import org.library.domain.model.UserRole;
import org.library.domain.service.UserService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests in this class rely on predefined test data loaded from the Flyway migration file:
 * /resources/db/migration/V1_0__library_manager_insert_data_ddl.sql
 *
 * The database state is initialized automatically when the test context starts,
 * so the tests use the same initial dataset as the application during normal startup.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AllArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application-test.yaml")
class LibrarianControllerIntegrationTest extends TestContainerConfig {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserService userService;
    @MockitoBean
    private Clock clock;

    @Test
    @WithUserDetails(value = "user1", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnForbidden_WhenNonLibrarianAccessAdminEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiPaths.ADMIN_USERS + "/jankowalski")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // user 8 has TECHNIC librarianRole
    @Test
    @WithUserDetails(value = "user8", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnForbidden_WhenNonAdminAccessPostUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.ADMIN_USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "user8", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnForbidden_WhenNonAdminAccessPatchUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(ApiPaths.ADMIN_USERS + "/magda@magda.pl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "user8", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnForbidden_WhenNonAdminAccessDeleteUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ApiPaths.ADMIN_USERS + "/karol")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // user5 has ADMIN role
    @Test
    @WithUserDetails(value = "user5", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldCreateNewLibrarian() throws Exception {
        Instant fixInstant = Instant.parse("2020-10-03T12:00:00Z");
        Mockito.when(clock.instant()).thenReturn(fixInstant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.ADMIN_USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequestDTO())
                        ))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().json(objectMapper.writeValueAsString(expectedCreateResponse()))
                );
    }

    @Test
    @WithUserDetails(value = "user5", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldUpdateUser() throws Exception {
        Instant fixInstant = Instant.parse("2025-10-22T12:00:00Z");
        Mockito.when(clock.instant()).thenReturn(fixInstant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        String email = "rachel.allen20@example.com";

        mockMvc.perform(MockMvcRequestBuilders.patch(ApiPaths.ADMIN_USERS + "/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateLibrarian())
                        ))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(expectedUpdateResponse()))
                );
    }

    @Test
    @WithUserDetails(value = "user5", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldCorrectlyDeleteUser() throws Exception {
        String email = "mia.harris15@example.com";
        mockMvc.perform(MockMvcRequestBuilders.delete(ApiPaths.ADMIN_USERS + "/" + email))
                .andExpect(status().isNoContent());

        Assertions.assertThat(userService.findUserByEmail(email)).isEmpty();
    }

    @Test
    @WithUserDetails(value = "user5", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldRemoveLibrarianRole() throws Exception {
        String userName = "user12";
        mockMvc.perform(MockMvcRequestBuilders.delete(ApiPaths.ADMIN_USERS + "/" + userName + "/librarian"))
                .andExpect(status().isNoContent());

        User user = userService.findUserByEmailOrUserName(userName);
        Assertions.assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        Assertions.assertThat(user.getLibrarian()).isNull();
    }

    private AdminRegistrationRequestDTO registrationRequestDTO() {
        return new AdminRegistrationRequestDTO(
                UserRole.LIBRARIAN,
                LibrarianRole.TECHNIC,
                "securePassword",
                "jankowalski",
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com",
                "123-456-789",
                "Warszawa",
                "Marszałkowska",
                "10",
                "00-000"
        );
    }

    private AdminUpdateUserDTO updateLibrarian() {
        return new AdminUpdateUserDTO(
                UserRole.LIBRARIAN,
                LibrarianRole.TECHNIC,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private UserDTO expectedCreateResponse(){
        return new UserDTO(
                "jankowalski",
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com",
                "123-456-789",
                LocalDate.of(2020,10,3),
                new UserDTO.AddressDTO(
                        "Warszawa",
                        "Marszałkowska",
                        "10",
                        "00-000"
                ),
                new UserDTO.LibrarianDTO(
                        LibrarianRole.TECHNIC,
                        LocalDate.of(2020,10,3)
                )
        );
    }

    private UserDTO expectedUpdateResponse(){
        return new UserDTO(
                "user20",
                "Rachel",
                "Allen",
                "rachel.allen20@example.com",
                "1234567809",
                LocalDate.of(2025,3,18),
                new UserDTO.AddressDTO(
                        "City2",
                        "Street2",
                        "2B",
                        "23456"
                ),
                new UserDTO.LibrarianDTO(
                        LibrarianRole.TECHNIC,
                        LocalDate.of(2025,10,22)
                )
        );
    }
}
