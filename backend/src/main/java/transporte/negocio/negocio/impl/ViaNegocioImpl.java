package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.ViaDominio;
import transporte.dominio.repositorio.ViaRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.negocio.negocio.ViaNegocio;
import transporte.negocio.solicitud.Solicitudes.ModificarVia;
import transporte.negocio.solicitud.Solicitudes.RegistrarVia;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ViaNegocioImpl implements ViaNegocio {
    private final ViaRepositorio via;

    public ViaNegocioImpl(ViaRepositorio via) {
        this.via = via;
    }

    @Override
    public ViaDominio consultarVia(UUID id) {
        return via.buscarPorId(requerido(id, "id"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO, CatalogoMensajes.NO_SE_ENCONTRO_VIA));
    }

    @Override
    public ViaDominio registrarVia(RegistrarVia registrarVia) {
        requerido(registrarVia, "datos");
        ViaDominio nuevo =
                IdentidadDisponible.crear(
                        () ->
                                ViaDominio.registrar(
                                        registrarVia.nombre(), registrarVia.descripcion()),
                        candidato -> via.buscarPorId(candidato.getId()).isPresent());
        via.guardar(nuevo);
        return nuevo;
    }

    @Override
    public ViaDominio modificarVia(UUID id, ModificarVia modificarVia) {
        requerido(modificarVia, "datos");
        var actual = consultarVia(id);
        actual.modificar(modificarVia.nombre(), modificarVia.descripcion());
        via.guardar(actual);
        return actual;
    }

    @Override
    public ViaDominio estadoVia(UUID id, boolean activo) {
        var actual = consultarVia(id);
        if (activo) {
            actual.reactivar();
        } else {
            actual.desactivar();
        }
        via.guardar(actual);
        return actual;
    }

    @Override
    public void eliminarVia(UUID id) {
        consultarVia(id);
        estado(!via.estaEnUso(id), CatalogoMensajes.EL_REGISTRO_ESTA_EN_USO_Y_NO);
        via.eliminar(id);
    }

    @Override
    public List<ViaDominio> buscarVia(
            @Nullable String nombre,
            @Nullable String descripcion,
            @Nullable Boolean activo,
            Pagina pagina) {
        return via.buscar(nombre, descripcion, activo, pagina);
    }
}
