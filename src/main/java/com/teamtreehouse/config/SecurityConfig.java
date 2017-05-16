package com.teamtreehouse.config;

import com.teamtreehouse.domain.User;
import com.teamtreehouse.service.DetailsService;
import com.teamtreehouse.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DetailsService detailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService)
                .passwordEncoder(User.PASSWORD_ENCODER);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/signup");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/signup").permitAll()
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .successHandler(loginSuccessHandler())
                    .failureHandler(loginFailureHandler())
                    .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/login")
                    .and()
                .csrf().disable();
    }

    public AuthenticationSuccessHandler loginSuccessHandler() {
        return ((request, response, authentication) -> response.sendRedirect("/"));
    }

    public AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            request.getSession().setAttribute("flash",
                    new FlashMessage("Incorrect username and/or password. Please try again.", FlashMessage.Status.FAILURE));
            response.sendRedirect("/login");
        };
    }

    @Bean
    public EvaluationContextExtension securityExtension() {
        return new EvaluationContextExtensionSupport() {
            @Override
            public String getExtensionId() {
                return "security";
            }

            @Override
            public Object getRootObject() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                return new SecurityExpressionRoot(authentication) {};
            }
        };
    }
}
