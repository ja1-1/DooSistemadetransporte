package transporte.dominio.valor;

import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.dominio.UsuarioDominio;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Responsable(UUID usuarioId, String nombre, String rol) {
    public Responsable {
        usuarioId = requerido(usuarioId, "usuarioId");
        nombre = texto(nombre, "nombre", 3, 200);
        rol = texto(rol, "rol", 1, 40);
    }

    public static Responsable desde(UsuarioDominio usuario) {
        requerido(usuario, "usuario");
        return new Responsable(
                usuario.getId(),
                Stream.of(
                                usuario.getPrimerNombre(),
                                usuario.getSegundoNombre().orElse(""),
                                usuario.getPrimerApellido(),
                                usuario.getSegundoApellido().orElse(""))
                        .filter(textoRecibido -> !textoRecibido.isEmpty())
                        .collect(Collectors.joining(" ")),
                usuario.getRol().getNombre());
    }
}
