package com.ind.tr.repository;

import com.ind.tr.repository.model.UserReadEntity;
import com.ind.tr.repository.model.UserWriteEntity;

import java.util.UUID;

public interface UserDao {
    void saveGuestUser(UserWriteEntity userWriteEntity);

    UserReadEntity getUser(UUID uuid);
}
