package com.dimas.bbbank.controller;


import com.dimas.bbbank.api.ApiLoginRequest;
import com.dimas.bbbank.api.ApiLoginResponse;
import com.dimas.bbbank.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dimas.bbbank.util.Util.ROOT_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT_PATH)
public class AuthController {

    private final static String PATH = "/auth/login";

    private final AuthService authService;

    @PostMapping(value = PATH, consumes = APPLICATION_JSON_VALUE)
    public ApiLoginResponse login(@RequestBody @Validated final ApiLoginRequest request) {
        return authService.login(request);
    }

}
