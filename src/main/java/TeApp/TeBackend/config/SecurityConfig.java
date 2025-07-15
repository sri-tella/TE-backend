package TeApp.TeBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for simplicity; consider enabling it for production
                .authorizeRequests()
                .requestMatchers("/api/admins/**","/api/auth/signup", "/api/auth/login", "api/options/saveSelected", "api/options", "api/saveSelectedRecommendations", "api/form/**","api/evaluations/**", "api/reports/save-pdf", "api/reports","/api/reports/**","/api/observers/**","api/classes/**" ).permitAll() // Allow access to these endpoints without authentication
                .anyRequest().authenticated(); // All other requests require authentication
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


