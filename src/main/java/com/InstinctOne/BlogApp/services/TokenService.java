    package com.InstinctOne.BlogApp.services;

    import com.InstinctOne.BlogApp.dtos.RegisterVerify;
    import com.InstinctOne.BlogApp.dtos.UserDto;
    import com.InstinctOne.BlogApp.entities.User;
    import com.InstinctOne.BlogApp.entities.UserToken;
    import com.InstinctOne.BlogApp.exceptions.UserNotFound;
    import com.InstinctOne.BlogApp.mappers.MapDtos;
    import com.InstinctOne.BlogApp.repositories.TokenRepository;
    import com.InstinctOne.BlogApp.repositories.UserRepository;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.time.temporal.ChronoUnit;
    import java.util.UUID;

    @Service
    public class TokenService {

        private final TokenRepository tokenRepository;
        private final UserRepository userRepository;
        private final MapDtos mapDtos;

        public TokenService(TokenRepository tokenRepository, UserRepository userRepository, MapDtos mapDtos) {
            this.tokenRepository = tokenRepository;
            this.userRepository = userRepository;
            this.mapDtos = mapDtos;
        }

        public RegisterVerify createLink(UserDto registration) {
            User user = userRepository.findById(registration.id())
                    .orElseThrow(
                            () -> new UserNotFound("User Id "+registration.id()+" is not found")
                    );
            UserToken userToken = new UserToken();
            String token = UUID.randomUUID().toString();
            userToken.setToken(token);
            userToken.setUserid(user);
            userToken.setExpireAt(LocalDateTime.now().plusHours(8));
            tokenRepository.save(userToken);
            String url = "http://localhost:8080/api/auth/verification?token="+token;
            return new RegisterVerify("Registration is successful , Please verify!!!",
                    url);
        }

        public UserDto verification(String token) {
            UserToken userToken = tokenRepository.findByToken(token);
            if (userToken == null){
                throw new UsernameNotFoundException("Token not found");
            }
            User user = userRepository.findById(userToken.getUserid().getId())
                    .orElseThrow( ()-> new UserNotFound("Not Found"));
            if (userToken.getExpireAt().isBefore(LocalDateTime.now())){
                    userRepository.delete(user);
                    tokenRepository.delete(userToken);
                throw new RuntimeException("Token expired register again");
            }
            user.setIsVerified(true);
            userRepository.save(user);
            tokenRepository.delete(userToken);
            return mapDtos.mapUserToDto(user);
        }
    }
