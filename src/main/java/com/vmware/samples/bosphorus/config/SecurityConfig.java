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
    	// We require authentication for all pages except some static content.
    	//
        http
            .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
            	.httpBasic()
            .and()
            	.logout().permitAll()
        	.and()
        		.csrf().ignoringAntMatchers("/events/**");
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new VraAuthenticationProvider(vraUrl));
    }
}