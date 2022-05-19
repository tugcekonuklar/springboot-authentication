package com.tkonuklar.springbootauthentication.api;

import com.tkonuklar.springbootauthentication.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("internal")
@RequiredArgsConstructor
public class InternalController {

    @GetMapping()
    @ResponseStatus(OK)
    public String getHello() {
        return "Hello ";
    }

}
