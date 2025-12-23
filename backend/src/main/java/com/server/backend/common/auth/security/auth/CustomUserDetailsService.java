package com.server.backend.common.auth.security.auth;

import com.server.backend.common.data.entity.UserEntity;
import com.server.backend.common.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("### LoadUserByUsername Service");
        log.info("username = {}", username);

        UserEntity userEntity = userRepository.findByUserId(username);

        if(userEntity == null) {
            throw new UsernameNotFoundException("User Not Found: ");
        }

        return new CustomUserDetails(userEntity);
    }
}
