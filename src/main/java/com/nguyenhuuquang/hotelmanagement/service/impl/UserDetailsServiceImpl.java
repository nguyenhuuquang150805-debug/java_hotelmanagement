package com.nguyenhuuquang.hotelmanagement.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nguyenhuuquang.hotelmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                log.debug("Loading user by email: {}", email);

                com.nguyenhuuquang.hotelmanagement.entity.User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> {
                                        log.error("User not found with email: {}", email);
                                        return new UsernameNotFoundException("User not found with email: " + email);
                                });

                log.debug("User found - email: {}, role: {}", user.getEmail(), user.getRole());

                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                                .username(user.getEmail())
                                .password(user.getPassword())
                                .roles(user.getRole())
                                .build();

                log.info("UserDetails created - username: {}, authorities: {}",
                                userDetails.getUsername(), userDetails.getAuthorities());

                return userDetails;
        }
}