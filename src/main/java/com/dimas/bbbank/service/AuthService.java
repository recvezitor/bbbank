package com.dimas.bbbank.service;

import com.dimas.bbbank.api.ApiLoginRequest;
import com.dimas.bbbank.api.ApiLoginResponse;
import com.dimas.bbbank.domain.entity.User;
import com.dimas.bbbank.exception.ApiAccessDeniedException;
import com.dimas.bbbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public ApiLoginResponse login(ApiLoginRequest authRequest) {
        final User user = userRepository.findById(Long.parseLong(authRequest.getLogin()))
                .orElseThrow(() -> new ApiAccessDeniedException("Не найден"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            return new ApiLoginResponse(accessToken, refreshToken);
        } else {
            throw new ApiAccessDeniedException("Неправильный пароль");
        }
    }

}
