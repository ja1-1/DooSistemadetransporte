package transporte.configuracion;

import transporte.infraestructura.seguridad.AutenticadorUsuarios;

import jakarta.servlet.DispatcherType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
public class SeguridadConfiguracion {
    @Bean
    SecurityFilterChain seguridad(HttpSecurity http, AutenticadorUsuarios autenticador)
            throws Exception {
        AuthenticationEntryPoint entrada =
                (peticion, respuesta, error) -> {
                    respuesta.setStatus(401);
                    respuesta.setContentType("application/json;charset=UTF-8");
                    respuesta
                            .getWriter()
                            .write(
                                    "{\"codigo\":\"NO_AUTENTICADO\",\"mensaje\":\"Debes iniciar"
                                            + " sesión.\"}");
                };
        return http.authenticationProvider(autenticador)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(
                        sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basic -> basic.authenticationEntryPoint(entrada))
                .authorizeHttpRequests(
                        reglas ->
                                reglas.dispatcherTypeMatchers(DispatcherType.ERROR)
                                        .permitAll()
                                        .requestMatchers(
                                                "/api/v1/sesion", "/api/v1/usuarios/me/contrasena")
                                        .authenticated()
                                        .requestMatchers("/api/v1/usuarios/me")
                                        .hasAnyRole("ADMINISTRADOR", "JEFE_OPERACION", "CONDUCTOR")
                                        .requestMatchers("/api/v1/usuarios/**")
                                        .hasRole("ADMINISTRADOR")
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/api/v1/roles/**",
                                                "/api/v1/estados-salida/**",
                                                "/api/v1/tipos-ruta/**",
                                                "/api/v1/tipos-incidencia/**")
                                        .hasAnyRole("ADMINISTRADOR", "JEFE_OPERACION")
                                        .requestMatchers(
                                                HttpMethod.GET, "/api/v1/asignaciones/disponibles")
                                        .hasRole("JEFE_OPERACION")
                                        .requestMatchers(
                                                HttpMethod.POST, "/api/v1/asignaciones/*/relevar")
                                        .hasRole("JEFE_OPERACION")
                                        .requestMatchers("/api/v1/asignaciones/**")
                                        .hasAnyRole("ADMINISTRADOR", "JEFE_OPERACION")
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                "/api/v1/programaciones-ruta/*/generar")
                                        .hasRole("JEFE_OPERACION")
                                        .requestMatchers(
                                                HttpMethod.GET, "/api/v1/programaciones-ruta/**")
                                        .hasAnyRole("ADMINISTRADOR", "JEFE_OPERACION")
                                        .requestMatchers("/api/v1/programaciones-ruta/**")
                                        .hasRole("ADMINISTRADOR")
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/api/v1/salidas/*/incidencias",
                                                "/api/v1/incidencias/**")
                                        .hasAnyRole("ADMINISTRADOR", "JEFE_OPERACION")
                                        .requestMatchers(
                                                HttpMethod.POST, "/api/v1/salidas/*/incidencias")
                                        .hasAnyRole("JEFE_OPERACION", "CONDUCTOR")
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                "/api/v1/salidas/*/iniciar",
                                                "/api/v1/salidas/*/finalizar")
                                        .hasRole("CONDUCTOR")
                                        .requestMatchers(HttpMethod.GET, "/api/v1/salidas/**")
                                        .hasAnyRole("JEFE_OPERACION", "CONDUCTOR")
                                        .requestMatchers("/api/v1/salidas/**")
                                        .hasRole("JEFE_OPERACION")
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/api/v1/categorias-vehiculo/**",
                                                "/api/v1/conductores/**",
                                                "/api/v1/vehiculos/**",
                                                "/api/v1/rutas/**",
                                                "/api/v1/municipios/**",
                                                "/api/v1/vias/**",
                                                "/api/v1/sectores/**",
                                                "/api/v1/lugares/**")
                                        .hasAnyRole("ADMINISTRADOR", "JEFE_OPERACION")
                                        .requestMatchers(
                                                "/api/v1/categorias-vehiculo/**",
                                                "/api/v1/conductores/**",
                                                "/api/v1/vehiculos/**",
                                                "/api/v1/rutas/**",
                                                "/api/v1/municipios/**",
                                                "/api/v1/vias/**",
                                                "/api/v1/sectores/**",
                                                "/api/v1/lugares/**")
                                        .hasRole("ADMINISTRADOR")
                                        .anyRequest()
                                        .denyAll())
                .exceptionHandling(
                        errores ->
                                errores.authenticationEntryPoint(entrada)
                                        .accessDeniedHandler(
                                                (peticion, respuesta, error) -> {
                                                    respuesta.setStatus(403);
                                                    respuesta.setContentType(
                                                            "application/json;charset=UTF-8");
                                                    respuesta
                                                            .getWriter()
                                                            .write(
                                                                    "{\"codigo\":\"SIN_PERMISO\",\"mensaje\":\"Tu"
                                                                        + " cuenta no tiene permiso"
                                                                        + " para realizar esta"
                                                                        + " acción.\"}");
                                                }))
                .build();
    }
}
