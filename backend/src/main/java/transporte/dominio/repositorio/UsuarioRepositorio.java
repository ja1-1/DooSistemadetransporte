package transporte.dominio.repositorio;

import transporte.dominio.UsuarioDominio;
import transporte.dominio.consulta.FiltroUsuario;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Correo;
import transporte.dominio.valor.Pagina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositorio {
    long contar(FiltroUsuario filtro);

    void guardar(UsuarioDominio usuario);

    Optional<UsuarioDominio> buscarPorId(UUID id);

    List<UsuarioDominio> buscar(FiltroUsuario filtro, Pagina pagina);

    List<UsuarioDominio> relacionados(FiltroUsuario filtro);

    default boolean existePorCedula(Cedula cedula) {
        return !relacionados(FiltroUsuario.todos().conCedula(cedula.valor())).isEmpty();
    }

    default boolean existePorCorreo(Correo correo) {
        return buscarPorCorreo(correo).isPresent();
    }

    default Optional<UsuarioDominio> buscarPorCorreo(Correo correo) {
        return relacionados(FiltroUsuario.todos().conCorreo(correo.valor())).stream().findFirst();
    }
}
