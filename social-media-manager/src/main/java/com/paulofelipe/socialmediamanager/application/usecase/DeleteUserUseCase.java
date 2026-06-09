package com.paulofelipe.socialmediamanager.application.usecase;

import com.paulofelipe.socialmediamanager.domain.exception.UserNotFoundException;
import com.paulofelipe.socialmediamanager.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }
}
