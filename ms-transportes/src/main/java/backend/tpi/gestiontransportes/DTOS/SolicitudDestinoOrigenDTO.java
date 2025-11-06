package backend.tpi.gestiontransportes.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para transferir solo los datos de ubicación (origen y destino) 
 * de una Solicitud entre microservicios.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDestinoOrigenDTO {
    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;
}
