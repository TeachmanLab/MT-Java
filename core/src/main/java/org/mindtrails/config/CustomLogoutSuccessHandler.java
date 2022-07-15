package org.mindtrails.config;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
{

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        String language=request.getParameter("lang");
        if( language!= null)
        {
            response.sendRedirect(request.getContextPath()+"/?lang="+language);
        }
        else
        {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}