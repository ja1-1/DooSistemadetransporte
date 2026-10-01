package transporte.dominio.repositorio;

import transporte.dominio.ConductorDominio;
import transporte.dominio.consulta.FiltroConductor;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Pagina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConductorRepositorio {
    long contar(FiltroConductor filtro);

    void guardar(ConductorDominio conductor);

    Optional<ConductorDominio> buscarPorId(UUID id);

    List<ConductorDominio> buscar(FiltroConductor filtro, Pagina pagina);

    List<ConductorDominio> relacionados(FiltroConductor filtro);

    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    default Optional<ConductorDominio> buscarPorCedula(Cedula cedula) {
        return relacionados(FiltroConductor.todos().conCedula(cedula.valor())).stream().findFirst();
    }

    default boolean existePorCedula(Cedula cedula) {
        return buscarPorCedula(cedula).isPresent();
    }

    default boolean existePorNumeroLicencia(String numeroLicencia) {
        return !relacionados(FiltroConductor.todos().conNumeroLicencia(numeroLicencia)).isEmpty();
    }
}
