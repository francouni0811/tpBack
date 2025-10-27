package backend.tpi.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tramos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramo")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_camion")
    private Camion camion;

    @ManyToOne
    @JoinColumn(name = "id_ruta")
    private Ruta ruta;

    private Integer nroOrden;

    @ManyToOne
    @JoinColumn(name = "deposito_origen")
    private Deposito depositoOrigen;

    @ManyToOne
    @JoinColumn(name = "deposito_destino")
    private Deposito depositoDestino;

    private String tipoTramo;
    private String estado;

    private BigDecimal costoAprox;
    private BigDecimal costoReal;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

}
