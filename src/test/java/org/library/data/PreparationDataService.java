package org.library.data;

import org.library.domain.model.Address;
import org.library.domain.model.Librarian;
import org.library.domain.model.Role;
import org.library.domain.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PreparationDataService {


    public static User someUser1() {
        return User.builder()
                .userName("PauPali")
                .name("Paul")
                .surname("Skorore")
                .email("emkr@gmail.com")
                .password("123456789")
                .membershipDate(LocalDate.now())
                .role(Role.USER)
                .address(
                        Address.builder()
                                .city("London")
                                .street("McJordan")
                                .number("16/22")
                                .postCode("22-256")
                                .build()
                )
                .build();
    }

    public static User someUser2() {
        return User.builder()
                .userName("djMasterPL2010888")
                .name("Dawid")
                .surname("Jagiełło")
                .email("buziaczek@wp.pl")
                .phoneNumber("+48 700-700-700")
                .password("mocnehasło")
                .membershipDate(LocalDate.now())
                .role(Role.USER)
                .address(
                        Address.builder()
                                .city("Sosnowiec")
                                .street("Śląska")
                                .number("8")
                                .postCode("22-200")
                                .build()
                )
                .build();
    }

    public static User someUser3() {
        return User.builder()
                .userName("LidiaMasterSheff")
                .name("Lidia")
                .surname("Jaskola")
                .email("jasLid2015@onet.pl")
                .password("999999999")
                .membershipDate(LocalDate.now())
                .role(Role.USER)
                .address(
                        Address.builder()
                                .city("Warszawa")
                                .street("Wiejska")
                                .number("7/33")
                                .postCode("00-800")
                                .build()
                )
                .build();
    }
    public static User someUser4() {
        return User.builder()
                .userName("MokoBoko")
                .name("Lidia")
                .surname("Nowak")
                .email("ruda256545@onet.pl")
                .password("15452455")
                .membershipDate(LocalDate.now())
                .role(Role.USER)
                .address(
                        Address.builder()
                                .city("Warszawa")
                                .street("Wiejska")
                                .number("7/33")
                                .postCode("00-800")
                                .build()
                )
                .build();
    }

    public static Address someAddress1() {
        return Address.builder()
                .city("someCity")
                .street("someStreet")
                .number("1")
                .postCode("00-000")
                .build();
    }

    public static Address someAddress2() {
        return Address.builder()
                .city("someCity")
                .street("someStreet")
                .number("1")
                .postCode("00-000")
                .build();
    }

    public static Librarian someLibrarian1() {
        return Librarian.builder()
                .role(Role.ADMIN)
                .hireDate(LocalDate.of(2020,2,11))
                .build();
    }

    public static Librarian someLibrarian2() {
        return Librarian.builder()
                .role(Role.TECHNIC)
                .hireDate(LocalDate.of(2021,12,1))
                .build();
    }
}
