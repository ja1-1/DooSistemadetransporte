package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.enums.EstadoAsignacion;
import transporte.dominio.valor.Periodo;
import transporte.transversal.mensajes.CatalogoMensajes;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public final class AsignacionDominio {
    private final UUID id, conductorId, vehiculoId;
    private Periodo periodo;

    public AsignacionDominio(UUID id, UUID conductorId, UUID vehiculoId, Periodo periodo) {
        this.id = requerido(id, "id");
        this.conductorId = requerido(conductorId, "conductorId");
        this.vehiculoId = requerido(vehiculoId, "vehiculoId");
        this.periodo = requerido(periodo, "periodo");
    }

    public static AsignacionDominio asignar(
            ConductorDominio conductor,
            VehiculoDominio vehiculo,
            CategoriaVehiculoDominio categoria,
            LocalDate fechaInicio) {
        requerido(conductor, "conductor");
        requerido(vehiculo, "vehiculo");
        requerido(categoria, "categoria");
        requerido(fechaInicio, "fechaInicio");
        estado(vehiculo.isActivo(), CatalogoMensajes.EL_VEHICULO_DEBE_ESTAR_ACTIVO);
        estado(
                vehiculo.getCategoriaVehiculoId().equals(categoria.getId()),
                CatalogoMensajes.LA_CATEGORIA_NO_CORRESPONDE_AL_VEHICULO);
        estado(
                conductor.estaHabilitadoPara(categoria.getCategoriaLicenciaMinima(), fechaInicio),
                CatalogoMensajes.EL_CONDUCTOR_NO_ESTA_HABILITADO_PARA_ESE);
        return new AsignacionDominio(
                UUID.randomUUID(),
                conductor.getId(),
                vehiculo.getId(),
                new Periodo(fechaInicio, Optional.empty()));
    }

    public void finalizar(LocalDate fechaFin) {
        estado(periodo.fin().isEmpty(), CatalogoMensajes.LA_ASIGNACION_YA_FUE_FINALIZADA);
        periodo = periodo.cerrarEn(fechaFin);
    }

    public EstadoAsignacion calcularEstado(LocalDate hoy) {
        requerido(hoy, "hoy");
        return periodo.fin().filter(hoy::isAfter).isPresent()
                ? EstadoAsignacion.FINALIZADA
                : EstadoAsignacion.VIGENTE;
    }

    public boolean estaVigenteDurante(Periodo periodo) {
        return this.periodo.contiene(periodo);
    }

    public UUID getId() {
        return id;
    }

    public UUID getConductorId() {
        return conductorId;
    }

    public UUID getVehiculoId() {
        return vehiculoId;
    }

    public Periodo getPeriodo() {
        return periodo;
    }
}
