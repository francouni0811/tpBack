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

import backend.tpi.gestiondesolicitudes.domain.Contenedor;
import backend.tpi.gestiondesolicitudes.services.ContenedorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/contenedores")
public class ContenedorController {
    
    private final ContenedorService contenedorService;

    public ContenedorController(ContenedorService contenedorService) {
        this.contenedorService = contenedorService;
    }

    // GET todos
    @GetMapping()
    public ResponseEntity<List<Contenedor>> obtenerTodosContenedores() {

        List<Contenedor> contenedoresEncontrados = contenedorService.listarTodos();

        if (contenedoresEncontrados.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(contenedoresEncontrados);
    }

    // GET por id
    @GetMapping("/{id}")
    public ResponseEntity<Contenedor> obtenerContenedorPorId(@PathVariable Integer id) {
        Optional<Contenedor> contenedorEncontrado = contenedorService.buscarPorId(id);
        return contenedorEncontrado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST 
    @PostMapping()
    public ResponseEntity<Contenedor> crearContenedor(@Valid @RequestBody Contenedor nuevoContenedor) {
        Optional<Contenedor> contenedorCreado = contenedorService.guardar(nuevoContenedor);
        
        return contenedorCreado
            .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c)) // 201 si se creó
            .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build()); // 400 si no se pudo
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Contenedor> modificarContenedor(@PathVariable("id") Integer id,@Valid @RequestBody Contenedor contenedorActualizar) {
        Optional<Contenedor> contenedorActualizado = contenedorService.modificar(id, contenedorActualizar);

        return contenedorActualizado.map(c -> ResponseEntity.status(HttpStatus.OK).body(c)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarContenedor(@PathVariable Integer id) {
        boolean encontrado = contenedorService.existe(id);
        if (encontrado) {
            contenedorService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
