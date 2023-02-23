package com.ind.tr.service.translator;

import com.ind.tr.controller.model.UserResponse;
import com.ind.tr.persistance.model.UserReadEntity;
import com.ind.tr.persistance.model.UserWriteEntity;
import com.ind.tr.service.model.User;
import com.ind.tr.service.model.GuestUser;

public interface UserTranslator {
    UserResponse toGuestUserResponse(GuestUser guestUser);
    UserWriteEntity toUserWriteEntity(GuestUser guestUser);

    User fromUserReadEntity(UserReadEntity userReadEntity);
}
