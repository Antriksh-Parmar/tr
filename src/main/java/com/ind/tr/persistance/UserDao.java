package com.ind.tr.persistance;

import com.ind.tr.persistance.model.UserReadEntity;
import com.ind.tr.persistance.model.UserWriteEntity;

import java.util.UUID;

public interface UserDao {
    void saveGuestUser(UserWriteEntity userWriteEntity);

    UserReadEntity getUser(UUID uuid);
}
