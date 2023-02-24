package com.ind.tr.service.translator;

import com.ind.tr.repository.model.UserReadEntity;
import com.ind.tr.repository.model.UserWriteEntity;
import com.ind.tr.service.model.GuestUser;
import com.ind.tr.service.model.User;

public interface UserTranslator {
    UserWriteEntity toUserWriteEntity(GuestUser guestUser);

    User fromUserReadEntity(UserReadEntity userReadEntity);
}
