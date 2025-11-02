package backend.tpi.gestiontransportes.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import backend.tpi.gestiontransportes.domain.Tramo;
import backend.tpi.gestiontransportes.repositorios.TramoRepository;

@Service
public class TramoService {

    private final TramoRepository tramoRepository;

    public TramoService(TramoRepository tramoRepository) {
        this.tramoRepository = tramoRepository;
    }

    public List<Tramo> listarTodos() { return tramoRepository.listarTodos(); }

    public Optional<Tramo> buscarPorId(Integer id) { return tramoRepository.buscarPorId(id); }

    public Stream<Tramo> listarStream() { return tramoRepository.listarStream(); }

    public Tramo guardar(Tramo nuevo) { return tramoRepository.guardar(nuevo); }

    public void eliminarPorId(Integer id) { tramoRepository.eliminarPorId(id); }

    public Optional<Tramo> modificar(Integer id, Tramo nuevo) { return tramoRepository.modificar(id, nuevo); }

    public boolean existe(Integer id) { return tramoRepository.existe(id); }
}

