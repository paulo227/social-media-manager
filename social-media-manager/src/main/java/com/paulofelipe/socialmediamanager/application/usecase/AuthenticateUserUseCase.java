package com.paulofelipe.socialmediamanager.application.usecase;

import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;

    public AuthenticateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));
    }
}
