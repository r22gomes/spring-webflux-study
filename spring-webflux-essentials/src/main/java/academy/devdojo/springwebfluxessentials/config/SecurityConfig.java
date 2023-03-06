package academy.devdojo.springwebfluxessentials.config;

import academy.devdojo.springwebfluxessentials.service.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/shows/*").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/shows/*").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/shows/*").hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST, "/shows").hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, "/shows/*").hasRole("USER")
                .pathMatchers(HttpMethod.GET, "/shows").hasRole("USER")
                .pathMatchers(HttpMethod.POST, "/user").permitAll()
                .pathMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/webjars/**"
                ).permitAll()
                .anyExchange().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic()
                .and()
                .build();

    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(UserDetailsService service) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(service);
    }
}
