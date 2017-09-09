/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.pandamaster.sample.spring.amqp.web.controller;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author yhida
 */
@Controller
public class IndexController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    ModelAndView getView(HttpServletRequest req, Principal principal) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("queue", req.getSession().getAttribute("queueName"));
        return mav;
    }
}
