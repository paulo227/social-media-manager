package com.paulofelipe.socialmediamanager.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulofelipe.socialmediamanager.application.usecase.CreateUserUseCase;
import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.infrastructure.security.SecurityConfig;
import com.paulofelipe.socialmediamanager.presentation.dto.UserRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @Test
    void deveCriarUsuarioRetornandoStatus200() throws Exception {
        UserRequestDTO request = new UserRequestDTO("Paulo", "paulo@email.com", "123456");
        User savedUser = new User(1L, "Paulo", "paulo@email.com", "123456");

        when(createUserUseCase.execute(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Paulo"))
                .andExpect(jsonPath("$.email").value("paulo@email.com"));
    }
}
