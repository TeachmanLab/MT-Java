package org.mindtrails.config;

import org.mindtrails.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Sets the language when users log into the system.
 */
@Component
public class LocaleSettingAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        setLocale(authentication, request, response);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    protected void setLocale(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Participant) {
                Participant p = (Participant) principal;
                if (p.getLanguage() != null) {
                    Locale providedLocale = new Locale(p.getLanguage());
                    localeResolver.setLocale(request, response, providedLocale);
                }
            }
        }
    }
}