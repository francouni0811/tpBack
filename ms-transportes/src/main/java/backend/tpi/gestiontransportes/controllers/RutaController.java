package backend.tpi.gestiontransportes.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.tpi.gestiontransportes.domain.Ruta;
import backend.tpi.gestiontransportes.services.RutaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/rutas")
public class RutaController {

    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    // GET /api/v1/rutas
    @GetMapping
    public ResponseEntity<List<Ruta>> obtenerTodos() {
        List<Ruta> lista = rutaService.listarTodos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    // GET /api/v1/rutas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Ruta> obtenerPorId(@PathVariable("id") Integer id) {
        Optional<Ruta> encontrado = rutaService.buscarPorId(id);
        return encontrado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /api/v1/rutas
    @PostMapping
    public ResponseEntity<Ruta> crear(@Valid @RequestBody Ruta nuevo) {
        Ruta guardado = rutaService.guardar(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // PUT /api/v1/rutas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Ruta> actualizar(@PathVariable("id") Integer id,
                                           @Valid @RequestBody Ruta actualizado) {
        Optional<Ruta> res = rutaService.modificar(id, actualizado);
        return res.map(r -> ResponseEntity.status(HttpStatus.OK).body(r))
                 .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE /api/v1/rutas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable("id") Integer id) {
        if (rutaService.existe(id)) {
            rutaService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

