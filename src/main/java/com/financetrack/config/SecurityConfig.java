package com.financetrack.config;

import com.financetrack.security.JwtAuthFilter;
import com.financetrack.security.JwtService;
import com.financetrack.service.ClienteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ClienteService usuarioService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(ClienteService usuarioService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/clientes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/clientes").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/clientes/auth").permitAll()
                        .requestMatchers("/api/v1/clientes/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/clientes/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/aportes/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/categoriasDespesa/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/categoriasReceita/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/despesas/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/formasPagamento/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/metasFinanceiras/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/parcelas/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/receitas/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }
}