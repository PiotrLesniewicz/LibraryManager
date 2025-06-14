package org.library.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.domain.exception.NotFoundUserException;
import org.library.domain.service.UserService;
import org.library.infrastructure.database.repository.UserRepository;
import org.library.mapper.UserEntityMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserEntityMapper userEntityMapper;

    @Test
    void shouldThrowException_WhenNotFoundUserById() {
        // given
        Integer userId = 1;
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> userService.findUserById(userId))
                .isInstanceOf(NotFoundUserException.class)
                .hasMessage("User with userId: [%s] does not exist".formatted(userId));


    }
}
