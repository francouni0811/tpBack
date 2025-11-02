package backend.tpi.gestiontransportes.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rutas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Integer id;

    // Referencia a solicitudes (microservicio ms-solicitudes). La mantenemos como entero simple.
    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "cant_tramos")
    private Integer cantTramos;

    @Column(name = "cant_depositos")
    private Integer cantDepositos;
}

