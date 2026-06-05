package com.paulofelipe.socialmediamanager.presentation.controller;
import com.paulofelipe.socialmediamanager.application.usecase.CreateUserUseCase;
import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.presentation.dto.UseResquestDTO;
import com.paulofelipe.socialmediamanager.presentation.dto.DTO;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/users")
    public class UserController {

        private final CreateUserUseCase createUserUseCase;

        public UserController(CreateUserUseCase createUserUseCase) {
            this.createUserUseCase = createUserUseCase;
        }

        @PostMapping
        public DTO create(@RequestBody UseResquestDTO dto) {

            User user = new User(
                    null,
                    dto.getName(),
                    dto.getEmail(),
                    dto.getPassword()
            );

            User saved = createUserUseCase.execute(user);

            return new DTO(
                    saved.getId(),
                    saved.getName(),
                    saved.getEmail()
            );
        }
    }

