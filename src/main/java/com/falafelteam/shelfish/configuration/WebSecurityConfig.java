package com.falafelteam.shelfish.configuration;

import com.falafelteam.shelfish.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan("com.falafelteam.shelfish.configuration")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    /**
     * Configuration for different requests permission depending on the role of the user
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/searchDocument", "/document/{id}", "/user/{id}").permitAll()
                    .antMatchers("/myProfile").not().hasRole("Admin")
                    .antMatchers("/addDocument", "/addUser").hasAnyRole("LibrarianPriv2", "LibrarianPriv3", "Admin")
                    .antMatchers("/modifyDocument/{id}", "/modifyUser/{id}").hasAnyRole("LibrarianPriv1", "LibrarianPriv2", "LibrarianPriv3", "Admin")
                    .antMatchers("/deleteDocument", "/deleteUser").hasAnyRole("LibrarianPriv3", "Admin")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.inMemoryAuthentication().withUser("shelfishuser").password("$2a$11$Mm4ulz9XgtA6bpsvCR2Eb.uHc6I2.folFUljUwT3jcFtri1XkfNRa").roles("Admin");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

}
