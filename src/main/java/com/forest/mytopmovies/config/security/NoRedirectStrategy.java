package com.forest.mytopmovies.config.security;

import org.springframework.security.web.RedirectStrategy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoRedirectStrategy implements RedirectStrategy {
    @Override
    public void sendRedirect(final HttpServletRequest request, final HttpServletResponse response, final String url)  {
        // No redirect is required with pure REST
        // Need to add this, or else it will keep redirecting after a success authentication
    }
}
