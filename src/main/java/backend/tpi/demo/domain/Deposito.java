package backend.tpi.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "depositos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deposito")
    private Integer id;

    private String nombre;

    @Column(name = "direccion_txt")
    private String direccionTxt;

    private Double longitud;
    private Double latitud;

    private BigDecimal costoEstadiaHora;

}
