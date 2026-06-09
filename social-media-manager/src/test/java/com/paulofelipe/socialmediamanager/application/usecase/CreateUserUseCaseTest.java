package com.paulofelipe.socialmediamanager.application.usecase;
import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    class CreateUserUseCaseTest {

        private final UserRepository repository = mock(UserRepository.class);
        private final CreateUserUseCase useCase = new CreateUserUseCase(repository);

        @Test
        void deveCriarUsuarioComSucesso() {

            User user = new User(null, "Paulo", "paulo@email.com", "123");

            when(repository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.empty());

            when(repository.save(user))
                    .thenReturn(user);

            User result = useCase.execute(user);

            assertNotNull(result);
            assertEquals("Paulo", result.getName());
            assertEquals("paulo@email.com", result.getEmail());

            verify(repository, times(1)).save(user);
        }

        @Test
        void deveFalharQuandoEmailJaExiste() {

            User user = new User(null, "Paulo", "paulo@email.com", "123");

            when(repository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));

            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> useCase.execute(user)
            );

            assertEquals("Email já existe", exception.getMessage());

            verify(repository, never()).save(user);
        }
    }
