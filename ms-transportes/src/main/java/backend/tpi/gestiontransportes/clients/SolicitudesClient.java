package backend.tpi.gestiontransportes.clients;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestClient.Builder;

import backend.tpi.gestiontransportes.DTOS.SolicitudDestinoOrigenDTO;

@Component
public class SolicitudesClient {

        private final RestClient restClient;

        public SolicitudesClient(@Value("${ms.solicitudes.url}") String baseUrl,
                        RestClient.Builder restClientBuilder) {
                this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        }

        public SolicitudDestinoOrigenDTO obtenerSolicitudPorId(Integer id) {

                return this.restClient.get()
                                // La URI es solo /{id} porque el ID se pasa como variable de ruta en el
                                // controller
                                .uri("/solicitudes/{id}", id)
                                .retrieve()
                                // Manejo de error 404 explícito: si no se encuentra la solicitud
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        // Lanzar una excepción de Spring que se mapeará a un 404 si es
                                                        // necesario
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "La solicitud con ID " + id
                                                                                        + " no fue encontrada.");
                                                })
                                // Mapear la respuesta JSON al DTO
                                .body(SolicitudDestinoOrigenDTO.class);
        }

        public void marcarSolicitudEntregada(Integer id) {
                this.restClient.patch()
                                // URI: /solicitudes/{id}/entregada
                                .uri("/solicitudes/{id}/entregada", id)
                                .retrieve()
                                // Manejo de error 404 explícito: si no se encuentra la solicitud
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se pudo marcar la solicitud con ID " + id
                                                                                        + " como entregada: no fue encontrada.");
                                                })
                                // Usamos toBodilessEntity() para ejecutar la llamada y confirmar el éxito (sin
                                // necesidad de deserializar el cuerpo de la respuesta)
                                .toBodilessEntity();

        }

        public void marcarSolicitudProgramada(Integer id) {
                this.restClient.patch()
                                // URI: /solicitudes/{id}/entregada
                                .uri("/solicitudes/{id}/programada", id)
                                .retrieve()
                                // Manejo de error 404 explícito: si no se encuentra la solicitud
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se pudo marcar la solicitud con ID " + id
                                                                                        + " como programada: no fue encontrada.");
                                                })
                                // Usamos toBodilessEntity() para ejecutar la llamada y confirmar el éxito (sin
                                // necesidad de deserializar el cuerpo de la respuesta)
                                .toBodilessEntity();
        }

        public void marcarSolicitudEnTransito(Integer id) {
                this.restClient.patch()
                                // URI: /solicitudes/{id}/entregada
                                .uri("/solicitudes/{id}/en-transito", id)
                                .retrieve()
                                // Manejo de error 404 explícito: si no se encuentra la solicitud
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se pudo marcar la solicitud con ID " + id
                                                                                        + " como EnTransito: no fue encontrada.");
                                                })
                                // Usamos toBodilessEntity() para ejecutar la llamada y confirmar el éxito (sin
                                // necesidad de deserializar el cuerpo de la respuesta)
                                .toBodilessEntity();
        }

        public void actualizarTiempoEstimadoHs(Integer id, Integer tiempoEstimadoHs) {
                Map<String, Object> body = new HashMap<>();
                body.put("tiempoEstimadoHs", tiempoEstimadoHs);

                this.restClient.patch()
                                .uri("/solicitudes/{id}", id)
                                .body(body)
                                .retrieve()
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se encontró la solicitud con ID " + id);
                                                })
                                .toBodilessEntity();
        }

        public void actualizarTiempoFinalHs(Integer id, Integer tiempoFinalHs) {
                Map<String, Object> body = new HashMap<>();
                body.put("tiempoFinalHs", tiempoFinalHs);

                this.restClient.patch()
                                .uri("/solicitudes/{id}", id)
                                .body(body)
                                .retrieve()
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se encontró la solicitud con ID " + id);
                                                })
                                .toBodilessEntity();
        }

        public void actualizarCostoFinal(Integer id, BigDecimal costoFinal) {
                Map<String, Object> body = new HashMap<>();
                body.put("costoFinal", costoFinal);

                this.restClient.patch()
                                .uri("/solicitudes/{id}", id)
                                .body(body)
                                .retrieve()
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se encontró la solicitud con ID " + id);
                                                })
                                .toBodilessEntity();
        }

        public void actualizarCostoEstimado(Integer id, BigDecimal costoEstimado) {
                Map<String, Object> body = new HashMap<>();
                body.put("costoEstimado", costoEstimado);

                this.restClient.patch()
                                .uri("/solicitudes/{id}", id)
                                .body(body)
                                .retrieve()
                                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                                                (request, response) -> {
                                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                        "No se encontró la solicitud con ID " + id);
                                                })
                                .toBodilessEntity();
        }

}
