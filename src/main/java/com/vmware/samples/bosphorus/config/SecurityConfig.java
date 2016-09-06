package com.vmware.samples.bosphorus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.vmware.samples.bosphorus.security.VraAuthenticationProvider;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${vra.url}")
	private String vraUrl;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// We require authentication for all pages
    	//
        http
            .authorizeRequests()
                .antMatchers("/", "/css/**").permitAll()
                .antMatchers("/", "/images/**").permitAll()
                .antMatchers("/", "/js/**").permitAll()
                .antMatchers("/", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new VraAuthenticationProvider(vraUrl));
           // .inMemoryAuthentication()
             //   .withUser("user").password("password").roles("USER");
    }
}