package com.ind.tr.controller.auth;

import com.ind.tr.service.ISTClock;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenService {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.time}")
    private long expirationTime;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Autowired
    private ISTClock istClock;

    //TODO consider using asymmetric key
    public JWTClaimsSet getUsernameFromToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(jwtSecret);
            SignedJWT signedJWT = SignedJWT.parse(token);
            signedJWT.verify(verifier);
            return signedJWT.getJWTClaimsSet();
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateJWTToken(UUID uuid) {
        Instant now = Instant.now(istClock.getClock());
            Date expiryDate = Date.from(now.plus(expirationTime, ChronoUnit.SECONDS));
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
            logger.error("JWT generation failed for Guest user: " + uuid);
            throw new RuntimeException(e);
        }
    }
}
