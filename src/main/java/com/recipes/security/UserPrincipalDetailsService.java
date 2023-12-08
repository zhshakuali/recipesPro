package com.recipes.security;

import com.recipes.entity.UserEntity;
import com.recipes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername");
        UserEntity user = this.userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("Username: " + s + " not found"));
        return new UserPrincipal(user);
    }
}
