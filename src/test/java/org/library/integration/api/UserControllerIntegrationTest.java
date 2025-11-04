package org.library.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.library.api.controller.ApiPaths;
import org.library.api.dto.RegistrationRequestDTO;
import org.library.api.dto.UpdateUserDTO;
import org.library.configuration.TestContainerConfig;
import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.LibrarianRole;
import org.library.domain.model.User;
import org.mockito.Mockito;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AllArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application-test.yaml")
class UserControllerIntegrationTest extends TestContainerConfig {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @MockitoBean
    private Clock clock;

    @Test
    void shouldCreateNewUser() throws Exception {
        Instant fixInstant = Instant.parse("2020-10-03T12:00:00Z");
        Mockito.when(clock.instant()).thenReturn(fixInstant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        String expected = """
                {
                    "userName": "jankowalski",
                    "name": "Jan",
                    "surname": "Kowalski",
                    "email": "jan.kowalski@example.com",
                    "phoneNumber": "123-456-789",
                    "membershipDate":[2020,10,03],
                  "address": {
                    "city": "Warszawa",
                    "street": "Marszałkowska",
                    "number": "10",
                    "postCode": "00-000"
                  }
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post(ApiPaths.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequestDTO())
                        ))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().json(expected)
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "PATCH"})
    void shouldReturnUnauthorized_WhenNotAuthenticated(String httpMethod) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.valueOf(httpMethod), ApiPaths.USER_PROFILE_ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "user5", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnUserInfo_WhenAuthenticated() throws Exception {

        String expected = objectMapper.writeValueAsString(returnGetUser());
        mockMvc.perform(MockMvcRequestBuilders.get(ApiPaths.USER_PROFILE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(expected)
                );
    }

    @Test
    @WithUserDetails(value = "user9", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnUpdateInfo_WhenAuthentication() throws Exception {
        String expected = objectMapper.writeValueAsString(returnUpdateUser());
        mockMvc.perform(MockMvcRequestBuilders.patch(ApiPaths.USER_PROFILE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser())))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(expected)
                );
    }

    @Test
    @WithUserDetails(value = "user20", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnUpdateInfo_WhenUserDeletePhoneNumber() throws Exception {
        String expected = objectMapper.writeValueAsString(returnUpdateUserDeletePhoneNumber());
        mockMvc.perform(MockMvcRequestBuilders.patch(ApiPaths.USER_PROFILE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDeletePhoneNumber())))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(expected)
                );
    }

    private RegistrationRequestDTO registrationRequestDTO() {
        return new RegistrationRequestDTO(
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

    private User returnGetUser() {
        return User.builder()
                .userName("user5")
                .name("Charlie")
                .surname("Brown")
                .email("charlie.brown5@example.com")
                .membershipDate(LocalDate.of(2025, 3, 18))
                .address(Address.builder()
                        .city("City4")
                        .street("Street4")
                        .number("4D")
                        .postCode("45678")
                        .build())
                .librarian(Librarian.builder()
                        .librarianRole(LibrarianRole.ADMIN)
                        .hireDate(LocalDate.of(2022, 1, 1))
                        .build())
                .build();
    }

    private UpdateUserDTO updateUser() {
        return new UpdateUserDTO(
                "newUserName",
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

    private UpdateUserDTO updateUserDeletePhoneNumber() {
        return new UpdateUserDTO(
                null,
                null,
                null,
                null,
                JsonNullable.of(null),
                null,
                null,
                null,
                null
        );
    }

    private User returnUpdateUser() {
        return User.builder()
                .userName("newUserName")
                .name("Grace")
                .surname("Moore")
                .email("grace.moore9@example.com")
                .phoneNumber("1234567898")
                .membershipDate(LocalDate.of(2025, 3, 18))
                .address(Address.builder()
                        .city("City5")
                        .street("Street5")
                        .number("5E")
                        .postCode("56789")
                        .build())
                .build();
    }

    private User returnUpdateUserDeletePhoneNumber() {
        return User.builder()
                .userName("user20")
                .name("Rachel")
                .surname("Allen")
                .email("rachel.allen20@example.com")
                .phoneNumber(null)
                .membershipDate(LocalDate.of(2025, 3, 18))
                .address(Address.builder()
                        .city("City2")
                        .street("Street2")
                        .number("2B")
                        .postCode("23456")
                        .build())
                .build();
    }
    }

