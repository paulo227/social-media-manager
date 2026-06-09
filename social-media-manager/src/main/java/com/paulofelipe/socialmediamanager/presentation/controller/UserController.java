package com.paulofelipe.socialmediamanager.presentation.controller;

import com.paulofelipe.socialmediamanager.application.usecase.CreateUserUseCase;
import com.paulofelipe.socialmediamanager.application.usecase.DeleteUserUseCase;
import com.paulofelipe.socialmediamanager.application.usecase.GetUserUseCase;
import com.paulofelipe.socialmediamanager.application.usecase.ListUsersUseCase;
import com.paulofelipe.socialmediamanager.application.usecase.UpdateUserUseCase;
import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.presentation.dto.UpdateUserRequestDTO;
import com.paulofelipe.socialmediamanager.presentation.dto.UserRequestDTO;
import com.paulofelipe.socialmediamanager.presentation.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase,
                          GetUserUseCase getUserUseCase,
                          ListUsersUseCase listUsersUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping
    public UserResponseDTO create(@Valid @RequestBody UserRequestDTO dto) {

        User user = new User(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getPassword()
        );

        User saved = createUserUseCase.execute(user);

        return toResponse(saved);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable Long id) {
        return toResponse(getUserUseCase.execute(id));
    }

    @GetMapping
    public List<UserResponseDTO> list() {
        return listUsersUseCase.execute().stream()
                .map(this::toResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public UserResponseDTO update(@PathVariable Long id,
                                  @Valid @RequestBody UpdateUserRequestDTO dto) {
        return toResponse(updateUserUseCase.execute(id, dto.getName(), dto.getEmail()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
