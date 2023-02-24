package com.ind.tr.service.translator;

import com.ind.tr.repository.model.UserReadEntity;
import com.ind.tr.repository.model.UserWriteEntity;
import com.ind.tr.service.model.User;
import com.ind.tr.service.model.GuestUser;
import com.ind.tr.service.model.PlatformUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserTranslatorImpl implements UserTranslator {

    @Override
    public UserWriteEntity toUserWriteEntity(GuestUser guestUser) {
        return new UserWriteEntity(guestUser.getId(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Override
    public User fromUserReadEntity(UserReadEntity userReadEntity) {
        if(userReadEntity.isGuestUser()) {
            return new GuestUser(userReadEntity.getId());
        } else {
            return new PlatformUser(userReadEntity.getId(), userReadEntity.getFirstName().get(), userReadEntity.getLastName().get(), userReadEntity.getEmail().get(), userReadEntity.getPasswordHash().get());
        }
    }
}
