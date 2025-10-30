package backend.tpi.gestiondesolicitudes.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tarifas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Integer id;

    private String descripcion;
    private BigDecimal volMin;
    private BigDecimal volMax;
    private BigDecimal costoBaseKmXVol;
    private BigDecimal valorCombustible;
    private Boolean activa;

}
