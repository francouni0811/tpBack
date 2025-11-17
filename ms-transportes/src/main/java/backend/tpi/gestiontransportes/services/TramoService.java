package backend.tpi.gestiontransportes.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.tpi.gestiontransportes.DTOS.ContenedorDTO;
import backend.tpi.gestiontransportes.DTOS.SolicitudDestinoOrigenDTO;
import backend.tpi.gestiontransportes.DTOS.TarifaDTO;
import backend.tpi.gestiontransportes.clients.ContenedoresClient;
import backend.tpi.gestiontransportes.clients.SolicitudesClient;
import backend.tpi.gestiontransportes.clients.TarifasClient;
import backend.tpi.gestiontransportes.domain.Camion;
import backend.tpi.gestiontransportes.domain.Deposito;
import backend.tpi.gestiontransportes.domain.Ruta;
import backend.tpi.gestiontransportes.domain.Tramo;
import backend.tpi.gestiontransportes.repositorios.TramoRepository;

@Service
public class TramoService {

    private final TramoRepository tramoRepository;
    private final SolicitudesClient solicitudesClient;
    private final ContenedoresClient contenedoresClient;
    private final TarifasClient tarifasClient;
    private final CamionService camionService;
    private final RutaService rutaService;

    public TramoService(TramoRepository tramoRepository,
            SolicitudesClient solicitudesClient,
            ContenedoresClient contenedoresClient,
            TarifasClient tarifasClient,
            CamionService camionService,
            @Lazy RutaService rutaService) {
        this.tramoRepository = tramoRepository;
        this.solicitudesClient = solicitudesClient;
        this.contenedoresClient = contenedoresClient;
        this.tarifasClient = tarifasClient;
        this.camionService = camionService;
        this.rutaService = rutaService;
    }

    public List<Tramo> listarTodos() {
        return tramoRepository.listarTodos();
    }

    public Optional<Tramo> buscarPorId(Integer id) {
        return tramoRepository.buscarPorId(id);
    }

    public Stream<Tramo> listarStream() {
        return tramoRepository.listarStream();
    }

    public Tramo guardar(Tramo nuevo) {
        System.out.println("--------------------------------------Guardando nuevo tramo: ");
        return tramoRepository.guardar(nuevo);
    }

    public void eliminarPorId(Integer id) {
        tramoRepository.eliminarPorId(id);
    }

    public Optional<Tramo> modificar(Integer id, Tramo nuevo) {
        return tramoRepository.modificar(id, nuevo);
    }

    public boolean existe(Integer id) {
        return tramoRepository.existe(id);
    }

    public List<Tramo> buscarPorRuta(Integer idRuta) {
        return tramoRepository.findByRuta_Id(idRuta);
    }

    public Tramo asignarCamion(Integer idTramo, Integer idCamion) {
        Tramo tramo = buscarPorId(idTramo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tramo no encontrado"));

        Camion camion = camionService.buscarPorId(idCamion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camion no encontrado"));

        if (!camion.getEstado().equalsIgnoreCase("libre")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El camion no esta libre");
        }
        tramo.setCamion(camion);

        return modificar(idTramo, tramo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo actualizar el tramo"));
    }

    public Tramo iniciarTramo(Integer idTramo) {
        Tramo tramo = buscarPorId(idTramo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tramo no encontrado"));

        if (tramo.getFechaHoraInicio() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tramo ya fue iniciado");
        }

        if (tramo.getCamion() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tramo no tiene camion asignado");
        }

        if (tramo.getNroOrden() != 0) {
            int idRuta = tramo.getRuta().getId();
            List<Tramo> tramosRuta = buscarPorRuta(idRuta);
            Tramo tramoAnterior = tramosRuta.get(tramo.getNroOrden() - 1);
            if (tramoAnterior.getFechaHoraFin() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tramo anterior aun no finalizo");
            }
        }

        tramo.setFechaHoraInicio(LocalDateTime.now());
        tramo.setEstado("comenzado");

        tramo.getCamion().setEstado("EnTransito");
        camionService.modificar(tramo.getCamion().getId(), tramo.getCamion());

        if (tramo.getNroOrden() == 0) {
            Ruta ruta = tramo.getRuta();
            Integer idSolicitud = ruta.getIdSolicitud();
            marcarSolicitudEnTransito(idSolicitud);
        }

        return modificar(idTramo, tramo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo actualizar el tramo"));
    }

    public Tramo finalizarTramo(Integer idTramo) {
        Tramo tramo = buscarPorId(idTramo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tramo no encontrado"));

        if (tramo.getFechaHoraFin() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tramo ya fue finalizado");
        }

        if (tramo.getFechaHoraInicio() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tramo aun no fue iniciado");
        }

        LocalDateTime fechaHoraFin = LocalDateTime.now();

        tramo.setFechaHoraFin(fechaHoraFin);
        tramo.setEstado("finalizado");

        // hacemos los calculos para la duracion del tramo
        LocalDateTime fechaHoraInicio = tramo.getFechaHoraInicio();
        Integer duracionTramoHs = (int) ChronoUnit.HOURS.between(fechaHoraInicio, fechaHoraFin);
        tramo.setTiempoRealHs(duracionTramoHs);

        tramo.getCamion().setEstado("libre");
        camionService.modificar(tramo.getCamion().getId(), tramo.getCamion());

        Tramo tramoActualizado = calcularCostoReal(tramo);

        // en caso de que sea el ultimo tramo, no tendrá un deposito de destino (o en
        // caso de que sea el unico tramo tampoco)
        if (tramo.getDepositoDestino() == null) {
            Ruta ruta = tramo.getRuta();
            Ruta rutaFinalizada = rutaService.calcularCostoReal(ruta);
            rutaService.modificar(ruta.getId(), rutaFinalizada);
            marcarSolicitudEntregada(ruta.getIdSolicitud());
            // si es el ultimo tramo de la ruta tenemos que calcular la duracion real en hs
            // de la solicitud
            actualizarTiempoFinalHs(ruta.getIdSolicitud(), ruta.getId(), fechaHoraFin);
        }

        return modificar(idTramo, tramoActualizado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo actualizar el tramo"));
    }

    public Tramo calcularCostoReal(Tramo tramo) {
        /*
         * TODO: TODO EL CALCULO DE COSTO FINAL
         * obtener distancia en km
         * obtener costoBaseTrasladoXKm del camion del tramo
         * obtener consumoXKm del camion del tramo
         * calcular dias de estadia (tomar tramo anterior a este si este no es el
         * primero,
         * quedate con la fechaFin del anterior y restala a tu fecha inicio, de eso
         * quedate con la cantidad de HORAS,
         * obtener el deposito de Origen y multiplicar esa cantDeDias * costoEstadia del
         * depositoOrigen)
         */

        BigDecimal distanciaKM_tramo = tramo.getDistanciaKm();
        BigDecimal costoBaseTrasladoXKM_camion = tramo.getCamion().getCostoBaseTrasladoXKm();
        BigDecimal consumoXKM_camion = tramo.getCamion().getConsumoXKm();

        // asumo que este es el primer tramo de su ruta
        BigDecimal cantHoras = BigDecimal.ZERO;
        // si no es el primer tramo, calculo la cant de horas que paró en el depósito
        // (le sumo +1 a la cant de horas por cuestiones de testing)
        if (tramo.getNroOrden() != 0) {
            int idRuta = tramo.getRuta().getId();
            List<Tramo> tramosRuta = buscarPorRuta(idRuta);
            Tramo tramoAnterior = tramosRuta.get(tramo.getNroOrden() - 1);
            LocalDateTime fechaFinTramoAnterior = tramoAnterior.getFechaHoraFin();
            LocalDateTime fechaInicioActual = tramo.getFechaHoraInicio();

            // corrijo con +1 por si el camion se quedó 0 horas
            cantHoras = BigDecimal.valueOf(ChronoUnit.HOURS.between(fechaFinTramoAnterior, fechaInicioActual) + 1);
        }
        // obtener el costo de estadia por hora del deposito de origen del tramo actual
        // aunque si es el primer tramo, no tiene deposito de origen
        Deposito depositoOrigenObtenido = tramo.getDepositoOrigen();
        BigDecimal costoEstadiaHora = BigDecimal.ZERO;
        if (depositoOrigenObtenido != null) {
            costoEstadiaHora = depositoOrigenObtenido.getCostoEstadiaHora();
        }

        /*
         * el tramo tiene una ruta, la ruta una solicitud, la solicitud un contenedor,
         * el contenedor un volumen
         * 
         * obtener tarifa para ese volumen : == (
         * 
         */

        // obtener solicitud
        Integer idSolicitud = tramo.getRuta().getIdSolicitud();
        SolicitudDestinoOrigenDTO solicitudDTO = this.solicitudesClient.obtenerSolicitudPorId(idSolicitud);
        // obtener contenedor de la solicitud
        Integer idContenedor = solicitudDTO.getIdContenedor();
        ContenedorDTO contenedorDTO = this.contenedoresClient.obtenerContenedorPorId(idContenedor);
        // obtener volumen del contenedor y tarifa correspondiente a ese volumen
        BigDecimal volumenContenedorEncontrado = contenedorDTO.getVolumen();
        TarifaDTO tarifaDTO = this.tarifasClient.obtenerTarifaParaVolumen(volumenContenedorEncontrado);
        // obtener el costoCombustible de la tarifa encontrada
        BigDecimal valorCombustible = tarifaDTO.getValorCombustible();

        // hacer el calulo por partes para evitar problemas con los bigDecimal ;(
        // costos de traslado (distancia * costoBaseTrasladoXKM)
        BigDecimal costosTraslado = distanciaKM_tramo.multiply(costoBaseTrasladoXKM_camion);
        // costos combustible (distancia * consumo * valorCombustible)
        BigDecimal costosCombustible = valorCombustible.multiply((distanciaKM_tramo.multiply(consumoXKM_camion)));
        // costos de estadia
        BigDecimal costosEstadia = cantHoras.multiply(costoEstadiaHora);
        // sumamos todo
        BigDecimal costoReal = costosTraslado.add(costosCombustible.add(costosEstadia));

        // asignamos el costoReal
        tramo.setCostoReal(costoReal);

        return tramo;

    }

    public void marcarSolicitudEntregada(Integer id) {
        this.solicitudesClient.marcarSolicitudEntregada(id);
    }

    public void marcarSolicitudEnTransito(Integer id) {
        this.solicitudesClient.marcarSolicitudEnTransito(id);
    }

    public void actualizarTiempoFinalHs(Integer idSol, Integer idRuta, LocalDateTime fechaFinal) {
        List<Tramo> tramos = buscarPorRuta(idRuta);
        List<Tramo> tramosOrdenados = tramos.stream().sorted(Comparator.comparing(Tramo::getNroOrden))
                .collect(Collectors.toList());
        // quedarse con la fecha de inicio del primer tramo
        LocalDateTime fechaInicial = tramosOrdenados.get(0).getFechaHoraInicio();
        Integer duracionReal_hs = (int) ChronoUnit.HOURS.between(fechaInicial, fechaFinal);
        this.solicitudesClient.actualizarTiempoFinalHs(idRuta, duracionReal_hs);
    }

}
