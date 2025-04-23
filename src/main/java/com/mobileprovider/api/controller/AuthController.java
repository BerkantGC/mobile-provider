package com.mobileprovider.api.controller;

import com.mobileprovider.api.dto.SigninDTO;
import com.mobileprovider.api.dto.SigninResponse;
import com.mobileprovider.api.dto.SignupDTO;
import com.mobileprovider.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Authentication", description = "API for authentication operations")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Sign new user up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User's signed up"),
            @ApiResponse(responseCode = "401", description = "User already taken")
    })
    public ResponseEntity<String> signup(@RequestBody SignupDTO signupDTO) {
        log.info("SignupDTO: {}", signupDTO);
        authService.signup(signupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign user in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "200", description = "Successfull login")
    })
    public ResponseEntity<SigninResponse> signin(@RequestBody SigninDTO signinDTO) {
        SigninResponse response = authService.signin(signinDTO);
        log.info("response: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
