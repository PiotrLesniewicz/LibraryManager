package org.library.domain.service;

import lombok.AllArgsConstructor;
import org.library.domain.model.User;
import org.library.infrastructure.database.entity.UserEntity;
import org.library.infrastructure.database.repository.UserRepository;
import org.library.mapper.UserEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Transactional
    public User saveUser(User user) {
        UserEntity toSave = userEntityMapper.mapToEntity(user);
        UserEntity userEntity = userRepository.saveAndFlush(toSave);
        return userEntityMapper.mapFromEntity(userEntity);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntityMapper::mapFromEntity);
    }

    public Long countUser() {
        return userRepository.count();
    }
}
