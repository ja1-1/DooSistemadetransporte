package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.MunicipioDominio;
import transporte.dominio.repositorio.MunicipioRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.negocio.negocio.MunicipioNegocio;
import transporte.negocio.solicitud.Solicitudes.ModificarMunicipio;
import transporte.negocio.solicitud.Solicitudes.RegistrarMunicipio;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MunicipioNegocioImpl implements MunicipioNegocio {
    private final MunicipioRepositorio municipio;

    public MunicipioNegocioImpl(MunicipioRepositorio municipio) {
        this.municipio = municipio;
    }

    @Override
    public MunicipioDominio consultarMunicipio(UUID id) {
        return municipio
                .buscarPorId(requerido(id, "id"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO, CatalogoMensajes.NO_SE_ENCONTRO_MUNICIPIO));
    }

    @Override
    public MunicipioDominio registrarMunicipio(RegistrarMunicipio registrarMunicipio) {
        requerido(registrarMunicipio, "datos");
        MunicipioDominio nuevo =
                IdentidadDisponible.crear(
                        () ->
                                MunicipioDominio.registrar(
                                        registrarMunicipio.nombre(),
                                        registrarMunicipio.departamento()),
                        candidato -> municipio.buscarPorId(candidato.getId()).isPresent());
        municipio.guardar(nuevo);
        return nuevo;
    }

    @Override
    public MunicipioDominio modificarMunicipio(UUID id, ModificarMunicipio modificarMunicipio) {
        requerido(modificarMunicipio, "datos");
        var actual = consultarMunicipio(id);
        actual.modificar(modificarMunicipio.nombre(), modificarMunicipio.departamento());
        municipio.guardar(actual);
        return actual;
    }

    @Override
    public MunicipioDominio estadoMunicipio(UUID id, boolean activo) {
        var actual = consultarMunicipio(id);
        if (activo) {
            actual.reactivar();
        } else {
            actual.desactivar();
        }
        municipio.guardar(actual);
        return actual;
    }

    @Override
    public void eliminarMunicipio(UUID id) {
        consultarMunicipio(id);
        estado(!municipio.estaEnUso(id), CatalogoMensajes.EL_REGISTRO_ESTA_EN_USO_Y_NO);
        municipio.eliminar(id);
    }

    @Override
    public List<MunicipioDominio> buscarMunicipio(
            @Nullable String nombre,
            @Nullable String departamento,
            @Nullable Boolean activo,
            Pagina pagina) {
        return municipio.buscar(nombre, departamento, activo, pagina);
    }
}
