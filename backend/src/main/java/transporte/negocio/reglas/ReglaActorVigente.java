package transporte.negocio.reglas;

import transporte.dominio.enums.Rol;
import transporte.dominio.repositorio.UsuarioRepositorio;
import transporte.dominio.valor.HashContrasena;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.jspecify.annotations.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReglaActorVigente implements Rule<ReglaActorVigente.Contexto> {
    public record Actor(UUID id, Rol rol, HashContrasena credencial) {}

    public record Contexto(@Nullable Actor actor, boolean cambioContrasena) {}

    private final UsuarioRepositorio usuarios;

    public ReglaActorVigente(UsuarioRepositorio usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public void ejecutar(Contexto contexto) {
        var actor = contexto.actor();
        if (actor == null) {
            throw new AccessDeniedException(CatalogoMensajes.DEBES_INICIAR_SESION);
        }
        var usuario =
                usuarios.buscarPorId(actor.id())
                        .orElseThrow(
                                () ->
                                        new AccessDeniedException(
                                                CatalogoMensajes.EL_USUARIO_YA_NO_ESTA_DISPONIBLE));
        if (!usuario.isActivo()
                || usuario.getRol() != actor.rol()
                || !usuario.getHashContrasena().equals(actor.credencial())
                || (usuario.isDebeCambiarContrasena() && !contexto.cambioContrasena())) {
            throw new AccessDeniedException(CatalogoMensajes.EL_ESTADO_O_LOS_PERMISOS_DE_LA);
        }
    }
}
