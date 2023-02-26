package com.ind.tr.controller.auth;

import com.ind.tr.repository.UserDao;
import com.ind.tr.repository.model.UserReadEntity;
import com.ind.tr.service.ISTClock;
import com.ind.tr.service.translator.UserTranslator;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private ISTClock istClock;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserTranslator userTranslator;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authToken = getTokenFromRequest(request);

        //TODO Check if hasText required
        if (StringUtils.hasText(authToken)) {
            JWTClaimsSet claimsSet = jwtTokenService.getUsernameFromToken(authToken);
            Date expiry = claimsSet.getExpirationTime();
            UserReadEntity userReadEntity = userDao.getUser(UUID.fromString(claimsSet.getSubject()));
            if (userReadEntity != null && expiry.compareTo(istClock.getTodayDate()) > 0) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(userTranslator.fromUserReadEntity(userReadEntity), null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
