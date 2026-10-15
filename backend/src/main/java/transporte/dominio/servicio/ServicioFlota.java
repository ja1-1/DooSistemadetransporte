package transporte.dominio.servicio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.AsignacionDominio;
import transporte.dominio.CategoriaVehiculoDominio;
import transporte.dominio.ConductorDominio;
import transporte.dominio.VehiculoDominio;
import transporte.dominio.consulta.FiltroConductor;
import transporte.dominio.consulta.FiltroVehiculo;
import transporte.dominio.enums.CategoriaLicencia;
import transporte.dominio.enums.EstadoAsignacion;
import transporte.dominio.enums.EstadoSalida;
import transporte.dominio.repositorio.AsignacionRepositorio;
import transporte.dominio.repositorio.CategoriaVehiculoRepositorio;
import transporte.dominio.repositorio.ConductorRepositorio;
import transporte.dominio.repositorio.SalidaRepositorio;
import transporte.dominio.repositorio.VehiculoRepositorio;
import transporte.dominio.valor.Licencia;
import transporte.transversal.mensajes.CatalogoMensajes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class ServicioFlota {
    private final AsignacionRepositorio asignaciones;
    private final SalidaRepositorio salidas;
    private final VehiculoRepositorio vehiculos;
    private final ConductorRepositorio conductores;
    private final CategoriaVehiculoRepositorio categorias;

    public ServicioFlota(
            AsignacionRepositorio asignaciones,
            SalidaRepositorio salidas,
            VehiculoRepositorio vehiculos,
            ConductorRepositorio conductores,
            CategoriaVehiculoRepositorio categorias) {
        this.asignaciones = Objects.requireNonNull(asignaciones);
        this.salidas = Objects.requireNonNull(salidas);
        this.vehiculos = Objects.requireNonNull(vehiculos);
        this.conductores = Objects.requireNonNull(conductores);
        this.categorias = Objects.requireNonNull(categorias);
    }

    public void cambiarLicencia(ConductorDominio conductor, Licencia licencia, LocalDate hoy) {
        requerido(licencia, "licencia");
        estado(
                conductores
                        .relacionados(FiltroConductor.todos().conNumeroLicencia(licencia.numero()))
                        .stream()
                        .allMatch(
                                conductorRecibido ->
                                        conductorRecibido.getId().equals(conductor.getId())),
                CatalogoMensajes.EL_NUMERO_DE_LICENCIA_YA_ESTA_REGISTRADO);
        for (var asignacionRegistrada : asignaciones.buscarPorConductor(conductor.getId())) {
            if (asignacionRegistrada.calcularEstado(hoy) == EstadoAsignacion.VIGENTE) {
                var vehiculoAsignado =
                        vehiculos.buscarPorId(asignacionRegistrada.getVehiculoId()).orElseThrow();
                var cat =
                        categorias
                                .buscarPorId(vehiculoAsignado.getCategoriaVehiculoId())
                                .orElseThrow();
                estado(
                        licencia.cubre(cat.getCategoriaLicenciaMinima()),
                        CatalogoMensajes.LA_LICENCIA_NO_CUBRE_UNA_ASIGNACION_VIGENTE);
            }
            for (var salidaAsignada :
                    salidas.buscarNoCanceladasPorAsignacion(asignacionRegistrada.getId())) {
                if (salidaAsignada.getEstadoSalida() == EstadoSalida.PROGRAMADA) {
                    estado(
                            licencia.estaVigenteEn(
                                    salidaAsignada
                                            .getVentana()
                                            .instanteSalidaProgramado()
                                            .toLocalDate()),
                            CatalogoMensajes.LA_LICENCIA_DEBE_ESTAR_VIGENTE_EN_LAS);
                }
            }
        }
        conductor.modificar(
                conductor.getPrimerNombre(),
                conductor.getSegundoNombre(),
                conductor.getPrimerApellido(),
                conductor.getSegundoApellido(),
                conductor.getNumeroTelefono(),
                conductor.getTipoSangre(),
                licencia,
                hoy);
    }

    private boolean pendientes(List<AsignacionDominio> lista) {
        return lista.stream()
                .flatMap(
                        asignacion ->
                                salidas
                                        .buscarNoCanceladasPorAsignacion(asignacion.getId())
                                        .stream())
                .anyMatch(salida -> !salida.getEstadoSalida().esDefinitivo());
    }

    public void desactivarConductor(ConductorDominio conductor, LocalDateTime ahora) {
        requerido(ahora, "ahora");
        estado(
                !pendientes(asignaciones.buscarPorConductor(conductor.getId())),
                CatalogoMensajes.EL_CONDUCTOR_TIENE_SALIDAS_PROGRAMADAS_O_EN);
        conductor.desactivar();
    }

    public void desactivarVehiculo(VehiculoDominio vehiculo, LocalDateTime ahora) {
        requerido(ahora, "ahora");
        estado(
                !pendientes(asignaciones.buscarPorVehiculo(vehiculo.getId())),
                CatalogoMensajes.EL_VEHICULO_TIENE_SALIDAS_PROGRAMADAS_O_EN);
        vehiculo.desactivar();
    }

    public void cambiarCategoriaDeVehiculo(
            VehiculoDominio vehiculo, CategoriaVehiculoDominio categoria, LocalDate hoy) {
        for (var asignacionRegistrada : asignaciones.buscarPorVehiculo(vehiculo.getId())) {
            if (asignacionRegistrada.calcularEstado(hoy) == EstadoAsignacion.VIGENTE) {
                estado(
                        conductores
                                .buscarPorId(asignacionRegistrada.getConductorId())
                                .orElseThrow()
                                .getLicencia()
                                .cubre(categoria.getCategoriaLicenciaMinima()),
                        CatalogoMensajes.UN_CONDUCTOR_ASIGNADO_NO_CUBRE_LA_CATEGORIA);
            }
        }
        vehiculo.modificar(
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnioFabricacion(),
                vehiculo.getCapacidadPasajeros(),
                categoria.getId(),
                hoy);
    }

    public void cambiarCategoriaMinima(
            CategoriaVehiculoDominio categoria,
            CategoriaLicencia categoriaLicenciaMinima,
            LocalDate hoy) {
        for (var vehiculoAsignado :
                vehiculos.relacionados(
                        FiltroVehiculo.todos().conCategoriaVehiculoId(categoria.getId()))) {
            for (var asignacionRegistrada :
                    asignaciones.buscarPorVehiculo(vehiculoAsignado.getId())) {
                if (asignacionRegistrada.calcularEstado(hoy) == EstadoAsignacion.VIGENTE) {
                    estado(
                            conductores
                                    .buscarPorId(asignacionRegistrada.getConductorId())
                                    .orElseThrow()
                                    .getLicencia()
                                    .cubre(categoriaLicenciaMinima),
                            CatalogoMensajes.UN_CONDUCTOR_ASIGNADO_NO_CUBRE_LA_LICENCIA);
                }
            }
        }
        categoria.modificar(categoria.getNombre(), categoriaLicenciaMinima);
    }
}
