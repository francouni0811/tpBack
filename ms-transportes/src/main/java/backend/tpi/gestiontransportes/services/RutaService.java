package backend.tpi.gestiontransportes.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import backend.tpi.gestiontransportes.domain.Ruta;
import backend.tpi.gestiontransportes.repositorios.RutaRepository;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    public List<Ruta> listarTodos() { return rutaRepository.listarTodos(); }

    public Optional<Ruta> buscarPorId(Integer id) { return rutaRepository.buscarPorId(id); }

    public Stream<Ruta> listarStream() { return rutaRepository.listarStream(); }

    public Ruta guardar(Ruta nuevo) { return rutaRepository.guardar(nuevo); }

    public void eliminarPorId(Integer id) { rutaRepository.eliminarPorId(id); }

    public Optional<Ruta> modificar(Integer id, Ruta nuevo) { return rutaRepository.modificar(id, nuevo); }

    public boolean existe(Integer id) { return rutaRepository.existe(id); }
}

