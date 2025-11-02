package backend.tpi.gestiondesolicitudes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import backend.tpi.gestiondesolicitudes.domain.Cliente;
import backend.tpi.gestiondesolicitudes.services.ClienteService;
import backend.tpi.gestiondesolicitudes.repositorios.ClienteRepository;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		System.out.println("funcionando.......................\n\n\n");

		Cliente cliente = new Cliente();
		cliente.setNombre("Juan");
		cliente.setTelefono("123456789");
		cliente.setEmail("juan.perez@example.com");

		ClienteRepository clienteRepository = context.getBean(ClienteRepository.class);

		ClienteService clienteService = new ClienteService(clienteRepository);
		clienteService.guardar(cliente);
		clienteService.guardar(cliente);
	}

}
