package com.example.loginauth.controller;

import com.example.loginauth.infra.security.TokenService;
import com.example.loginauth.model.dto.LoginRequestDTO;
import com.example.loginauth.model.dto.RegisterRequestDTO;
import com.example.loginauth.model.dto.ResponseDTO;
import com.example.loginauth.model.entity.UserEntity;
import com.example.loginauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        UserEntity user = this.repository.findByEmail(body.email())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(passwordEncoder.matches(user.getPassword(), body.password())){
            String token = this.tokenService.generateToken(user);

            return ResponseEntity.ok(new ResponseDTO(user.getName(),token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<UserEntity> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()){
            UserEntity newUser = new UserEntity();

            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());

            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(),token));
        }
        return ResponseEntity.badRequest().build();
    }
}
