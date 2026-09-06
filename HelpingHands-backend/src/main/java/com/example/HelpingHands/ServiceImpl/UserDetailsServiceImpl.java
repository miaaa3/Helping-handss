package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity =userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("USER  NOT FOUND"));
        return new User(userEntity.getEmail(), userEntity.getPassword(), userEntity.getAuthorities());
    }
}