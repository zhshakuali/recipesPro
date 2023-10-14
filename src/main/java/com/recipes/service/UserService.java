package com.recipes.service;

import com.recipes.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.recipes.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity save(UserEntity userEntity){
        Optional<UserEntity> userSave = Optional.of(userRepository.findByUsername(userEntity.getUsername()).get());

        userRepository.save(userSave.get());
        return userSave.get();
    }

    public UserEntity findById(Integer id){
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.get();
    }
    public void deleteUserById(Integer id){
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(userName).get();
        return User.withUsername(userEntity.getUsername()).password(userEntity.getPassword()).authorities("USER").build();
    }
}