/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.pandamaster.sample.spring.amqp.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.pandamaster.sample.spring.amqp.entity.Users;
import net.pandamaster.sample.spring.amqp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *
 * @author yhida
 */
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationSuccessHandler.class);

    @Value("${sample.amqp.queue.salt}")
    private String SALT;
    
    private UserService userService;
    
    public UserAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        String queueName = new ShaPasswordEncoder(256).encodePassword(((Users)authentication.getPrincipal()).getUsername(), SALT);
        req.getSession().setAttribute("queueName", queueName);
        userService.addQueue(queueName);
        res.sendRedirect("index");
    }
}
