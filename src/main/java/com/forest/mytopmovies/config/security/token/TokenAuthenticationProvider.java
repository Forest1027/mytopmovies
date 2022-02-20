package com.forest.mytopmovies.config.security.token;

import com.forest.mytopmovies.service.user.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Finding the user by its authentication token
 */
@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private UserAuthService userAuthService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // no additional authentication
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getCredentials());
        return userAuthService.findByToken(token).orElseThrow(() -> new BadCredentialsException("Invalid token: " + token));
    }
}
