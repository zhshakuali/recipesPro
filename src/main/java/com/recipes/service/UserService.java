package com.recipes.service;

import com.recipes.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.recipes.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    public UserEntity findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    public String findRolesById(Integer id) {
        return userRepository.findRolesById(id);
    }

    public Page<UserEntity> findAllByUsernameNotLike(String username, Integer offset, Integer size) {
        Pageable page = PageRequest.of(offset, size);
        return userRepository.findAllByUsernameNotLike(username, page);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
        return User.withUsername(userEntity.getUsername()).password(userEntity.getPassword()).authorities("USER").build();
    }
}