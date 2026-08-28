package com.InstinctOne.BlogApp.services;

import com.InstinctOne.BlogApp.dtos.UserDto;
import com.InstinctOne.BlogApp.dtos.UserDtoRequest;
import com.InstinctOne.BlogApp.entities.User;
import com.InstinctOne.BlogApp.exceptions.UserNotFound;
import com.InstinctOne.BlogApp.mappers.MapDtos;
import com.InstinctOne.BlogApp.repositories.UserRepository;
import com.InstinctOne.BlogApp.security.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MapDtos mapper;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, MapDtos mapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public UserDto registration(UserDtoRequest request) {
            User user = new User();
            user.setEmail(request.email());
            user.setName(request.name());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setIsVerified(false);
            userRepository.save(user);
            return
                    mapper.mapUserToDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null){
            throw new UserNotFound("User Not Found");
        }
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(!user.getIsVerified())
                .build();
    }

    public String signinIn(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if (user == null){
            throw new UserNotFound("User Not Found");
        }
        if (!user.getIsVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Password Doesn't Match Incorrect");
        }
        return JwtUtil.generateToken(user);
    }
}
