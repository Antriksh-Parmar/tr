package com.ind.tr.service;

import com.ind.tr.repository.UserDao;
import com.ind.tr.service.model.GuestUser;
import com.ind.tr.service.translator.UserTranslator;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class RegistrationServiceImpl implements RegistrationService {

    private final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.time}")
    private long expirationTime;

    @Value("${jwt.issuer}")
    private String issuer;

    @Autowired
    private UserTranslator userTranslator;

    @Autowired
    private ISTClock istClock;

    @Autowired
    private UserDao userDao;

    @Override
    public String createGuestUser() {
        return generateJWTToken();
    }

    private String generateJWTToken() {
        Instant now = Instant.now(istClock.getClock());
        UUID uuid = UUID.randomUUID();
        GuestUser guestUser = new GuestUser(uuid);
        Date expiryDate = Date.from(now.plus(expirationTime, ChronoUnit.SECONDS));

        userDao.saveGuestUser(userTranslator.toUserWriteEntity(guestUser));

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(uuid.toString())
                .issuer(issuer)
                .expirationTime(expiryDate)
                .build();

        try {
            JWSSigner signer = new MACSigner(secret);
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            logger.error("JWT generation failed for Guest user.");
            throw new RuntimeException(e);
        }
    }
}
