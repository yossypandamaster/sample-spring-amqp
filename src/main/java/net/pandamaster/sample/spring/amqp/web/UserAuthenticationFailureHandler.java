package net.pandamaster.sample.spring.amqp.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationFailureHandler.class);
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException authenticationException)
                    throws IOException, ServletException {

        String errorId = "";
        if(authenticationException instanceof DisabledException){
            errorId = "2";
        }
        else if(authenticationException instanceof LockedException){
            errorId = "3";
        }
        else if(authenticationException instanceof AccountExpiredException){
            errorId = "4";
        }
        else if(authenticationException instanceof CredentialsExpiredException){
            errorId = "5";
        }
        else if(authenticationException instanceof BadCredentialsException) {
            errorId = "6";
        }
        else {
            LOGGER.debug(authenticationException.getMessage(), authenticationException.getCause(), authenticationException);
            errorId = "1";
        }

        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login?error=" + errorId);
    }
}
