package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.geografia.LugarDTO;
import transporte.dto.geografia.ModificarLugarDTO;
import transporte.dto.geografia.RegistrarLugarDTO;
import transporte.negocio.fachada.LugarFachada;

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
@RequestMapping("/api/v1/lugares")
public class LugarControlador {
    private final LugarFachada fachada;

    public LugarControlador(LugarFachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public ResponseEntity<Respuesta<LugarDTO>> registrar(
            @RequestBody RegistrarLugarDTO registrarLugar) {
        var registrado = fachada.registrarLugar(registrarLugar);
        return ResponseEntity.created(URI.create("/api/v1/lugares/" + registrado.id()))
                .body(new Respuesta<>(registrado));
    }

    @PutMapping("/{id}")
    public Respuesta<LugarDTO> modificar(
            @PathVariable UUID id, @RequestBody ModificarLugarDTO modificarLugar) {
        return new Respuesta<>(fachada.modificarLugar(id, modificarLugar));
    }

    @PostMapping("/{id}/desactivar")
    public Respuesta<LugarDTO> desactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoLugar(id, false));
    }

    @PostMapping("/{id}/reactivar")
    public Respuesta<LugarDTO> reactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoLugar(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        fachada.eliminarLugar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public Respuesta<LugarDTO> consultar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.consultarLugar(id));
    }

    @GetMapping
    public Respuesta<List<LugarDTO>> buscar(
            @RequestParam(required = false) @Nullable String nombre,
            @RequestParam(required = false) @Nullable UUID sectorId,
            @RequestParam(required = false) @Nullable Boolean activo,
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {
        return new Respuesta<>(fachada.buscarLugar(nombre, sectorId, activo, pagina, tamano));
    }
}
