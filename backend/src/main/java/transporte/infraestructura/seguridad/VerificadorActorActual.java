package transporte.infraestructura.seguridad;

import transporte.negocio.reglas.ReglaActorVigente;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class VerificadorActorActual {
    private final ReglaActorVigente regla;

    public VerificadorActorActual(ReglaActorVigente regla) {
        this.regla = regla;
    }

    public void verificar() {
        var peticion = RequestContextHolder.getRequestAttributes();
        boolean cambio =
                peticion instanceof ServletRequestAttributes solicitud
                        && solicitud
                                .getRequest()
                                .getRequestURI()
                                .equals("/api/v1/usuarios/me/contrasena");
        regla.ejecutar(new ReglaActorVigente.Contexto(actorActual(), cambio));
    }

    private static ReglaActorVigente.@Nullable Actor actorActual() {
        var autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null
                || !(autenticacion.getPrincipal() instanceof UsuarioAutenticado usuario)) {
            return null;
        }
        return new ReglaActorVigente.Actor(usuario.id(), usuario.rol(), usuario.credencial());
    }
}
