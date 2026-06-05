package com.paulofelipe.socialmediamanager.infrastructure.persistence.jpa;


import com.paulofelipe.socialmediamanager.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
        Optional<UserEntity> findByEmail(String email);
    }

