package net.pandamaster.sample.spring.amqp.web.controller;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    ModelAndView getView(@RequestParam("error") Optional<Integer> error) {
        ModelAndView mav = new ModelAndView("login");
        String message = "";
        switch(error.orElse(0)) {
            case 1:
                message = messageSource.getMessage("user.error.notfound", null, LocaleContextHolder.getLocale());
                break;
            case 2:
                message = messageSource.getMessage("user.error.disabled", null, LocaleContextHolder.getLocale());
                break;
            case 3:
                message = messageSource.getMessage("user.error.locked", null, LocaleContextHolder.getLocale());
                break;
            case 4:
                message = messageSource.getMessage("user.error.expired.account", null, LocaleContextHolder.getLocale());
                break;
            case 5:
                message = messageSource.getMessage("user.error.expired.credentials", null, LocaleContextHolder.getLocale());
                break;
        }
        mav.addObject("message", message);
        return mav;
    }

    @RequestMapping("/logout")
    String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}
