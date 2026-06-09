
package com.paulofelipe.socialmediamanager.domain.repository;

import com.paulofelipe.socialmediamanager.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(Long id);
}