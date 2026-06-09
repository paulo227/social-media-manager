package com.paulofelipe.socialmediamanager.presentation.controller;

import com.paulofelipe.socialmediamanager.application.usecase.CreateUserUseCase;
import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.presentation.dto.UserRequestDTO;
import com.paulofelipe.socialmediamanager.presentation.dto.UserResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public UserResponseDTO create(@RequestBody UserRequestDTO dto) {

        User user = new User(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getPassword()
        );

        User saved = createUserUseCase.execute(user);

        return new UserResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }
}
