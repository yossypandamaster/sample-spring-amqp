package net.pandamaster.sample.spring.amqp.web;

import net.pandamaster.sample.spring.amqp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .csrf()
                .and()
            .formLogin()
                .loginProcessingUrl("/auth")
                //.defaultSuccessUrl("/index")
                .loginPage("/login")
                .successHandler(new UserAuthenticationSuccessHandler(userService))
                .failureHandler(new UserAuthenticationFailureHandler())
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }
    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new Md5PasswordEncoder());
    }
}
