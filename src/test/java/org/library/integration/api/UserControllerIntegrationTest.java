package org.library.integration.api;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.library.configuration.TestContainerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AllArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application-test.yaml")
class UserControllerIntegrationTest extends TestContainerConfig {

    private MockMvc mockMvc;

    @Test
    void shouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/library/user/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "user5", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnUserInfo_WhenAuthenticated() throws Exception {
        String jsonExpected = """
                {
                "userName":"user5",
                "name":"Charlie",
                "surname":"Brown",
                "email":"charlie.brown5@example.com",
                "phoneNumber":"1234567894",
                "membershipDate":"2025-03-18"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.get("/library/user/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonExpected)
                );
    }
}
