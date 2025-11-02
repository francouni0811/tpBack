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

import backend.tpi.gestiondesolicitudes.domain.Solicitud;
import backend.tpi.gestiondesolicitudes.services.SolicitudService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    /*
    * TODO: ENDPOINTS ESPECIFICOS DE SOLICITUDES
    * 
    * 
    * 
    * 
    * 
    */

    // GET TODOS
    @GetMapping
    public ResponseEntity<List<Solicitud>> obtenerTodasSolicitudes() {

        List<Solicitud> solicitudesEncontradas = solicitudService.listarTodos();
        if (solicitudesEncontradas.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(solicitudesEncontradas);
    }

    // GET id
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerSolicitudPorId(@PathVariable("id") Integer id) {
        Optional<Solicitud> solicitudEncontrada = solicitudService.buscarPorId(id);
        return solicitudEncontrada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //POST
    @PostMapping
    public ResponseEntity<Solicitud> crearSolicitud(@Valid @RequestBody Solicitud nuevaSolicitud) {
        Solicitud solicitudCreada = solicitudService.guardar(nuevaSolicitud);
        if (solicitudCreada != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(solicitudCreada); // 201 Created
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> modificarSolicitud(@PathVariable("id") Integer id, @Valid @RequestBody Solicitud solicitudActualizar) {
        Optional<Solicitud> solicitudActualizada = solicitudService.modificar(id, solicitudActualizar);
        return solicitudActualizada.map(c -> ResponseEntity.status(HttpStatus.OK).body(c)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarSolicitud(@PathVariable("id") Integer id) {
        boolean encontrada = solicitudService.existe(id);
        if (encontrada) {
            solicitudService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
