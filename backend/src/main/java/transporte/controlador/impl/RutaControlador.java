package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.Comandos.CambiarRecorrido;
import transporte.dto.Comandos.ModificarRuta;
import transporte.dto.Comandos.RegistrarRuta;
import transporte.dto.RutaDTO;
import transporte.negocio.fachada.RutaFachada;

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
@RequestMapping("/api/v1/rutas")
public class RutaControlador {
    private final RutaFachada fachada;

    public RutaControlador(RutaFachada rutaFachada) {
        fachada = rutaFachada;
    }

    @PostMapping
    public ResponseEntity<Respuesta<RutaDTO>> registrar(@RequestBody RegistrarRuta registrarRuta) {
        var registrado = fachada.registrar(registrarRuta);
        return ResponseEntity.created(URI.create("/api/v1/rutas/" + registrado.id()))
                .body(new Respuesta<>(registrado));
    }

    @PutMapping("/{id}")
    public Respuesta<RutaDTO> modificar(
            @PathVariable UUID id, @RequestBody ModificarRuta modificarRuta) {
        return new Respuesta<>(fachada.modificar(id, modificarRuta));
    }

    @PutMapping("/{id}/recorrido")
    public Respuesta<RutaDTO> recorrido(
            @PathVariable UUID id, @RequestBody CambiarRecorrido cambiarRecorrido) {
        return new Respuesta<>(fachada.recorrido(id, cambiarRecorrido));
    }

    @PostMapping("/{id}/desactivar")
    public Respuesta<RutaDTO> desactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estado(id, false));
    }

    @PostMapping("/{id}/reactivar")
    public Respuesta<RutaDTO> reactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estado(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        fachada.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
