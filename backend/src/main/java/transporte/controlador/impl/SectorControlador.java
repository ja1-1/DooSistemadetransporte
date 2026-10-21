package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.geografia.ModificarSectorDTO;
import transporte.dto.geografia.RegistrarSectorDTO;
import transporte.dto.geografia.SectorDTO;
import transporte.negocio.fachada.SectorFachada;

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
@RequestMapping("/api/v1/sectores")
public class SectorControlador {
    private final SectorFachada fachada;

    public SectorControlador(SectorFachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public ResponseEntity<Respuesta<SectorDTO>> registrar(
            @RequestBody RegistrarSectorDTO registrarSector) {
        var registrado = fachada.registrarSector(registrarSector);
        return ResponseEntity.created(URI.create("/api/v1/sectores/" + registrado.id()))
                .body(new Respuesta<>(registrado));
    }

    @PutMapping("/{id}")
    public Respuesta<SectorDTO> modificar(
            @PathVariable UUID id, @RequestBody ModificarSectorDTO modificarSector) {
        return new Respuesta<>(fachada.modificarSector(id, modificarSector));
    }

    @PostMapping("/{id}/desactivar")
    public Respuesta<SectorDTO> desactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoSector(id, false));
    }

    @PostMapping("/{id}/reactivar")
    public Respuesta<SectorDTO> reactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoSector(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        fachada.eliminarSector(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public Respuesta<SectorDTO> consultar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.consultarSector(id));
    }

    @GetMapping
    public Respuesta<List<SectorDTO>> buscar(
            @RequestParam(required = false) @Nullable String nombre,
            @RequestParam(required = false) @Nullable UUID municipioId,
            @RequestParam(required = false) @Nullable Boolean activo,
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {
        return new Respuesta<>(fachada.buscarSector(nombre, municipioId, activo, pagina, tamano));
    }
}
