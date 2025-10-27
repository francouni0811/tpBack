package backend.tpi.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "camiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camion")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_transportista")
    private Transportista transportista;

    private String patente;

    private String telefono;

    private BigDecimal capacidadKg;

    private BigDecimal volumenMax;

    private String estado;

    private BigDecimal consumoXKm;

    private BigDecimal costoBaseTrasladoXKm;

}
