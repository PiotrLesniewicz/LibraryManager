package org.library.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.library.api.dto.*;
import org.library.configuration.TestContainerConfig;
import org.library.domain.model.LibrarianRole;
import org.library.domain.model.UserRole;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
public class RegistrationControllerTest extends TestContainerConfig {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @MockitoBean
    private Clock clock;

    @Test
    void shouldCorrectlyCreateNewUser_WhenCreateStandardUser() throws Exception {
        Instant fixInstant = Instant.parse("2020-10-03T12:00:00Z");
        Mockito.when(clock.instant()).thenReturn(fixInstant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        String jsonExpected = """
                {
                  "userDTO": {
                    "userName": "jankowalski",
                    "name": "Jan",
                    "surname": "Kowalski",
                    "email": "jan.kowalski@example.com",
                    "phoneNumber": "123-456-789",
                    "membershipDate":"2020-10-03"
                  },
                  "addressDTO": {
                    "city": "Warszawa",
                    "street": "Marszałkowska",
                    "number": "10",
                    "postCode": "00-000"
                  }
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/library/user/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDTO(fixInstant))))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().json(jsonExpected)
                );

    }

    @Test
    @WithMockUser(roles = {"LIBRARIAN"})
    void shouldCorrectlyCreateNewUser_WhenCreateLibrarianUser() throws Exception {
        Instant fixInstant = Instant.parse("2020-07-22T12:00:00Z");
        Mockito.when(clock.instant()).thenReturn(fixInstant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        String jsonExpected = """
                {
                  "userDTO": {
                    "userName": "andnow",
                    "name": "Andrzej",
                    "surname": "Nowak",
                    "email": "nowak@example.com",
                    "phoneNumber": "266-985-525",
                    "membershipDate":"2020-07-22"
                  },
                  "addressDTO": {
                    "city": "Rzeszów",
                    "street": "Warszawska",
                    "number": "54",
                    "postCode": "23-800"
                  },
                  "librarianDTO": {
                    "librarianRole": "ADMIN",
                    "hireDate": "2020-07-22"
                  }
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/library/librarian/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestLibrarianDTO(fixInstant))))
                .andDo(print())
                . andExpectAll(
                        status().isCreated(),
                        content().json(jsonExpected)
                );

    }

    private RegistrationRequestDTO requestUserDTO(Instant fixInstant) {
        LocalDate membership = LocalDate.ofInstant(fixInstant, ZoneId.systemDefault());
        return new RegistrationRequestDTO(
                "moje_haslo",
                new UserDTO(
                        "jankowalski",
                        "Jan",
                        "Kowalski",
                        "jan.kowalski@example.com",
                        "123-456-789",
                        membership
                ),
                new AddressDTO(
                        "Warszawa",
                        "Marszałkowska",
                        "10",
                        "00-000"
                )
        );
    }

    private AdminRegistrationRequestDTO requestLibrarianDTO(Instant fixInstant) {
        LocalDate membership = LocalDate.ofInstant(fixInstant, ZoneId.systemDefault());
        return new AdminRegistrationRequestDTO(
                "134**9124Ł",
                UserRole.LIBRARIAN,
                new UserDTO(
                        "andnow",
                        "Andrzej",
                        "Nowak",
                        "nowak@example.com",
                        "266-985-525",
                        membership
                ),
                new AddressDTO(
                        "Rzeszów",
                        "Warszawska",
                        "54",
                        "23-800"
                ),
                new LibrarianDTO(
                        LibrarianRole.ADMIN,
                        membership
                )
        );
    }
}
