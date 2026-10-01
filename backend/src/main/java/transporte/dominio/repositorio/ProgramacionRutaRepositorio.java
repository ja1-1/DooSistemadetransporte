package transporte.dominio.repositorio;

import transporte.dominio.ProgramacionRutaDominio;
import transporte.dominio.consulta.FiltroProgramacionRuta;
import transporte.dominio.valor.Pagina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProgramacionRutaRepositorio {
    long contar(FiltroProgramacionRuta filtro);

    void guardar(ProgramacionRutaDominio programacionRuta);

    Optional<ProgramacionRutaDominio> buscarPorId(UUID id);

    List<ProgramacionRutaDominio> buscar(FiltroProgramacionRuta filtro, Pagina pagina);

    List<ProgramacionRutaDominio> relacionados(FiltroProgramacionRuta filtro);

    default List<ProgramacionRutaDominio> buscarActivasPorRuta(UUID rutaId) {
        return relacionados(FiltroProgramacionRuta.todos().conRutaId(rutaId).conActiva(true));
    }

    default List<ProgramacionRutaDominio> buscarActivasPorAsignacion(UUID asignacionId) {
        return relacionados(
                FiltroProgramacionRuta.todos().conAsignacionId(asignacionId).conActiva(true));
    }
}
