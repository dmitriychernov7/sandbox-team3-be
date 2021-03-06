package com.exadel.discountwebapp.security;

import com.exadel.discountwebapp.exception.exception.client.EntityNotFoundException;
import com.exadel.discountwebapp.user.service.UserService;
import com.exadel.discountwebapp.user.vo.UserResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signin")
    public TokenResponse signIn(@RequestBody SigninVO signinVO) {
        UserResponseVO userResponse;
        try {
            userResponse = userService.findByLoginAndPassword(signinVO);
        } catch (EntityNotFoundException e) {
            throw new BadCredentialsException("Login or Password is wrong! Try again!");
        }
        return new TokenResponse(jwtProvider.generateToken(userResponse.getEmail()));
    }
}
