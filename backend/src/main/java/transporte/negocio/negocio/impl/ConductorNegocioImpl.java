package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.ConductorDominio;
import transporte.dominio.enums.CategoriaLicencia;
import transporte.dominio.enums.TipoSangre;
import transporte.dominio.repositorio.ConductorRepositorio;
import transporte.dominio.servicio.ServicioFlota;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Licencia;
import transporte.dominio.valor.Telefono;
import transporte.negocio.negocio.ConductorNegocio;
import transporte.negocio.solicitud.Solicitudes.ModificarConductor;
import transporte.negocio.solicitud.Solicitudes.RegistrarConductor;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConductorNegocioImpl implements ConductorNegocio {
    private final ConductorRepositorio conductores;
    private final ServicioFlota servicio;
    private final Clock reloj;

    public ConductorNegocioImpl(
            ConductorRepositorio conductores, ServicioFlota servicio, Clock reloj) {
        this.conductores = conductores;
        this.servicio = servicio;
        this.reloj = reloj;
    }

    @Override
    public ConductorDominio conductor(UUID id) {
        return conductores
                .buscarPorId(requerido(id, "id"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO,
                                        CatalogoMensajes.NO_SE_ENCONTRO_EL_CONDUCTOR));
    }

    @Override
    public ConductorDominio registrar(RegistrarConductor registrarConductor) {
        requerido(registrarConductor, "datos");
        estado(
                !conductores.existePorCedula(new Cedula(registrarConductor.cedula())),
                CatalogoMensajes.LA_CEDULA_YA_ESTA_REGISTRADA);
        estado(
                !conductores.existePorNumeroLicencia(registrarConductor.numeroLicencia()),
                CatalogoMensajes.LA_LICENCIA_YA_ESTA_REGISTRADA);
        ConductorDominio conductorDominio =
                IdentidadDisponible.crear(
                        () ->
                                ConductorDominio.registrar(
                                        new Cedula(registrarConductor.cedula()),
                                        registrarConductor.primerNombre(),
                                        Optional.ofNullable(registrarConductor.segundoNombre()),
                                        registrarConductor.primerApellido(),
                                        Optional.ofNullable(registrarConductor.segundoApellido()),
                                        registrarConductor.fechaNacimiento(),
                                        new Telefono(registrarConductor.numeroTelefono()),
                                        TipoSangre.desdeSimbolo(registrarConductor.tipoSangre()),
                                        licencia(
                                                registrarConductor.numeroLicencia(),
                                                registrarConductor.categoriaLicencia(),
                                                registrarConductor.fechaVencimientoLicencia()),
                                        LocalDate.now(reloj)),
                        candidato -> conductores.buscarPorId(candidato.getId()).isPresent());
        conductores.guardar(conductorDominio);
        return conductorDominio;
    }

    @Override
    public ConductorDominio modificar(UUID id, ModificarConductor modificarConductor) {
        requerido(modificarConductor, "datos");
        var conductorDominio = conductor(id);
        var licenciaNueva =
                licencia(
                        modificarConductor.numeroLicencia(),
                        modificarConductor.categoriaLicencia(),
                        modificarConductor.fechaVencimientoLicencia());
        servicio.cambiarLicencia(conductorDominio, licenciaNueva, LocalDate.now(reloj));
        conductorDominio.modificar(
                modificarConductor.primerNombre(),
                Optional.ofNullable(modificarConductor.segundoNombre()),
                modificarConductor.primerApellido(),
                Optional.ofNullable(modificarConductor.segundoApellido()),
                new Telefono(modificarConductor.numeroTelefono()),
                TipoSangre.desdeSimbolo(modificarConductor.tipoSangre()),
                licenciaNueva,
                LocalDate.now(reloj));
        conductores.guardar(conductorDominio);
        return conductorDominio;
    }

    @Override
    public ConductorDominio estadoConductor(UUID id, boolean activo) {
        var conductorDominio = conductor(id);
        if (activo) {
            conductorDominio.reactivar();
        } else {
            servicio.desactivarConductor(conductorDominio, LocalDateTime.now(reloj));
        }
        conductores.guardar(conductorDominio);
        return conductorDominio;
    }

    @Override
    public void eliminarConductor(UUID id) {
        conductor(id);
        estado(!conductores.estaEnUso(id), CatalogoMensajes.EL_CONDUCTOR_ESTA_EN_USO_CONSERVA_SU);
        conductores.eliminar(id);
    }

    private Licencia licencia(String numero, String categoria, LocalDate fechaVencimiento) {
        return new Licencia(numero, CategoriaLicencia.desdeTexto(categoria), fechaVencimiento);
    }
}
