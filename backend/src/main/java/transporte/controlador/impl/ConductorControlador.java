package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.Comandos.ModificarConductor;
import transporte.dto.Comandos.RegistrarConductor;
import transporte.dto.ConductorDTO;
import transporte.negocio.fachada.ConductorFachada;

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
@RequestMapping("/api/v1/conductores")
public class ConductorControlador {
    private final ConductorFachada fachada;

    public ConductorControlador(ConductorFachada flotaFachada) {
        fachada = flotaFachada;
    }

    @PostMapping
    public ResponseEntity<Respuesta<ConductorDTO>> registrar(
            @RequestBody RegistrarConductor registrarConductor) {
        var registrado = fachada.registrar(registrarConductor);
        return ResponseEntity.created(URI.create("/api/v1/conductores/" + registrado.id()))
                .body(new Respuesta<>(registrado));
    }

    @PutMapping("/{id}")
    public Respuesta<ConductorDTO> modificar(
            @PathVariable UUID id, @RequestBody ModificarConductor modificarConductor) {
        return new Respuesta<>(fachada.modificarConductor(id, modificarConductor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        fachada.eliminarConductor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/desactivar")
    public Respuesta<ConductorDTO> desactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoConductor(id, false));
    }

    @PostMapping("/{id}/reactivar")
    public Respuesta<ConductorDTO> reactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoConductor(id, true));
    }
}
