package com.ttn.bluebell.security;

import com.ttn.bluebell.durable.model.employee.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Value("${security.enable-csrf}")
    private boolean csrfEnabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(!csrfEnabled)
        {
            http.csrf().disable();
        }

        http.authorizeRequests()
            .antMatchers("/login/**","*.html","*.css","*.png","*.js",".gif","/swagger-resources/**,/refreshCache", "/sso/callback").permitAll()
            .antMatchers("/**").hasAnyAuthority(Role.SYSTEM_ADMIN.getName(),Role.REGION_HEAD.getName(), Role.STAFFING_MANAGER.getName(),Role.SALES_LEAD.getName(),Role.HRBP.getName())
            .and().addFilterBefore(new StatelessAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public RoleVoter roleVoter(){
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
       return roleVoter;
    }

}
