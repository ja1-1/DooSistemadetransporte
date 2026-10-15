package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.LugarDominio;
import transporte.dominio.MunicipioDominio;
import transporte.dominio.RutaDominio;
import transporte.dominio.SectorDominio;
import transporte.dominio.ViaDominio;
import transporte.dominio.enums.TipoRuta;
import transporte.dominio.repositorio.RutaRepositorio;
import transporte.dominio.repositorio.SalidaRepositorio;
import transporte.negocio.negocio.LugarNegocio;
import transporte.negocio.negocio.MunicipioNegocio;
import transporte.negocio.negocio.RutaNegocio;
import transporte.negocio.negocio.SectorNegocio;
import transporte.negocio.negocio.ViaNegocio;
import transporte.negocio.solicitud.Solicitudes.CambiarRecorrido;
import transporte.negocio.solicitud.Solicitudes.ModificarRuta;
import transporte.negocio.solicitud.Solicitudes.RegistrarRuta;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RutaNegocioImpl implements RutaNegocio {
    private final RutaRepositorio rutas;
    private final SalidaRepositorio salidas;
    private final MunicipioNegocio municipioNegocio;
    private final SectorNegocio sectorNegocio;
    private final LugarNegocio lugarNegocio;
    private final ViaNegocio viaNegocio;

    public RutaNegocioImpl(
            RutaRepositorio repositorioRuta,
            SalidaRepositorio repositorioSalida,
            MunicipioNegocio municipioNegocio,
            SectorNegocio sectorNegocio,
            LugarNegocio lugarNegocio,
            ViaNegocio viaNegocio) {
        rutas = repositorioRuta;
        salidas = repositorioSalida;
        this.municipioNegocio = municipioNegocio;
        this.sectorNegocio = sectorNegocio;
        this.lugarNegocio = lugarNegocio;
        this.viaNegocio = viaNegocio;
    }

    @Override
    public RutaDominio consultar(UUID id) {
        return rutas.buscarPorId(requerido(id, "id"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO, CatalogoMensajes.NO_SE_ENCONTRO_LA_RUTA));
    }

    private record Geografia(
            LugarDominio origen,
            LugarDominio destino,
            ViaDominio via,
            Map<UUID, SectorDominio> sectores,
            Map<UUID, MunicipioDominio> municipios) {}

    private Geografia geografia(UUID origen, UUID destino, UUID via) {
        var lugarOrigen = lugarNegocio.consultarLugar(origen);
        var lugarDestino = lugarNegocio.consultarLugar(destino);
        var viaRuta = viaNegocio.consultarVia(via);
        var so = sectorNegocio.consultarSector(lugarOrigen.getSectorId());
        var sd = sectorNegocio.consultarSector(lugarDestino.getSectorId());
        var mo = municipioNegocio.consultarMunicipio(so.getMunicipioId());
        var md = municipioNegocio.consultarMunicipio(sd.getMunicipioId());
        var sectores = new HashMap<UUID, SectorDominio>();
        sectores.put(so.getId(), so);
        sectores.put(sd.getId(), sd);
        var municipios = new HashMap<UUID, MunicipioDominio>();
        municipios.put(mo.getId(), mo);
        municipios.put(md.getId(), md);
        return new Geografia(lugarOrigen, lugarDestino, viaRuta, sectores, municipios);
    }

    @Override
    public RutaDominio registrar(RegistrarRuta registrarRuta) {
        requerido(registrarRuta, "datos");
        var geografiaRuta =
                geografia(
                        registrarRuta.origenId(), registrarRuta.destinoId(), registrarRuta.viaId());
        RutaDominio ruta =
                IdentidadDisponible.crear(
                        () ->
                                RutaDominio.registrar(
                                        registrarRuta.descripcion(),
                                        TipoRuta.desdeId(registrarRuta.tipoRutaId()),
                                        geografiaRuta.origen(),
                                        geografiaRuta.destino(),
                                        geografiaRuta.via(),
                                        registrarRuta.distanciaTotalKm(),
                                        rutas.existePorRecorrido(
                                                registrarRuta.origenId(),
                                                registrarRuta.destinoId(),
                                                registrarRuta.viaId(),
                                                Optional.empty()),
                                        geografiaRuta.sectores(),
                                        geografiaRuta.municipios()),
                        candidato -> rutas.buscarPorId(candidato.getId()).isPresent());
        rutas.guardar(ruta);
        return ruta;
    }

    @Override
    public RutaDominio modificar(UUID id, ModificarRuta modificarRuta) {
        requerido(modificarRuta, "datos");
        var ruta = consultar(id);
        ruta.modificar(modificarRuta.descripcion());
        rutas.guardar(ruta);
        return ruta;
    }

    @Override
    public RutaDominio recorrido(UUID id, CambiarRecorrido cambiarRecorrido) {
        requerido(cambiarRecorrido, "datos");
        var ruta = consultar(id);
        var geografiaRuta =
                geografia(
                        cambiarRecorrido.origenId(),
                        cambiarRecorrido.destinoId(),
                        cambiarRecorrido.viaId());
        ruta.cambiarRecorrido(
                geografiaRuta.origen(),
                geografiaRuta.destino(),
                geografiaRuta.via(),
                cambiarRecorrido.distanciaTotalKm(),
                salidas.existenPendientesPorRuta(id),
                rutas.existePorRecorrido(
                        cambiarRecorrido.origenId(),
                        cambiarRecorrido.destinoId(),
                        cambiarRecorrido.viaId(),
                        Optional.of(id)),
                geografiaRuta.sectores(),
                geografiaRuta.municipios());
        rutas.guardar(ruta);
        return ruta;
    }

    @Override
    public RutaDominio estadoRuta(UUID id, boolean activa) {
        var ruta = consultar(id);
        if (activa) {
            ruta.reactivar();
        } else {
            ruta.desactivar();
        }
        rutas.guardar(ruta);
        return ruta;
    }

    @Override
    public void eliminar(UUID id) {
        consultar(id);
        estado(!rutas.estaEnUso(id), CatalogoMensajes.LA_RUTA_ESTA_EN_USO_Y_DEBE);
        rutas.eliminar(id);
    }
}
