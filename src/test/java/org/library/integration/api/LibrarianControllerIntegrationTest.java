package org.library.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.library.configuration.TestContainerConfig;
import org.library.domain.model.Librarian;
import org.library.domain.model.LibrarianRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class LibrarianControllerIntegrationTest extends TestContainerConfig {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/library/user/me/librarian"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "user1", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnForbidden_WhenNotDenied() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/library/user/me/librarian"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "user5", userDetailsServiceBeanName = "libraryUserDetailsService")
    void shouldReturnLibrarianInfo_whenAccess() throws Exception {

        String expected = objectMapper.writeValueAsString(buildLibrarian());

        mockMvc.perform(MockMvcRequestBuilders.get("/library/user/me/librarian")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(expected)
                );
    }

    private Librarian buildLibrarian() {
        return Librarian.builder()
                .librarianRole(LibrarianRole.ADMIN)
                .hireDate(LocalDate.of(2022,1,1))
                .build();
    }
}

