package transporte.dominio.repositorio;

import transporte.dominio.RutaDominio;
import transporte.dominio.consulta.FiltroRuta;
import transporte.dominio.valor.Pagina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RutaRepositorio {
    long contar(FiltroRuta filtro);

    void guardar(RutaDominio ruta);

    Optional<RutaDominio> buscarPorId(UUID id);

    List<RutaDominio> buscar(FiltroRuta filtro, Pagina pagina);

    List<RutaDominio> relacionados(FiltroRuta filtro);

    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    default boolean existePorRecorrido(
            UUID origenId, UUID destinoId, UUID viaId, Optional<UUID> excluirId) {
        return relacionados(
                        FiltroRuta.todos()
                                .conOrigenLugarId(origenId)
                                .conDestinoLugarId(destinoId)
                                .conViaId(viaId))
                .stream()
                .anyMatch(ruta -> excluirId.isEmpty() || !ruta.getId().equals(excluirId.get()));
    }
}
