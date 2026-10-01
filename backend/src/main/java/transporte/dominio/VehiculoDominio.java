package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.dominio.valor.Placa;
import transporte.transversal.mensajes.CatalogoMensajes;

import java.time.LocalDate;
import java.util.UUID;

public final class VehiculoDominio {
    private static final int ANIO_MINIMO_FABRICACION = 1900;

    private final UUID id;
    private final Placa placa;
    private String marca, modelo;
    private int anioFabricacion, capacidadPasajeros;
    private UUID categoriaVehiculoId;
    private boolean activo;

    public VehiculoDominio(
            UUID id,
            Placa placa,
            String marca,
            String modelo,
            int anioFabricacion,
            int capacidadPasajeros,
            UUID categoriaVehiculoId,
            boolean activo) {
        this.id = requerido(id, "id");
        this.placa = requerido(placa, "placa");
        datos(marca, modelo, anioFabricacion, capacidadPasajeros, categoriaVehiculoId);
        this.activo = activo;
    }

    private void datos(String marca, String modelo, int anio, int capacidad, UUID categoria) {
        String marcaValidada = texto(marca, "marca", 1, 60);
        String modeloValidado = texto(modelo, "modelo", 1, 60);
        requerido(categoria, "categoriaVehiculoId");
        if (anio < ANIO_MINIMO_FABRICACION) {
            throw new IllegalArgumentException(CatalogoMensajes.EL_ANIO_MINIMO_ES_1900);
        }
        if (capacidad < 1) {
            throw new IllegalArgumentException(CatalogoMensajes.LA_CAPACIDAD_DEBE_SER_POSITIVA);
        }
        this.marca = marcaValidada;
        this.modelo = modeloValidado;
        anioFabricacion = anio;
        capacidadPasajeros = capacidad;
        categoriaVehiculoId = categoria;
    }

    private static void validarAnio(int anio, LocalDate hoy) {
        if (anio > requerido(hoy, "hoy").getYear()) {
            throw new IllegalArgumentException(CatalogoMensajes.EL_ANO_NO_PUEDE_SUPERAR_EL_ACTUAL);
        }
    }

    public static VehiculoDominio registrar(
            Placa placa,
            String marca,
            String modelo,
            int anioFabricacion,
            int capacidadPasajeros,
            UUID categoriaVehiculoId,
            LocalDate hoy) {
        validarAnio(anioFabricacion, hoy);
        return new VehiculoDominio(
                UUID.randomUUID(),
                placa,
                marca,
                modelo,
                anioFabricacion,
                capacidadPasajeros,
                categoriaVehiculoId,
                true);
    }

    public void modificar(
            String marca,
            String modelo,
            int anioFabricacion,
            int capacidadPasajeros,
            UUID categoriaVehiculoId,
            LocalDate hoy) {
        validarAnio(anioFabricacion, hoy);
        datos(marca, modelo, anioFabricacion, capacidadPasajeros, categoriaVehiculoId);
    }

    public void desactivar() {
        estado(activo, CatalogoMensajes.EL_VEHICULO_DEBE_ESTAR_ACTIVO);
        activo = false;
    }

    public void reactivar() {
        estado(!activo, CatalogoMensajes.EL_VEHICULO_DEBE_ESTAR_INACTIVO);
        activo = true;
    }

    public UUID getId() {
        return id;
    }

    public Placa getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public UUID getCategoriaVehiculoId() {
        return categoriaVehiculoId;
    }

    public boolean isActivo() {
        return activo;
    }
}
