package no.nav.arbeid.cv.es.config;

import org.springframework.boot.actuate.autoconfigure.security.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class MyWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        // Spring Security should completely ignore URLs starting with /resources/
        .antMatchers("/resources/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .requestMatchers(EndpointRequest.to("status", "info", "health", "prometheus")).permitAll()
        .antMatchers("/rest/internal/isAlive").permitAll()
        .anyRequest().hasRole("USER").and()
        // Possibly more configuration ...
        .formLogin() // enable form based log in
        // set permitAll for all URLs associated with Form Login
        .permitAll();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // enable in memory based authentication with a user named "user" and "admin"
    auth.inMemoryAuthentication().withUser("user").password("password").roles("USER").and()
        .withUser("admin").password("password").roles("USER", "ADMIN");
    
    //auth.authenticationEventPublisher(new DefaultAuthenticationEventPublisher());
  }

  // Possibly more overridden methods ...
}
