package transporte.negocio.fachada.impl;

import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.valor.Pagina;
import transporte.dto.flota.CategoriaVehiculoDTO;
import transporte.dto.flota.ListadoCategoriasDTO;
import transporte.dto.flota.RegistrarCategoriaVehiculoDTO;
import transporte.negocio.fachada.CategoriaVehiculoFachada;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.assembler.impl.CategoriaVehiculoDTOAssembler;
import transporte.negocio.negocio.CategoriaVehiculoNegocio;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CategoriaVehiculoFachadaImpl implements CategoriaVehiculoFachada {
    private final CategoriaVehiculoNegocio negocio;
    private final CategoriaVehiculoDTOAssembler assembler;

    public CategoriaVehiculoFachadaImpl(
            CategoriaVehiculoNegocio negocio, CategoriaVehiculoDTOAssembler assembler) {
        this.negocio = negocio;
        this.assembler = assembler;
    }

    @Override
    @Transactional
    @Escritura
    public CategoriaVehiculoDTO registrar(RegistrarCategoriaVehiculoDTO datos) {
        if (datos == null) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.DEBES_ENVIAR_LOS_DATOS_DE_LA_CATEGORIA);
        }
        return assembler.convertirADTO(
                negocio.registrar(
                        requerido(datos.nombre(), "nombre"),
                        requerido(datos.categoriaLicenciaMinima(), "categoriaLicenciaMinima")));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public CategoriaVehiculoDTO consultarPorId(UUID id) {
        return assembler.convertirADTO(negocio.consultarPorId(id));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public ListadoCategoriasDTO consultar(
            @Nullable String nombre, @Nullable String minima, int numero, int tamano) {
        var pagina = new Pagina(numero, tamano);
        var datos =
                negocio.consultar(nombre, minima, pagina).stream()
                        .map(assembler::convertirADTO)
                        .toList();
        return new ListadoCategoriasDTO(datos, negocio.contar(nombre, minima), numero, tamano);
    }

    @Override
    @Transactional
    @Escritura
    public CategoriaVehiculoDTO modificar(
            UUID id, RegistrarCategoriaVehiculoDTO registrarCategoriaVehiculo) {
        if (registrarCategoriaVehiculo == null) {
            throw new IllegalArgumentException(CatalogoMensajes.LOS_DATOS_SON_OBLIGATORIOS);
        }
        return assembler.convertirADTO(
                negocio.modificar(
                        id,
                        requerido(registrarCategoriaVehiculo.nombre(), "nombre"),
                        requerido(
                                registrarCategoriaVehiculo.categoriaLicenciaMinima(),
                                "categoriaLicenciaMinima")));
    }

    @Override
    @Transactional
    @Escritura
    public void eliminar(UUID id) {
        negocio.eliminar(id);
    }
}
