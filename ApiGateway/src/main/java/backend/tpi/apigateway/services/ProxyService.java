package backend.tpi.apigateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyService {

    private final RestClient.Builder restClientBuilder;

    // Cambiá este puerto si tu microservicio corre en otro
    private static final String BASE_URL_MS_SOLICITUDES = "http://localhost:8081/api/v1";

    public ResponseEntity<String> forward(String method, String path, String body, Map<String, String> headers) {
        RestClient client = restClientBuilder.build();
        String targetUrl = BASE_URL_MS_SOLICITUDES + path;

        log.info("➡️ Redirigiendo [{}] a {}", method, targetUrl);

        var request = client.method(HttpMethod.valueOf(method))
                .uri(targetUrl);

        if (body != null && !body.isEmpty()) {
            request.body(body);
        }

        return request
                .retrieve()
                .toEntity(String.class);
    }
}
