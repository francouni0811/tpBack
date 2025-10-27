package backend.tpi.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_contenedor")
    private Contenedor contenedor;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_tarifa")
    private Tarifa tarifa;

    private BigDecimal costoEstimado;
    private LocalDateTime tiempoEstimado;
    private BigDecimal costoFinal;
    private LocalDateTime tiempoFinal;
    private String estado;

    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    private LocalDateTime fechaSolicitud;

}
