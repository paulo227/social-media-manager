package com.paulofelipe.socialmediamanager.application.usecase;

import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.domain.exception.DuplicateEmailException;
import com.paulofelipe.socialmediamanager.domain.exception.UserNotFoundException;
import com.paulofelipe.socialmediamanager.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserUseCase {

    private final UserRepository userRepository;

    public UpdateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long id, String name, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!user.getEmail().equals(email)) {
            userRepository.findByEmail(email)
                    .ifPresent(u -> {
                        throw new DuplicateEmailException("Email já existe");
                    });
        }

        user.setName(name);
        user.setEmail(email);

        return userRepository.save(user);
    }
}
