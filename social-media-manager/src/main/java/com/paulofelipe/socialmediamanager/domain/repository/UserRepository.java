
package com.paulofelipe.socialmediamanager.domain.repository;

import com.paulofelipe.socialmediamanager.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);
}