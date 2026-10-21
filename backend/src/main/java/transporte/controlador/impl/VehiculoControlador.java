package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.Comandos.ModificarVehiculo;
import transporte.dto.Comandos.RegistrarVehiculo;
import transporte.dto.VehiculoDTO;
import transporte.negocio.fachada.VehiculoFachada;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehiculos")
public class VehiculoControlador {
    private final VehiculoFachada fachada;

    public VehiculoControlador(VehiculoFachada flotaFachada) {
        fachada = flotaFachada;
    }

    @PostMapping
    public ResponseEntity<Respuesta<VehiculoDTO>> registrar(
            @RequestBody RegistrarVehiculo registrarVehiculo) {
        var registrado = fachada.registrar(registrarVehiculo);
        return ResponseEntity.created(URI.create("/api/v1/vehiculos/" + registrado.id()))
                .body(new Respuesta<>(registrado));
    }

    @PutMapping("/{id}")
    public Respuesta<VehiculoDTO> modificar(
            @PathVariable UUID id, @RequestBody ModificarVehiculo modificarVehiculo) {
        return new Respuesta<>(fachada.modificarVehiculo(id, modificarVehiculo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        fachada.eliminarVehiculo(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/desactivar")
    public Respuesta<VehiculoDTO> desactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoVehiculo(id, false));
    }

    @PostMapping("/{id}/reactivar")
    public Respuesta<VehiculoDTO> reactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoVehiculo(id, true));
    }
}
