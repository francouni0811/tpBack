package backend.tpi.gestiondesolicitudes.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.tpi.gestiondesolicitudes.domain.Tarifa;
import backend.tpi.gestiondesolicitudes.services.TarifaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/tarifas")
public class TarifaController {
    
    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {

        this.tarifaService = tarifaService;
    }

    //GET todos
    @GetMapping
    public ResponseEntity<List<Tarifa>> obtenerTodasTarifas() {

        List<Tarifa> tarifasEncontradas = tarifaService.listarTodos();

        if (tarifasEncontradas.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(tarifasEncontradas);
    }

    //GET por id
    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> obtenerTarifaPorId(@PathVariable Integer id) {
        Optional<Tarifa> tarifaEncontrada = tarifaService.buscarPorId(id);
        return tarifaEncontrada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //POST
    @PostMapping
    public ResponseEntity<Tarifa> crearTarifa(@Valid @RequestBody Tarifa tarifaNueva) {
        Tarifa tarifaCreada = tarifaService.guardar(tarifaNueva);

        if (tarifaCreada != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tarifaCreada); // 201 Created
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }

    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<Tarifa> modificarTarifa(@PathVariable("id") Integer id, @Valid @RequestBody Tarifa tarifaActualizar) {
        Optional<Tarifa> tarifaActualizada = tarifaService.modificar(id, tarifaActualizar);
        return tarifaActualizada.map(c -> ResponseEntity.status(HttpStatus.OK).body(c)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarTarifa(@PathVariable("id") Integer id) {
        boolean encontrada = tarifaService.existe(id);
        if (encontrada) {
            tarifaService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
