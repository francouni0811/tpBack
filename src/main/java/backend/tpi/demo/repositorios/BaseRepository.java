package backend.tpi.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repositorio genérico base para todos los repositorios JPA.
 * Define operaciones CRUD básicas y utilitarios de streaming.
 */
@NoRepositoryBean // evita que Spring intente instanciar directamente este repo
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * Guarda o actualiza una entidad.
     */
    default T guardar(T entidad) {
        return save(entidad);
    }

    /**
     * Busca una entidad por su ID.
     */
    default Optional<T> buscarPorId(ID id) {
        return findById(id);
    }

    /**
     * Lista todas las entidades.
     */
    default List<T> listarTodos() {
        return findAll();
    }

    /**
     * Devuelve todas las entidades como Stream.
     */
    default Stream<T> listarStream() {
        return findAll().stream();
    }

    /**
     * Elimina una entidad por su ID.
     */
    default void eliminarPorId(ID id) {
        deleteById(id);
    }
}
