package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.flota.CategoriaVehiculoDTO;
import transporte.dto.flota.ListadoCategoriasDTO;
import transporte.dto.flota.RegistrarCategoriaVehiculoDTO;
import transporte.negocio.fachada.CategoriaVehiculoFachada;

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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categorias-vehiculo")
public class CategoriaVehiculoControlador {
    private final CategoriaVehiculoFachada fachada;

    public CategoriaVehiculoControlador(CategoriaVehiculoFachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public ResponseEntity<Respuesta<CategoriaVehiculoDTO>> registrar(
            @RequestBody RegistrarCategoriaVehiculoDTO datos) {
        var categoria = fachada.registrar(datos);
        return ResponseEntity.created(URI.create("/api/v1/categorias-vehiculo/" + categoria.id()))
                .body(new Respuesta<>(categoria));
    }

    @GetMapping("/{id}")
    public Respuesta<CategoriaVehiculoDTO> consultarPorId(@PathVariable UUID id) {
        return new Respuesta<>(fachada.consultarPorId(id));
    }

    @GetMapping
    public ListadoCategoriasDTO consultar(
            @RequestParam(required = false) @Nullable String nombre,
            @RequestParam(required = false) @Nullable String categoriaLicenciaMinima,
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {
        return fachada.consultar(nombre, categoriaLicenciaMinima, pagina, tamano);
    }

    @PutMapping("/{id}")
    public Respuesta<CategoriaVehiculoDTO> modificar(
            @PathVariable UUID id,
            @RequestBody RegistrarCategoriaVehiculoDTO registrarCategoriaVehiculo) {
        return new Respuesta<>(fachada.modificar(id, registrarCategoriaVehiculo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        fachada.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
