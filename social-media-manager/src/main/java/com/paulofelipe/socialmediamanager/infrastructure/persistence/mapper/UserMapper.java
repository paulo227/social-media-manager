package com.paulofelipe.socialmediamanager.infrastructure.persistence.mapper;

import com.paulofelipe.socialmediamanager.domain.entity.User;
import com.paulofelipe.socialmediamanager.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
        entity.setCreatedAt(user.getCreatedAt());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        User user = new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword()
        );
        user.setCreatedAt(entity.getCreatedAt());
        return user;
    }
}

