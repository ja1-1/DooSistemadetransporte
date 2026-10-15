package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.LugarDominio;
import transporte.dominio.repositorio.LugarRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.negocio.negocio.LugarNegocio;
import transporte.negocio.negocio.MunicipioNegocio;
import transporte.negocio.negocio.SectorNegocio;
import transporte.negocio.solicitud.Solicitudes.ModificarLugar;
import transporte.negocio.solicitud.Solicitudes.RegistrarLugar;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LugarNegocioImpl implements LugarNegocio {
    private final LugarRepositorio lugar;
    private final MunicipioNegocio municipiosNegocio;
    private final SectorNegocio sectoresNegocio;

    public LugarNegocioImpl(
            LugarRepositorio lugar,
            MunicipioNegocio municipiosNegocio,
            SectorNegocio sectoresNegocio) {
        this.lugar = lugar;
        this.municipiosNegocio = municipiosNegocio;
        this.sectoresNegocio = sectoresNegocio;
    }

    @Override
    public LugarDominio consultarLugar(UUID id) {
        return lugar.buscarPorId(requerido(id, "id"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO, CatalogoMensajes.NO_SE_ENCONTRO_LUGAR));
    }

    @Override
    public LugarDominio registrarLugar(RegistrarLugar registrarLugar) {
        requerido(registrarLugar, "datos");
        LugarDominio nuevo =
                IdentidadDisponible.crear(
                        () ->
                                LugarDominio.registrar(
                                        registrarLugar.nombre(),
                                        sectoresNegocio.consultarSector(registrarLugar.sectorId()),
                                        municipiosNegocio.consultarMunicipio(
                                                sectoresNegocio
                                                        .consultarSector(registrarLugar.sectorId())
                                                        .getMunicipioId())),
                        candidato -> lugar.buscarPorId(candidato.getId()).isPresent());
        lugar.guardar(nuevo);
        return nuevo;
    }

    @Override
    public LugarDominio modificarLugar(UUID id, ModificarLugar modificarLugar) {
        requerido(modificarLugar, "datos");
        var actual = consultarLugar(id);
        actual.modificar(modificarLugar.nombre());
        lugar.guardar(actual);
        return actual;
    }

    @Override
    public LugarDominio estadoLugar(UUID id, boolean activo) {
        var actual = consultarLugar(id);
        if (activo) {
            actual.reactivar();
        } else {
            actual.desactivar();
        }
        lugar.guardar(actual);
        return actual;
    }

    @Override
    public void eliminarLugar(UUID id) {
        consultarLugar(id);
        estado(!lugar.estaEnUso(id), CatalogoMensajes.EL_REGISTRO_ESTA_EN_USO_Y_NO);
        lugar.eliminar(id);
    }

    @Override
    public List<LugarDominio> buscarLugar(
            @Nullable String nombre,
            @Nullable UUID sectorId,
            @Nullable Boolean activo,
            Pagina pagina) {
        return lugar.buscar(nombre, sectorId, activo, pagina);
    }
}
