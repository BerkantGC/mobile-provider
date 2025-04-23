package com.mobileprovider.api.service;

import com.mobileprovider.api.dto.SigninDTO;
import com.mobileprovider.api.dto.SigninResponse;
import com.mobileprovider.api.dto.SignupDTO;
import com.mobileprovider.api.model.User;
import com.mobileprovider.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void signup (SignupDTO signupDTO) {
        if (userRepository.findByUsername(signupDTO.getUsername().toLowerCase()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        User user = new User();
        user.setEmail(signupDTO.getEmail());
        user.setUsername(signupDTO.getUsername());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setFullname(signupDTO.getFirst_name() + " " + signupDTO.getLast_name());
        user.setRole("USER");
        userRepository.save(user);
    }

    public SigninResponse signin (SigninDTO signinDTO) {
        User user = userRepository.findByUsername(signinDTO.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        if (!passwordEncoder.matches(signinDTO.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect credentials");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            signinDTO.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }


        var jwtToken = jwtService.generateToken(user);

        return SigninResponse.builder()
                .access_token(jwtToken)
                .username(user.getUsername())
                .build();
    }
}
