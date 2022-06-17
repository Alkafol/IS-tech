package com.techprog.outerInterface.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import static com.techprog.outerInterface.security.ApplicationUserRole.ADMIN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService){
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        InMemoryUserDetailsManagerConfigurer memprovider =
                new InMemoryUserDetailsManagerConfigurer();

        memprovider.passwordEncoder(passwordEncoder);
        memprovider.withUser("admin")
                .disabled(false)
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .password(passwordEncoder.encode("12345"))
                .roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthorities().stream().toList());
        auth.apply(memprovider);

        DaoAuthenticationConfigurer daoAC = new DaoAuthenticationConfigurer(userDetailsService);
        daoAC.passwordEncoder(passwordEncoder);
        auth.apply(daoAC);
    }
}
