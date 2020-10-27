package com.zakharovakr.blogCMSMastery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetails;

    @Autowired
    public void configureGlobalInDB(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers( "/static").permitAll()
                .antMatchers( "/post").permitAll()
                .antMatchers( "/createUser").permitAll()
                .antMatchers( "/searchPost").permitAll()
                .antMatchers( "/searchPostByCategory").permitAll()
                .antMatchers( "/editUser").hasAnyRole("ADMIN", "MANAGER","USER")
                .antMatchers( "/contentManager").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers( "/editContent").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers( "/createContent").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers( "/categoryManager").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers( "/editCategory").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers( "/userManager").hasRole("ADMIN")
                .antMatchers("/images/**", "/css/**", "/js/**", "/fonts/**").permitAll()
                .anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?login_error=1")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
    }

}
