package escenario01.factorymethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Year;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FactoryMethodTest {

    private static final int ANIO_ACTUAL = Year.now().getValue();

    private final CatalogoCreadores catalogo = CatalogoCreadores.porDefecto();

    private Vehiculo registrar(String tipo, int anio, double avaluo, Map<String, String> extras) {
        SolicitudMatricula s = new SolicitudMatricula(tipo, "XYZ-0001", "Marca", "Modelo", anio, avaluo, extras);
        return catalogo.obtener(tipo).registrarVehiculo(s);
    }

    @Test
    void creaElProductoConcretoSegunElTipo() {
        assertInstanceOf(Auto.class, registrar("auto", ANIO_ACTUAL, 10000, Map.of()));
        assertInstanceOf(Camioneta.class, registrar("Camioneta", ANIO_ACTUAL, 10000,
                Map.of("capacidadCarga", "1", "usoComercial", "false")));
        assertInstanceOf(Camion.class, registrar(" CAMION ", ANIO_ACTUAL, 10000,
                Map.of("tonelaje", "10", "numeroEjes", "2")));
    }

    @Test
    void autoAplicaDescuentoPorAntiguedadConTope() {
        assertEquals(10000 * 0.015 + 30, registrar("auto", ANIO_ACTUAL, 10000, Map.of()).calcularMatricula(), 0.001);
        assertEquals(10000 * 0.015 * 0.90 + 30, registrar("auto", ANIO_ACTUAL - 5, 10000, Map.of()).calcularMatricula(), 0.001);
        assertEquals(10000 * 0.015 * 0.80 + 30, registrar("auto", ANIO_ACTUAL - 30, 10000, Map.of()).calcularMatricula(), 0.001);
    }

    @Test
    void camionetaAplicaRecargoPorUsoComercial() {
        double base = 20000 * 0.018 + 2 * 15 + 30;
        assertEquals(base, registrar("camioneta", ANIO_ACTUAL, 20000,
                Map.of("capacidadCarga", "2", "usoComercial", "false")).calcularMatricula(), 0.001);
        assertEquals(base * 1.10, registrar("camioneta", ANIO_ACTUAL, 20000,
                Map.of("capacidadCarga", "2", "usoComercial", "true")).calcularMatricula(), 0.001);
    }

    @Test
    void camionConsideraTonelajeYEjes() {
        double esperado = 50000 * 0.01 + 12 * 20 + 3 * 10 + 30;
        assertEquals(esperado, registrar("camion", ANIO_ACTUAL, 50000,
                Map.of("tonelaje", "12", "numeroEjes", "3")).calcularMatricula(), 0.001);
    }

    @Test
    void nuevoTipoSeIncorporaSoloRegistrandoSuCreador() {
        catalogo.registrar("motocicleta", new CreadorVehiculo() {
            @Override
            protected Vehiculo crearVehiculo(SolicitudMatricula s) {
                return new Vehiculo(s.placa(), s.marca(), s.modelo(), s.anioFabricacion(), s.avaluo()) {
                    @Override
                    public double calcularMatricula() {
                        return getAvaluo() * 0.01 + TASA_FIJA;
                    }

                    @Override
                    public String getTipo() {
                        return "Motocicleta";
                    }
                };
            }
        });
        assertEquals(3000 * 0.01 + 30, registrar("motocicleta", ANIO_ACTUAL, 3000, Map.of()).calcularMatricula(), 0.001);
    }

    @Test
    void tipoConfigurableSeRegistraYCalculaSegunSuDefinicion() {
        DefinicionTipo bus = new DefinicionTipo("Bus", 0.012, 0.01, 0.10, List.of(
                new TarifaCaracteristica("Pasajeros", CampoRequerido.TipoDato.ENTERO, 2.0),
                new TarifaCaracteristica("Servicio público", CampoRequerido.TipoDato.LOGICO, 0.05)));
        catalogo.registrar(bus.nombre(), new CreadorConfigurable(bus));

        assertTrue(catalogo.contiene("bus"));
        assertEquals(List.of("Auto", "Camioneta", "Camión", "Bus"), catalogo.getTipos());
        assertEquals(List.of("Pasajeros", "Servicio público"), nombres(catalogo.obtener("Bus")));

        Vehiculo vehiculo = registrar("Bus", ANIO_ACTUAL - 4, 80000,
                Map.of("Pasajeros", "40", "Servicio público", "true"));
        assertInstanceOf(VehiculoConfigurable.class, vehiculo);
        assertEquals("Bus", vehiculo.getTipo());
        double esperado = (80000 * 0.012 * (1 - 0.04) + 40 * 2.0 + 30) * 1.05;
        assertEquals(esperado, vehiculo.calcularMatricula(), 0.001);
    }

    @Test
    void definicionDeTipoRechazaDatosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new DefinicionTipo(" ", 0.01, 0, 0, List.of()));
        assertThrows(IllegalArgumentException.class, () -> new DefinicionTipo("Bus", -0.01, 0, 0, List.of()));
        assertThrows(IllegalArgumentException.class, () -> new DefinicionTipo("Bus", 0.01, 0, 1.5, List.of()));
        assertThrows(IllegalArgumentException.class, () -> new DefinicionTipo("Bus", 0.01, 0, 0, List.of(
                new TarifaCaracteristica("Ejes", CampoRequerido.TipoDato.ENTERO, 1),
                new TarifaCaracteristica("ejes", CampoRequerido.TipoDato.DECIMAL, 1))));
        assertThrows(IllegalArgumentException.class,
                () -> new TarifaCaracteristica("Ejes", CampoRequerido.TipoDato.ENTERO, -1));
    }

    @Test
    void catalogoExponeTiposYCamposEspecificosDeCadaCreador() {
        assertEquals(List.of("Auto", "Camioneta", "Camión"), catalogo.getTipos());
        assertEquals(List.of(), catalogo.obtener("Auto").getCamposEspecificos());
        assertEquals(List.of("capacidadCarga", "usoComercial"), nombres(catalogo.obtener("Camioneta")));
        assertEquals(List.of("tonelaje", "numeroEjes"), nombres(catalogo.obtener("Camión")));
    }

    @Test
    void aceptaDecimalesConComa() {
        assertEquals(20000 * 0.018 + 1.5 * 15 + 30, registrar("camioneta", ANIO_ACTUAL, 20000,
                Map.of("capacidadCarga", "1,5", "usoComercial", "false")).calcularMatricula(), 0.001);
    }

    private static List<String> nombres(CreadorVehiculo creador) {
        return creador.getCamposEspecificos().stream().map(CampoRequerido::nombre).toList();
    }

    @Test
    void rechazaTiposYDatosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> registrar("camion", ANIO_ACTUAL, 50000,
                Map.of("tonelaje", "abc", "numeroEjes", "2")));
        assertThrows(IllegalArgumentException.class, () -> catalogo.obtener("avion"));
        assertThrows(IllegalArgumentException.class, () -> registrar("camion", ANIO_ACTUAL, 50000, Map.of()));
        assertThrows(IllegalArgumentException.class, () -> registrar("auto", ANIO_ACTUAL + 1, 10000, Map.of()));
        assertThrows(IllegalArgumentException.class, () -> registrar("auto", ANIO_ACTUAL, 0, Map.of()));
    }
}
