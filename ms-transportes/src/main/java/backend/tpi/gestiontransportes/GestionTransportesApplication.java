package backend.tpi.gestiontransportes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import backend.tpi.gestiontransportes.domain.Transportista;
import backend.tpi.gestiontransportes.services.TransportistaService;

@SpringBootApplication
public class GestionTransportesApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(GestionTransportesApplication.class, args);
		System.out.println("funcionando..............\n\n\n");
		/*
		 * // Obtenemos el service desde el contexto de Spring
		 * TransportistaService service = context.getBean(TransportistaService.class);
		 * 
		 * // Creamos un transportista de prueba
		 * Transportista nuevo = Transportista.builder()
		 * .nombre("Juan Pérez")
		 * .telefono("351-555-1234")
		 * .build();
		 * 
		 * // Lo guardamos en la BD
		 * Transportista guardado = service.guardar(nuevo);
		 */

	}

}
