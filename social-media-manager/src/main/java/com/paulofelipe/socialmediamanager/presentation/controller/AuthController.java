package com.paulofelipe.socialmediamanager.presentation.controller;

import com.paulofelipe.socialmediamanager.application.usecase.AuthenticateUserUseCase;
import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.infrastructure.security.JwtTokenProvider;
import com.paulofelipe.socialmediamanager.presentation.dto.LoginRequestDTO;
import com.paulofelipe.socialmediamanager.presentation.dto.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Retorna um token JWT")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        User user = authenticateUserUseCase.execute(dto.getEmail(), dto.getPassword());
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
        return new LoginResponseDTO(token, user.getId(), user.getName(), user.getEmail());
    }
}
