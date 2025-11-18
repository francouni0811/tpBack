package backend.tpi.gestiontransportes.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import backend.tpi.gestiontransportes.domain.Tramo;
import backend.tpi.gestiontransportes.services.TramoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/tramos")
public class TramoController {

    private final TramoService tramoService;

    public TramoController(TramoService tramoService) {
        this.tramoService = tramoService;
    }

    // GET /api/v1/tramos
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TRANSPORTISTA')")
    public ResponseEntity<List<Tramo>> obtenerTodos() {
        List<Tramo> lista = tramoService.listarTodos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    // GET /api/v1/tramos/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tramo> obtenerPorId(@PathVariable("id") Integer id) {
        Optional<Tramo> encontrado = tramoService.buscarPorId(id);
        return encontrado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /api/v1/tramos
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tramo> crear(@Valid @RequestBody Tramo nuevo) {
        Tramo guardado = tramoService.guardar(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // PUT /api/v1/tramos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Tramo> actualizar(@PathVariable("id") Integer id,
                                            @Valid @RequestBody Tramo actualizado) {
        Optional<Tramo> res = tramoService.modificar(id, actualizado);
        return res.map(t -> ResponseEntity.status(HttpStatus.OK).body(t))
                 .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE /api/v1/tramos/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> borrar(@PathVariable("id") Integer id) {
        if (tramoService.existe(id)) {
            tramoService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{idTramo}/asignar-camion/{idCamion}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tramo> asignarCamion(@PathVariable("idCamion") Integer idCamion, @PathVariable("idTramo") Integer idTramo) {
        Tramo tramoActualizado = tramoService.asignarCamion(idTramo, idCamion);
        return ResponseEntity.ok(tramoActualizado);
    }

    @PatchMapping("{idTramo}/inicio")
    @PreAuthorize("hasRole('TRANSPORTISTA')")
    public ResponseEntity<Tramo> asignarInicio(@PathVariable Integer idTramo) {

        Tramo tramoActualizado = tramoService.iniciarTramo(idTramo);
        return ResponseEntity.ok(tramoActualizado);
    }

    @PatchMapping("{idTramo}/fin")
    @PreAuthorize("hasRole('TRANSPORTISTA')")
    public ResponseEntity<Tramo> asignarFin(@PathVariable Integer idTramo) {

        Tramo tramoActualizado = tramoService.finalizarTramo(idTramo);
        return ResponseEntity.ok(tramoActualizado);
    }

}

