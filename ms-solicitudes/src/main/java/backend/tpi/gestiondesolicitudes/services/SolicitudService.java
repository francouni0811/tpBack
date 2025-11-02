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

    public Solicitud guardar(Solicitud nuevaSolicitud) {
        /*
         * 
         * 
         *    TODO: !!!!!! FALTA TODA LA LOGICA DE AGREGAR UNA SOLICITUD, REALIZAR TODOS LOS CALCULOS DE SUS COSTOS
         *    ASOCIADOS, ETC....
         * 
        */
        
        // verificar si el cliente existe sino retorno null
        if (!clienteRepository.existe(nuevaSolicitud.getCliente().getId())) { return null;}
        // verificar si el contenedor existe o retorno null
        if (!contenedorRepository.existe(nuevaSolicitud.getContenedor().getId())) { return null;}
        // verificar si la tarifa existe o retorno null
        if (!tarifaRepository.existe(nuevaSolicitud.getTarifa().getId())) {return null;}

        // hay que tomar origen lat y long, y destino lat y long y mandar a crear una ruta al ms-transporte con restclient
        //la creacion de una ruta desencadena la creacion de sus respectivos tramos
        // !! la ruta tiene una solicitud!! y los tramos tienen una ruta!!

        // una vez se haya creado la ruta asociada a la solicitud y sus respectivos tramos:
        
        //calcular costo estimado -> sumar costo estimado de todos los tramos asociasdos a la ruta
        //calcular tiempo estimado
        // calcular costo final
        //calcular tiempo final


        return solicitudRepository.guardar(nuevaSolicitud);
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
