package org.library.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.library.api.controller.UserController;
import org.library.configuration.TestContainerConfig;
import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.LibrarianRole;
import org.library.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

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

    private static final String USER_PROFILE_ENDPOINT = UserController.USER + UserController.PROFILE;

    @ParameterizedTest
    @ValueSource(strings = {"GET"})
    void shouldReturnUnauthorized_WhenNotAuthenticated(String httpMethod) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.valueOf(httpMethod), USER_PROFILE_ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "user5", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnUserInfo_WhenAuthenticated() throws Exception {

        String expected = objectMapper.writeValueAsString(returnGetUser());
        mockMvc.perform(MockMvcRequestBuilders.get(USER_PROFILE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(expected)
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

}
