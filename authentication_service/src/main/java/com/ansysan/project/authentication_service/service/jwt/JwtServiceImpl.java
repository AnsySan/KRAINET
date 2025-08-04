package com.ansysan.project.authentication_service.service.jwt;

import com.ansysan.project.authentication_service.dto.response.TokenResponse;
import com.ansysan.project.authentication_service.entity.Token;
import com.ansysan.project.authentication_service.entity.User;
import com.ansysan.project.authentication_service.exception.InvalidTokenException;
import com.ansysan.project.authentication_service.exception.NotFoundException;
import com.ansysan.project.authentication_service.repository.TokenRepository;
import com.ansysan.project.authentication_service.repository.UserRepository;
import com.ansysan.project.authentication_service.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JwtServiceImpl implements JwtService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    @Override
    public TokenResponse generateToken(UserDetails user) {
        log.debug("Generating token");
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        saveToken(user.getUsername(), refreshToken);
        log.debug("Generated token");
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse regenerateToken(String token) {
        log.debug("Regenerating token");
        if(!jwtProvider.validateToken(token)) {
            throw new InvalidTokenException("Invalid token");
        }
        String username = jwtProvider.extractAllClaims(token).getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found" + username));
        log.debug("Regenerated token");
        return generateToken(user);
    }

    private void saveToken(String username, String token) {
        log.debug("Saving token");
        if(!userRepository.existsByUsername(username)) {
            throw new NotFoundException("User not found" + username);
        }
        tokenRepository.save(new Token(username, token));
    }
}