package com.project.HeartConnect.configs;

import com.project.HeartConnect.security.JwtAuthenticationEntryPoint;
import com.project.HeartConnect.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    // Permit all swagger urls
    httpSecurity
        .authorizeHttpRequests(
            requests -> requests.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll());

    httpSecurity
        .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            request -> request.requestMatchers("/user/signup/**", "/user/token/login/**")
                .permitAll()
                .anyRequest().authenticated())
        .exceptionHandling(
            exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                .authenticationEntryPoint(jwtAuthenticationEntryPoint));

    httpSecurity.sessionManagement(
        httpSecuritySessionManagementConfigurer ->
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

    httpSecurity.addFilterBefore(jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration builder)
      throws Exception {
    return builder.getAuthenticationManager();
  }
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**"); // Enable CORS for the whole application.
      }
    };
  }
}
