package backend.tpi.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transportistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Transportista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transportista")
    private Integer id;

    private String nombre;

    private String telefono;

}
