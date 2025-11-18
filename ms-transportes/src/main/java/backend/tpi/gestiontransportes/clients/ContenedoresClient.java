package backend.tpi.gestiontransportes.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import backend.tpi.gestiontransportes.DTOS.ContenedorDTO;

@Component
public class ContenedoresClient {

        private final RestClient restClient;

        public ContenedoresClient(@Value("${ms.solicitudes.url}") String baseUrl,
                        RestClient.Builder restClientBuilder) {
                this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        }

        public ContenedorDTO obtenerContenedorPorId(Integer id) {
                return this.restClient.get()
                                .uri("/contenedores/{id}", id)
                                .retrieve()
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        // Lanzar una excepción de Spring que se mapeará a un 404 si es
                                                        // necesario
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "el contenedor con ID " + id
                                                                                        + " no fue encontrado.");
                                                })
                                .body(ContenedorDTO.class);
        }

        public void marcarContenedorEnViaje(Integer id) {
                this.restClient.patch()
                                // URI: /contenedores/{id}/enViaje
                                .uri("/contenedores/{id}/enViaje", id)
                                .retrieve()
                                // Manejo de error 404 explícito: si no se encuentra el contenedor
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se pudo marcar el contenedor con ID " + id
                                                                                        + " como en viaje: no fue encontrado.");
                                                })
                                // Usamos toBodilessEntity() para ejecutar la llamada y confirmar el éxito (sin
                                // necesidad de deserializar el cuerpo de la respuesta)
                                .toBodilessEntity();

        }

        public void marcarContenedorEnDeposito(Integer id) {
                this.restClient.patch()
                                // URI: /contenedores/{id}/enDeposito
                                .uri("/contenedores/{id}/enDeposito", id)
                                .retrieve()
                                // Manejo de error 404 explícito: si no se encuentra el contenedor
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se pudo marcar el contenedor con ID " + id
                                                                                        + " como en deposito: no fue encontrado.");
                                                })
                                // Usamos toBodilessEntity() para ejecutar la llamada y confirmar el éxito (sin
                                // necesidad de deserializar el cuerpo de la respuesta)
                                .toBodilessEntity();

        }

        public void marcarContenedorRetirado(Integer id) {
                this.restClient.patch()
                                // URI: /contenedores/{id}/retirado
                                .uri("/contenedores/{id}/retirado", id)
                                .retrieve()
                                // Manejo de error 404 explícito: si no se encuentra el contenedor
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se pudo marcar el contenedor con ID " + id
                                                                                        + " como retirado: no fue encontrado.");
                                                })
                                // Usamos toBodilessEntity() para ejecutar la llamada y confirmar el éxito (sin
                                // necesidad de deserializar el cuerpo de la respuesta)
                                .toBodilessEntity();

        }
}
