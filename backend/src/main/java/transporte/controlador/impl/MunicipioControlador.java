package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.geografia.ModificarMunicipioDTO;
import transporte.dto.geografia.MunicipioDTO;
import transporte.dto.geografia.RegistrarMunicipioDTO;
import transporte.negocio.fachada.MunicipioFachada;

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
@RequestMapping("/api/v1/municipios")
public class MunicipioControlador {
    private final MunicipioFachada fachada;

    public MunicipioControlador(MunicipioFachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public ResponseEntity<Respuesta<MunicipioDTO>> registrar(
            @RequestBody RegistrarMunicipioDTO registrarMunicipio) {
        var registrado = fachada.registrarMunicipio(registrarMunicipio);
        return ResponseEntity.created(URI.create("/api/v1/municipios/" + registrado.id()))
                .body(new Respuesta<>(registrado));
    }

    @PutMapping("/{id}")
    public Respuesta<MunicipioDTO> modificar(
            @PathVariable UUID id, @RequestBody ModificarMunicipioDTO modificarMunicipio) {
        return new Respuesta<>(fachada.modificarMunicipio(id, modificarMunicipio));
    }

    @PostMapping("/{id}/desactivar")
    public Respuesta<MunicipioDTO> desactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoMunicipio(id, false));
    }

    @PostMapping("/{id}/reactivar")
    public Respuesta<MunicipioDTO> reactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoMunicipio(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        fachada.eliminarMunicipio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public Respuesta<MunicipioDTO> consultar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.consultarMunicipio(id));
    }

    @GetMapping
    public Respuesta<List<MunicipioDTO>> buscar(
            @RequestParam(required = false) @Nullable String nombre,
            @RequestParam(required = false) @Nullable String departamento,
            @RequestParam(required = false) @Nullable Boolean activo,
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {
        return new Respuesta<>(
                fachada.buscarMunicipio(nombre, departamento, activo, pagina, tamano));
    }
}
