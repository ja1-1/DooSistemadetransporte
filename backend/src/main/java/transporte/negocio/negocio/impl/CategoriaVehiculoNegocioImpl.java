package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.CONFLICTO;
import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;

import transporte.dominio.CategoriaVehiculoDominio;
import transporte.dominio.enums.CategoriaLicencia;
import transporte.dominio.repositorio.CategoriaVehiculoRepositorio;
import transporte.dominio.servicio.ServicioFlota;
import transporte.dominio.valor.Pagina;
import transporte.negocio.negocio.CategoriaVehiculoNegocio;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CategoriaVehiculoNegocioImpl implements CategoriaVehiculoNegocio {
    private final CategoriaVehiculoRepositorio repositorio;
    private final ServicioFlota flota;
    private final Clock reloj;

    public CategoriaVehiculoNegocioImpl(
            CategoriaVehiculoRepositorio repositorio, ServicioFlota flota, Clock reloj) {
        this.repositorio = repositorio;
        this.flota = flota;
        this.reloj = reloj;
    }

    @Override
    public CategoriaVehiculoDominio registrar(String nombre, String minima) {
        String nombreValido = CategoriaVehiculoDominio.normalizarNombre(nombre);
        CategoriaLicencia licencia = CategoriaLicencia.desdeTexto(minima);
        if (repositorio.existePorNombre(nombreValido)) {
            throw new NegocioExcepcion(
                    CONFLICTO, CatalogoMensajes.YA_EXISTE_UNA_CATEGORIA_DE_VEHICULO_CON);
        }
        return IdentidadDisponible.crearYGuardar(
                () ->
                        new CategoriaVehiculoDominio(
                                repositorio.nuevoIdentificador(), nombreValido, licencia),
                candidato -> repositorio.buscarPorId(candidato.getId()).isPresent(),
                repositorio::guardar);
    }

    @Override
    public CategoriaVehiculoDominio consultarPorId(UUID id) {
        return repositorio
                .buscarPorId(id)
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO,
                                        CatalogoMensajes.NO_SE_ENCONTRO_LA_CATEGORIA_DE_VEHICULO));
    }

    private @Nullable String nombreFiltro(@Nullable String nombre) {
        return nombre == null || nombre.isBlank()
                ? null
                : CategoriaVehiculoDominio.normalizarNombre(nombre);
    }

    private @Nullable CategoriaLicencia licenciaFiltro(@Nullable String minima) {
        return minima == null || minima.isBlank() ? null : CategoriaLicencia.desdeTexto(minima);
    }

    @Override
    public List<CategoriaVehiculoDominio> consultar(
            @Nullable String nombre, @Nullable String minima, Pagina pagina) {
        return repositorio.buscar(nombreFiltro(nombre), licenciaFiltro(minima), pagina);
    }

    @Override
    public long contar(@Nullable String nombre, @Nullable String minima) {
        return repositorio.contar(nombreFiltro(nombre), licenciaFiltro(minima));
    }

    @Override
    public CategoriaVehiculoDominio modificar(UUID id, String nombre, String minima) {
        var actual = consultarPorId(id);
        String nombreNormalizado = CategoriaVehiculoDominio.normalizarNombre(nombre);
        var licencia = CategoriaLicencia.desdeTexto(minima);
        boolean cambiaNombre = !actual.getNombre().equals(nombreNormalizado);
        if (cambiaNombre && repositorio.existePorNombre(nombreNormalizado)) {
            throw new NegocioExcepcion(
                    CONFLICTO, CatalogoMensajes.YA_EXISTE_UNA_CATEGORIA_CON_ESE_NOMBRE);
        }
        flota.cambiarCategoriaMinima(actual, licencia, LocalDate.now(reloj));
        actual.modificar(nombreNormalizado, licencia);
        repositorio.guardar(actual);
        return actual;
    }

    @Override
    public void eliminar(UUID id) {
        consultarPorId(id);
        if (repositorio.estaEnUso(id)) {
            throw new NegocioExcepcion(CONFLICTO, CatalogoMensajes.LA_CATEGORIA_ESTA_EN_USO);
        }
        repositorio.eliminar(id);
    }
}
