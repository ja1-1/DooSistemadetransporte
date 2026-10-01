package transporte.dominio.repositorio;

import transporte.dominio.MunicipioDominio;
import transporte.dominio.valor.Pagina;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MunicipioRepositorio {
    void guardar(MunicipioDominio municipio);

    Optional<MunicipioDominio> buscarPorId(UUID id);

    List<MunicipioDominio> buscar(
            @Nullable String nombre,
            @Nullable String departamento,
            @Nullable Boolean activo,
            Pagina pagina);

    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    boolean existePorNombreYDepartamento(String nombre, String departamento);
}
