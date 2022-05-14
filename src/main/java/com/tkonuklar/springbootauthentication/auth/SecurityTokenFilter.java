package com.tkonuklar.springbootauthentication.auth;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.tkonuklar.springbootauthentication.auth.model.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecurityTokenFilter extends BasicAuthenticationFilter {
    public SecurityTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        verifyToken(request);
        filterChain.doFilter(request, response);
    }

    private void verifyToken(HttpServletRequest request) throws FirebaseAuthException {
        String token = getBearerToken(request);
        if (token != null) {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            User user = firebaseTokenToUserDto(decodedToken);
            if (token != null && user != null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, null);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }

    private User firebaseTokenToUserDto(FirebaseToken decodedToken) {
        if (decodedToken != null) {
            return new User().setUid(decodedToken.getUid())
                    .setName(decodedToken.getName())
                    .setEmail(decodedToken.getEmail())
                    .setPicture(decodedToken.getPicture())
                    .setIssuer(decodedToken.getIssuer())
                    .setEmailVerified(decodedToken.isEmailVerified());
        }
        return null;
    }

    public String getBearerToken(HttpServletRequest request) {
        String bearerToken = null;
        String authorization = request.getHeader("Authorization");
        if (!Strings.isBlank(authorization) && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7);
        }
        return bearerToken;
    }
}
