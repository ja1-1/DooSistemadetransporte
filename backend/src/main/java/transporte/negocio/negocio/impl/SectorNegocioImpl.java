package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.SectorDominio;
import transporte.dominio.repositorio.SectorRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.negocio.negocio.MunicipioNegocio;
import transporte.negocio.negocio.SectorNegocio;
import transporte.negocio.solicitud.Solicitudes.ModificarSector;
import transporte.negocio.solicitud.Solicitudes.RegistrarSector;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SectorNegocioImpl implements SectorNegocio {
    private final SectorRepositorio sector;
    private final MunicipioNegocio municipiosNegocio;

    public SectorNegocioImpl(SectorRepositorio sector, MunicipioNegocio municipiosNegocio) {
        this.sector = sector;
        this.municipiosNegocio = municipiosNegocio;
    }

    @Override
    public SectorDominio consultarSector(UUID id) {
        return sector.buscarPorId(requerido(id, "id"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO, CatalogoMensajes.NO_SE_ENCONTRO_SECTOR));
    }

    @Override
    public SectorDominio registrarSector(RegistrarSector registrarSector) {
        requerido(registrarSector, "datos");
        SectorDominio nuevo =
                IdentidadDisponible.crear(
                        () ->
                                SectorDominio.registrar(
                                        registrarSector.nombre(),
                                        municipiosNegocio.consultarMunicipio(
                                                registrarSector.municipioId())),
                        candidato -> sector.buscarPorId(candidato.getId()).isPresent());
        sector.guardar(nuevo);
        return nuevo;
    }

    @Override
    public SectorDominio modificarSector(UUID id, ModificarSector modificarSector) {
        requerido(modificarSector, "datos");
        var actual = consultarSector(id);
        actual.modificar(modificarSector.nombre());
        sector.guardar(actual);
        return actual;
    }

    @Override
    public SectorDominio estadoSector(UUID id, boolean activo) {
        var actual = consultarSector(id);
        if (activo) {
            actual.reactivar();
        } else {
            actual.desactivar();
        }
        sector.guardar(actual);
        return actual;
    }

    @Override
    public void eliminarSector(UUID id) {
        consultarSector(id);
        estado(!sector.estaEnUso(id), CatalogoMensajes.EL_REGISTRO_ESTA_EN_USO_Y_NO);
        sector.eliminar(id);
    }

    @Override
    public List<SectorDominio> buscarSector(
            @Nullable String nombre,
            @Nullable UUID municipioId,
            @Nullable Boolean activo,
            Pagina pagina) {
        return sector.buscar(nombre, municipioId, activo, pagina);
    }
}
