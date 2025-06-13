// Archivo: SecurityConfig.java (o el nombre que tenga tu configuración de seguridad)
package com.projectfinal.greenthumb_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Aplicamos nuestra configuración de CORS
                .cors(withDefaults())
                // 2. Deshabilitamos CSRF (común en APIs sin estado)
                .csrf(csrf -> csrf.disable())
                // 3. Configuramos las reglas de autorización (PERMITIR TODO)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite el acceso a todos los endpoints sin autenticación
                );
        return http.build();
    }

    // 4. CREAMOS EL BEAN DE CONFIGURACIÓN DE CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Especificamos el origen permitido (tu frontend)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // Especificamos los métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permitimos todas las cabeceras
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Permitimos el envío de credenciales (si fuera necesario en el futuro)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicamos la configuración a todas las rutas de la API
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}