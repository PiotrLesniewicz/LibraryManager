package org.library.domain.service;

import lombok.AllArgsConstructor;
import org.library.domain.exception.NotFoundUserException;
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

    @Transactional
    public void deleteUser(Integer userId) {
        findUserById(userId);
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntityMapper::mapFromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .map(userEntityMapper::mapFromEntity);
    }

    @Transactional(readOnly = true)
    public User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .map(userEntityMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundUserException("User with userId: [%s] does not exist".formatted(userId)));
    }

    @Transactional(readOnly = true)
    public Long countUser() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public int findCountUsersForAddress(Integer addressId) {
        return userRepository.findCountUsersForAddress(addressId);
    }
}
