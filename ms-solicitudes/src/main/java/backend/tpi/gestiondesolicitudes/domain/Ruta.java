package backend.tpi.gestiondesolicitudes.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_solicitud")
    private Solicitud solicitud;

    private Integer cantTramos;
    private Integer cantDepositos;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tramo> tramos;

}
