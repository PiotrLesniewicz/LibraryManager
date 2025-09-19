package org.library.integration.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.library.api.controller.UserController;
import org.library.api.dto.UserDTO;
import org.library.configuration.TestContainerConfig;
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
import java.util.HashMap;
import java.util.Map;

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

    private static final String USER_PROFILE_ENDPOINT = UserController.USER + UserController.USER_PROFILE;

    @ParameterizedTest
    @ValueSource(strings = {"GET", "PATCH"})
    void shouldReturnUnauthorized_WhenNotAuthenticated(String httpMethod) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.valueOf(httpMethod),USER_PROFILE_ENDPOINT))
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

    @Test
    @WithUserDetails(value = "user7", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldPatchCurrentUserSuccessfully_WhenAuthenticated() throws Exception {

        ObjectMapper testMapper = new ObjectMapper();
        testMapper.registerModule(new JavaTimeModule());
        testMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        String request = testMapper.writeValueAsString(updateUser());
        String expected = testMapper.writeValueAsString(returnPatchUser());
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_PROFILE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
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
                .phoneNumber("1234567894")
                .membershipDate(LocalDate.of(2025,3,18))
                .build();
    }
    private Map<String,Object> updateUser() {
        Map<String,Object> map = new HashMap<>();
        map.put("userName","newUser7");
        map.put("email","newuser7@gmail.com");
        map.put("phoneNumber",null);
        return map;
    }

    private UserDTO returnPatchUser() {
        return new UserDTO(
                "newUser7",
                "Emily",
                "Miller",
                "newuser7@gmail.com",
                null,
                LocalDate.of(2025,3,18));

    }
}
