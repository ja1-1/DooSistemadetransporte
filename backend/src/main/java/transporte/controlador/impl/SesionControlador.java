package transporte.controlador.impl;

import transporte.dominio.enums.Rol;
import transporte.infraestructura.seguridad.UsuarioAutenticado;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class SesionControlador {
    public record Sesion(
            String usuario, List<String> roles, UUID id, boolean debeCambiarContrasena) {}

    @GetMapping("/api/v1/sesion")
    public Sesion consultar(Authentication autenticacion) {
        if (!(autenticacion.getPrincipal() instanceof UsuarioAutenticado actor)) {
            throw new AccessDeniedException(CatalogoMensajes.DEBES_INICIAR_SESION);
        }
        String rol = actor.rol() == Rol.JEFE_DE_OPERACION ? "JEFE_OPERACION" : actor.rol().name();
        return new Sesion(actor.correo(), List.of(rol), actor.id(), actor.debeCambiarContrasena());
    }
}
