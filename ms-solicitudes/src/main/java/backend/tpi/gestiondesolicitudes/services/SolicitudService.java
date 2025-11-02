package backend.tpi.gestiondesolicitudes.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import backend.tpi.gestiondesolicitudes.domain.Solicitud;
import backend.tpi.gestiondesolicitudes.repositorios.ClienteRepository;
import backend.tpi.gestiondesolicitudes.repositorios.ContenedorRepository;
import backend.tpi.gestiondesolicitudes.repositorios.SolicitudRepository;
import backend.tpi.gestiondesolicitudes.repositorios.TarifaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;
    private final TarifaRepository tarifaRepository;

    public List<Solicitud> listarTodos() {
        return solicitudRepository.listarTodos();
    }

    public Optional<Solicitud> buscarPorId(Integer id) {
        return solicitudRepository.buscarPorId(id);
    }

    public Stream<Solicitud> listarStream() {
        return solicitudRepository.listarStream();
    }

    public Solicitud guardar(Solicitud nuevo) {
        /*
         * 
         * 
         *    TODO: !!!!!! FALTA TODA LA LOGICA DE AGREGAR UNA SOLICITUD, REALIZAR TODOS LOS CALCULOS DE SUS COSTOS
         *    ASOCIADOS, ETC....
         * 
         */
        return solicitudRepository.guardar(nuevo);
    }

    public void eliminarPorId(Integer id) {
        solicitudRepository.eliminarPorId(id);
    }

    public Optional<Solicitud> modificar(Integer id, Solicitud nuevo) {
        /*
         *     TODO: FALTA IMPMENTAR LOGICA DE MODIFICACION PARA VER QUE NO SE CAMBIEN ATRIBUTOS INVALIDOS O REPETIDOS
         */
        return solicitudRepository.modificar(id, nuevo);
    }

    public boolean existe(Integer id) {
        return solicitudRepository.existe(id);
    }


    
    
}
