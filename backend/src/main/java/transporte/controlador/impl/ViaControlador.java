package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.geografia.ModificarViaDTO;
import transporte.dto.geografia.RegistrarViaDTO;
import transporte.dto.geografia.ViaDTO;
import transporte.negocio.fachada.ViaFachada;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vias")
public class ViaControlador {
    private final ViaFachada fachada;

    public ViaControlador(ViaFachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public ResponseEntity<Respuesta<ViaDTO>> registrar(@RequestBody RegistrarViaDTO registrarVia) {
        var registrado = fachada.registrarVia(registrarVia);
        return ResponseEntity.created(URI.create("/api/v1/vias/" + registrado.id()))
                .body(new Respuesta<>(registrado));
    }

    @PutMapping("/{id}")
    public Respuesta<ViaDTO> modificar(
            @PathVariable UUID id, @RequestBody ModificarViaDTO modificarVia) {
        return new Respuesta<>(fachada.modificarVia(id, modificarVia));
    }

    @PostMapping("/{id}/desactivar")
    public Respuesta<ViaDTO> desactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoVia(id, false));
    }

    @PostMapping("/{id}/reactivar")
    public Respuesta<ViaDTO> reactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoVia(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        fachada.eliminarVia(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public Respuesta<ViaDTO> consultar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.consultarVia(id));
    }

    @GetMapping
    public Respuesta<List<ViaDTO>> buscar(
            @RequestParam(required = false) @Nullable String nombre,
            @RequestParam(required = false) @Nullable String descripcion,
            @RequestParam(required = false) @Nullable Boolean activo,
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {
        return new Respuesta<>(fachada.buscarVia(nombre, descripcion, activo, pagina, tamano));
    }
}
