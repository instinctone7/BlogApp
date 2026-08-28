package com.InstinctOne.BlogApp.controllers;

import com.InstinctOne.BlogApp.dtos.LoginRequest;
import com.InstinctOne.BlogApp.dtos.RegisterVerify;
import com.InstinctOne.BlogApp.dtos.UserDto;
import com.InstinctOne.BlogApp.dtos.UserDtoRequest;
import com.InstinctOne.BlogApp.services.TokenService;
import com.InstinctOne.BlogApp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterVerify> registerUser(@RequestBody @Valid UserDtoRequest request){
        UserDto registration = userService.registration(request);
        RegisterVerify response =  tokenService.createLink(registration);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody LoginRequest request){
        String jwtToken = userService.signinIn(request.email(), request.password());
        return ResponseEntity.ok(jwtToken);
    }
    @GetMapping("/verification")
    public ResponseEntity<UserDto> verification(@RequestParam String token){
        UserDto res = tokenService.verification(token);
        return ResponseEntity.accepted().body(res);
    }
    @GetMapping("/hello")
    public ResponseEntity<String> verification(){
        return ResponseEntity.ok("Shit went well");
    }
}
