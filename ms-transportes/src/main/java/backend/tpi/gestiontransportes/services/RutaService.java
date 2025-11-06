package backend.tpi.gestiontransportes.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import backend.tpi.gestiontransportes.domain.Deposito;
import backend.tpi.gestiontransportes.domain.Ruta;
import backend.tpi.gestiontransportes.domain.Tramo;
import backend.tpi.gestiontransportes.repositorios.RutaRepository;
import backend.tpi.gestiontransportes.services.geoApi.GeoService;
import backend.tpi.gestiontransportes.DTOS.SolicitudDestinoOrigenDTO;
import backend.tpi.gestiontransportes.DTOS.geoApi.DistanciaDTO;
import backend.tpi.gestiontransportes.clients.SolicitudesClient;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;
    private final DepositoService depositoService;
    private final SolicitudesClient solicitudesClient;
    private final GeoService geoService;

    public RutaService(RutaRepository rutaRepository, SolicitudesClient solicitudesClient, GeoService geoService, DepositoService depositoService) {
        this.rutaRepository = rutaRepository;
        this.solicitudesClient = solicitudesClient;
        this.geoService = geoService;
        this.depositoService = depositoService;
    }

    public List<Ruta> listarTodos() { return rutaRepository.listarTodos(); }

    public Optional<Ruta> buscarPorId(Integer id) { return rutaRepository.buscarPorId(id); }

    public Stream<Ruta> listarStream() { return rutaRepository.listarStream(); }

    public Ruta guardar(Ruta nuevo) { return rutaRepository.guardar(nuevo); }

    public void eliminarPorId(Integer id) { rutaRepository.eliminarPorId(id); }

    public Optional<Ruta> modificar(Integer id, Ruta nuevo) { return rutaRepository.modificar(id, nuevo); }

    public boolean existe(Integer id) { return rutaRepository.existe(id); }

    public List<Ruta> generarRutasPosibles(Integer idSolicitud) {
        SolicitudDestinoOrigenDTO solicitudDTO = this.solicitudesClient.obtenerSolicitudPorId(idSolicitud);
        
        String origenDireccion = solicitudDTO.getOrigenDireccion();
        String destinoDireccion = solicitudDTO.getDestinoDireccion();

        List<Ruta> listaRutas = new ArrayList<>();

        try {

            DistanciaDTO distanciaDTO = geoService.calcularDistancia(origenDireccion, destinoDireccion);
            String duracionString = distanciaDTO.getDuracionTexto();

            int duracionInt = transformarAHoras(duracionString);

            if (duracionInt < 6) {
                Ruta rutaUnica = calcular_ruta_unica(idSolicitud);
                listaRutas.add(rutaUnica);
            }

            if (duracionInt >= 6 && duracionInt <= 12) {
                // generar ruta de tramo unico y ruta de tramo doble
                Ruta rutaUnica = calcular_ruta_unica(idSolicitud);
                Ruta rutaDoble = calcular_ruta_doble(idSolicitud, origenDireccion);
                listaRutas.add(rutaUnica);
                listaRutas.add(rutaDoble);
            }

            if (duracionInt > 12) {
                // generar ruta de tramo unico, doble y triple
                Ruta rutaUnica = calcular_ruta_unica(idSolicitud);
                Ruta rutaDoble = calcular_ruta_doble(idSolicitud, origenDireccion);
                Ruta rutaTriple = calcular_ruta_triple(idSolicitud, origenDireccion, destinoDireccion);
                listaRutas.add(rutaUnica);
                listaRutas.add(rutaDoble);
                listaRutas.add(rutaTriple);
            }

        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n\n\nalgo falló desde RutaService - generarRutasPosibles()");
        }

        return listaRutas;
    }
    // public boolean asignarRutas() {}

    private Deposito obtenerDepositoMasCercano(String origen) {

        List<Deposito> depositos = depositoService.listarTodos();

        if (depositos == null || depositos.isEmpty()) {
            return null; // No hay depósitos para comparar
        }

        System.out.println("=======debuggin obtener depo mas cercano====");


        Optional<Deposito> resultado = depositos.stream().min(Comparator.comparingInt(deposito -> {
            String destinoDireccion = deposito.getDireccionTxt();
            try {
                DistanciaDTO distanciaDTO = geoService.calcularDistancia(origen, destinoDireccion);
                String duracionString = distanciaDTO.getDuracionTexto();
                System.out.println("\n\n\n\n=========");
                System.out.println("distancia hasta depo: "+deposito.getNombre()+" : "+duracionString);
                
                return transformarAHoras(duracionString);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("\n\n\nalgo fallo en rutaService - obtenerDepositoMasCercano()");
                return Integer.MAX_VALUE;
            }
        }));

        return resultado.orElse(null);
    }


    private Ruta calcular_ruta_unica(Integer idSolicitud) {
        Ruta rutaUnica = new Ruta();
        Tramo tramo1 = new Tramo();

        //setear ruta
        rutaUnica.setIdSolicitud(idSolicitud);
        rutaUnica.setCantTramos(1);
        rutaUnica.setCantDepositos(0);
        
        //setear tramo (sin camion, con ruta, con nroOrden, sin depoDestino ni origen, calcular costo aprox)
        //tramo1.setRuta(rutaUnica);
        tramo1.setNroOrden(0);
        tramo1.setTipoTramo("origen-destino");
        tramo1.setEstado("no comenzado");
        // calcular costo aprox BigDecimal

        // agregar tramo a ruta
        rutaUnica.agregarTramo(tramo1);

        return rutaUnica;
    }

    private Ruta calcular_ruta_doble(Integer idSolicitud,String origenDireccion) {

        Deposito depositoIntermedio = obtenerDepositoMasCercano(origenDireccion);

        if (depositoIntermedio == null) {return null;}

        Ruta rutaDoble = new Ruta();
        Tramo tramo1 = new Tramo();
        Tramo tramo2 = new Tramo();

        // setear una ruta
        rutaDoble.setIdSolicitud(idSolicitud);
        rutaDoble.setCantTramos(2);
        rutaDoble.setCantDepositos(1);

        //setear tramo1
        //tramo1.setRuta(rutaDoble);
        tramo1.setNroOrden(0);
        tramo1.setTipoTramo("origen-deposito");
        tramo1.setDepositoDestino(depositoIntermedio);
        tramo1.setEstado("no comenzado");
        // calcular costo aprox BigDecimal

        //setear tramo2
        //tramo2.setRuta(rutaDoble);
        tramo2.setNroOrden(1);
        tramo2.setTipoTramo("deposito-destino");
        tramo2.setDepositoOrigen(depositoIntermedio);
        tramo2.setEstado("no comenzado");
        // calcular costo aprox BigDecimal

        //agregar a la ruta
        rutaDoble.agregarTramo(tramo1);
        rutaDoble.agregarTramo(tramo2);

        return rutaDoble;
    }

    private Ruta calcular_ruta_triple(Integer idSolicitud, String origenDireccion, String destinoDireccion) {
        Deposito depositoCercanoAlOrigen = obtenerDepositoMasCercano(origenDireccion);
        Deposito depositoCercanoAlDestino = obtenerDepositoMasCercano(destinoDireccion);

        if (depositoCercanoAlOrigen == null || depositoCercanoAlDestino == null || depositoCercanoAlDestino == depositoCercanoAlOrigen) {
            System.out.println("\n\nCalculo TRiple debbuging....\n\n");
            System.out.println("cercano al origen "+depositoCercanoAlOrigen.getNombre());
            System.out.println("cercano al destino "+depositoCercanoAlDestino.getNombre());
            System.out.println("==============================\n\n\n");
            return null;
        }

        Ruta rutaTramoTriple = new Ruta();
        Tramo tramo1 = new Tramo();
        Tramo tramo2 = new Tramo();
        Tramo tramo3 = new Tramo();

        // setear una ruta
        rutaTramoTriple.setIdSolicitud(idSolicitud);
        rutaTramoTriple.setCantTramos(3);
        rutaTramoTriple.setCantDepositos(2);

        //setear tramo 1
        //tramo1.setRuta(rutaTramoTriple);
        tramo1.setNroOrden(0);
        tramo1.setTipoTramo("origen-deposito");
        tramo1.setDepositoDestino(depositoCercanoAlOrigen);
        tramo1.setEstado("no comenzado");
        // calcular costo aprox BigDecimal

        //setear tramo 2
        //tramo2.setRuta(rutaTramoTriple);
        tramo2.setNroOrden(1);
        tramo2.setTipoTramo("deposito-deposito");
        tramo2.setDepositoOrigen(depositoCercanoAlOrigen);
        tramo2.setDepositoDestino(depositoCercanoAlDestino);
        tramo2.setEstado("no comenzado");
        // calcular costo aprox BigDecimal

        //setar tramo 3
        //tramo3.setRuta(rutaTramoTriple);
        tramo3.setNroOrden(2);
        tramo3.setTipoTramo("deposito-destino");
        tramo3.setDepositoOrigen(depositoCercanoAlDestino);
        tramo3.setEstado("no comenzado");
        // calcular costo aprox BigDecimal

        //agregar a la ruta
        rutaTramoTriple.agregarTramo(tramo1);
        rutaTramoTriple.agregarTramo(tramo2);
        rutaTramoTriple.agregarTramo(tramo3);

        return rutaTramoTriple;
    }

    private int transformarAHoras(String duracionString) {

        if (!duracionString.contains("hours")) {
            return 0; // si no dura por lo menos 1 hora devuelvo 0
        }

        // si dura mas que una hora, dividimos en un array el string de duracion
        String[] partes = duracionString.split(" ");

        if (duracionString.contains("days")) {
            return Integer.parseInt(partes[2])+48;
        }


        if (duracionString.contains("day")) {
            return Integer.parseInt(partes[2])+24;
        }


        // lo que esté en la primer posicion son las horas
        return Integer.parseInt(partes[0]);
    }
}