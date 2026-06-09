package com.paulofelipe.socialmediamanager.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulofelipe.socialmediamanager.application.usecase.CreateUserUseCase;
import com.paulofelipe.socialmediamanager.application.usecase.DeleteUserUseCase;
import com.paulofelipe.socialmediamanager.application.usecase.GetUserUseCase;
import com.paulofelipe.socialmediamanager.application.usecase.ListUsersUseCase;
import com.paulofelipe.socialmediamanager.application.usecase.UpdateUserUseCase;
import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.domain.exception.UserNotFoundException;
import com.paulofelipe.socialmediamanager.presentation.dto.UpdateUserRequestDTO;
import com.paulofelipe.socialmediamanager.presentation.dto.UserRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private GetUserUseCase getUserUseCase;

    @MockitoBean
    private ListUsersUseCase listUsersUseCase;

    @MockitoBean
    private UpdateUserUseCase updateUserUseCase;

    @MockitoBean
    private DeleteUserUseCase deleteUserUseCase;

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

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        User user = new User(1L, "Paulo", "paulo@email.com", "123456");

        when(getUserUseCase.execute(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Paulo"))
                .andExpect(jsonPath("$.email").value("paulo@email.com"));
    }

    @Test
    void deveRetornar404QuandoUsuarioNaoEncontrado() throws Exception {
        when(getUserUseCase.execute(99L)).thenThrow(new UserNotFoundException("Usuário não encontrado"));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));
    }

    @Test
    void deveListarUsuarios() throws Exception {
        User user1 = new User(1L, "Paulo", "paulo@email.com", "123");
        User user2 = new User(2L, "Maria", "maria@email.com", "456");

        when(listUsersUseCase.execute()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        UpdateUserRequestDTO request = new UpdateUserRequestDTO("Paulo Updated", "paulo@email.com");
        User updatedUser = new User(1L, "Paulo Updated", "paulo@email.com", "123");

        when(updateUserUseCase.execute(eq(1L), any(), any())).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paulo Updated"));
    }

    @Test
    void deveDeletarUsuario() throws Exception {
        doNothing().when(deleteUserUseCase).execute(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        UserRequestDTO request = new UserRequestDTO("", "", "");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void deveRetornar404AoDeletarUsuarioInexistente() throws Exception {
        doThrow(new UserNotFoundException("Usuário não encontrado")).when(deleteUserUseCase).execute(99L);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));
    }

    @org.springframework.boot.test.context.TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    )
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable);
            return http.build();
        }
    }
}
