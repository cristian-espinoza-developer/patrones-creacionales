package escenario01.factorymethod;

import java.util.List;
import java.util.Map;

/**
 * Cliente: el tipo de vehículo se conoce solo en tiempo de ejecución y,
 * una vez creado, se trabaja exclusivamente con la abstracción Vehiculo.
 */
public class Main {

    public static void main(String[] args) {
        CatalogoCreadores catalogo = CatalogoCreadores.porDefecto();

        List<SolicitudMatricula> solicitudes = List.of(
                new SolicitudMatricula("auto", "ABC-1234", "Chevrolet", "Aveo", 2018, 12000, Map.of()),
                new SolicitudMatricula("camioneta", "PBA-5678", "Toyota", "Hilux", 2021, 35000,
                        Map.of("capacidadCarga", "1.0", "usoComercial", "true")),
                new SolicitudMatricula("camion", "GTR-9012", "Hino", "500", 2015, 60000,
                        Map.of("tonelaje", "12", "numeroEjes", "3")));

        for (SolicitudMatricula solicitud : solicitudes) {
            Vehiculo vehiculo = catalogo.obtener(solicitud.tipo()).registrarVehiculo(solicitud);
            System.out.printf("  Valor de matrícula: $%,.2f%n", vehiculo.calcularMatricula());
        }
    }
}
