package com.wizzdi.security.bearer.jwt;

import com.wizzdi.security.adapter.FlexicoreUserDetails;
import com.wizzdi.security.bearer.jwt.request.AuthRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Authentication")
@RestController
@RequestMapping(path = "api/public")
public class AuthApi {

    private final AuthenticationManager authenticationManager;
    private final FlexicoreJwtTokenUtil flexicoreJwtTokenUtil;

    public AuthApi(AuthenticationManager authenticationManager,
                   FlexicoreJwtTokenUtil flexicoreJwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.flexicoreJwtTokenUtil = flexicoreJwtTokenUtil;
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                    )
                );

            FlexicoreUserDetails flexicoreUserDetails= (FlexicoreUserDetails) authenticate.getPrincipal();
            return ResponseEntity.ok()
                .header(
                    HttpHeaders.AUTHORIZATION,
                    flexicoreJwtTokenUtil.generateAccessToken(flexicoreUserDetails)
                )
                .body(authenticate.getPrincipal());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}