package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.CategoriaVehiculoDominio;
import transporte.dominio.VehiculoDominio;
import transporte.dominio.repositorio.CategoriaVehiculoRepositorio;
import transporte.dominio.repositorio.VehiculoRepositorio;
import transporte.dominio.servicio.ServicioFlota;
import transporte.dominio.valor.Placa;
import transporte.negocio.negocio.VehiculoNegocio;
import transporte.negocio.solicitud.Solicitudes.ModificarVehiculo;
import transporte.negocio.solicitud.Solicitudes.RegistrarVehiculo;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VehiculoNegocioImpl implements VehiculoNegocio {
    private final VehiculoRepositorio vehiculos;
    private final CategoriaVehiculoRepositorio categorias;
    private final ServicioFlota servicio;
    private final Clock reloj;

    public VehiculoNegocioImpl(
            VehiculoRepositorio vehiculos,
            CategoriaVehiculoRepositorio categorias,
            ServicioFlota servicio,
            Clock reloj) {
        this.vehiculos = vehiculos;
        this.categorias = categorias;
        this.servicio = servicio;
        this.reloj = reloj;
    }

    @Override
    public VehiculoDominio vehiculo(UUID id) {
        return vehiculos
                .buscarPorId(requerido(id, "id"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO,
                                        CatalogoMensajes.NO_SE_ENCONTRO_EL_VEHICULO));
    }

    @Override
    public VehiculoDominio registrar(RegistrarVehiculo registrarVehiculo) {
        requerido(registrarVehiculo, "datos");
        categoria(registrarVehiculo.categoriaVehiculoId());
        var placaValidada = new Placa(registrarVehiculo.placa());
        estado(
                !vehiculos.existePorPlaca(placaValidada),
                CatalogoMensajes.LA_PLACA_YA_ESTA_REGISTRADA);
        VehiculoDominio vehiculoDominio =
                IdentidadDisponible.crear(
                        () ->
                                VehiculoDominio.registrar(
                                        placaValidada,
                                        registrarVehiculo.marca(),
                                        registrarVehiculo.modelo(),
                                        requerido(
                                                registrarVehiculo.anioFabricacion(),
                                                "anioFabricacion"),
                                        requerido(
                                                registrarVehiculo.capacidadPasajeros(),
                                                "capacidadPasajeros"),
                                        registrarVehiculo.categoriaVehiculoId(),
                                        LocalDate.now(reloj)),
                        candidato -> vehiculos.buscarPorId(candidato.getId()).isPresent());
        vehiculos.guardar(vehiculoDominio);
        return vehiculoDominio;
    }

    @Override
    public VehiculoDominio modificar(UUID id, ModificarVehiculo modificarVehiculo) {
        requerido(modificarVehiculo, "datos");
        var vehiculoDominio = vehiculo(id);
        var cat = categoria(modificarVehiculo.categoriaVehiculoId());
        servicio.cambiarCategoriaDeVehiculo(vehiculoDominio, cat, LocalDate.now(reloj));
        vehiculoDominio.modificar(
                modificarVehiculo.marca(),
                modificarVehiculo.modelo(),
                requerido(modificarVehiculo.anioFabricacion(), "anioFabricacion"),
                requerido(modificarVehiculo.capacidadPasajeros(), "capacidadPasajeros"),
                cat.getId(),
                LocalDate.now(reloj));
        vehiculos.guardar(vehiculoDominio);
        return vehiculoDominio;
    }

    @Override
    public VehiculoDominio estadoVehiculo(UUID id, boolean activo) {
        var vehiculoDominio = vehiculo(id);
        if (activo) {
            vehiculoDominio.reactivar();
        } else {
            servicio.desactivarVehiculo(vehiculoDominio, LocalDateTime.now(reloj));
        }
        vehiculos.guardar(vehiculoDominio);
        return vehiculoDominio;
    }

    @Override
    public void eliminarVehiculo(UUID id) {
        vehiculo(id);
        estado(!vehiculos.estaEnUso(id), CatalogoMensajes.EL_VEHICULO_ESTA_EN_USO_CONSERVA_SU);
        vehiculos.eliminar(id);
    }

    private CategoriaVehiculoDominio categoria(UUID id) {
        return categorias
                .buscarPorId(requerido(id, "categoriaVehiculoId"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO,
                                        CatalogoMensajes.NO_SE_ENCONTRO_LA_CATEGORIA));
    }
}
