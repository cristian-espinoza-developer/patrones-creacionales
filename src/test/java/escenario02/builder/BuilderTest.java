package escenario02.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BuilderTest {

    @Test
    void construyeSoloConLosDatosObligatorios() {
        ActivoFijo activo = new ActivoFijo.Builder("AF010", "Archivador metálico", 180).build();

        assertEquals("AF010", activo.getCodigo());
        assertEquals("Archivador metálico", activo.getNombre());
        assertEquals(180, activo.getPrecioAdquisicion());
        for (Optional<?> dato : List.of(activo.getMarca(), activo.getModelo(), activo.getNumeroSerie(),
                activo.getUbicacion(), activo.getCustodio(), activo.getFechaAdquisicion(),
                activo.getGarantiaMeses(), activo.getProveedor(), activo.getObservaciones())) {
            assertTrue(dato.isEmpty());
        }
        assertTrue(activo.getCaracteristicas().isEmpty());
    }

    @Test
    void incorporaProgresivamenteLosDatosOpcionales() {
        ActivoFijo activo = new ActivoFijo.Builder(" AF001 ", " Computador portátil ", 1200)
                .marca("Dell")
                .modelo("Latitude 5440")
                .numeroSerie("SN-88231")
                .ubicacion("Cuenca")
                .custodio("Ana Torres")
                .fechaAdquisicion(LocalDate.of(2024, 1, 31))
                .garantiaMeses(36)
                .proveedor("Tecnología Austral")
                .caracteristica("Procesador", "Intel Core i7")
                .caracteristica("RAM", "16 GB")
                .observaciones("Asignado al área de sistemas")
                .build();

        assertEquals("AF001", activo.getCodigo());
        assertEquals("Computador portátil", activo.getNombre());
        assertEquals(Optional.of("Dell"), activo.getMarca());
        assertEquals(Optional.of(36), activo.getGarantiaMeses());
        assertEquals(Optional.of(LocalDate.of(2027, 1, 31)), activo.getFinGarantia());
        assertEquals(List.of("Procesador", "RAM"), List.copyOf(activo.getCaracteristicas().keySet()));
        assertTrue(activo.describir().contains("Fin de garantía: 2027-01-31"));
    }

    @Test
    void lasCaracteristicasDelProductoSonInmutables() {
        ActivoFijo activo = new ActivoFijo.Builder("AF020", "Proyector", 600)
                .caracteristica("Lúmenes", "3600")
                .build();
        Map<String, String> caracteristicas = activo.getCaracteristicas();
        assertThrows(UnsupportedOperationException.class, () -> caracteristicas.put("Color", "Blanco"));
    }

    @Test
    void elProductoNoCambiaSiElBuilderSeSigueUsando() {
        ActivoFijo.Builder builder = new ActivoFijo.Builder("AF030", "Monitor", 220).marca("LG");
        ActivoFijo primero = builder.build();
        builder.caracteristica("Tamaño", "27 pulgadas");

        assertTrue(primero.getCaracteristicas().isEmpty());
        assertEquals(1, builder.build().getCaracteristicas().size());
    }

    @Test
    void buildInformaTodosLosDatosObligatoriosFaltantes() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> new ActivoFijo.Builder(" ", "", 0).build());

        assertTrue(e.getMessage().contains("código institucional"));
        assertTrue(e.getMessage().contains("nombre o descripción"));
        assertTrue(e.getMessage().contains("precio de adquisición"));
    }

    @Test
    void buildRechazaFechaFuturaYGarantiaNegativa() {
        assertThrows(IllegalStateException.class, () -> new ActivoFijo.Builder("AF040", "Silla", 90)
                .fechaAdquisicion(LocalDate.now().plusDays(1)).build());
        assertThrows(IllegalStateException.class, () -> new ActivoFijo.Builder("AF041", "Silla", 90)
                .garantiaMeses(-1).build());
    }

    @Test
    void losPasosRechazanValoresVacios() {
        ActivoFijo.Builder builder = new ActivoFijo.Builder("AF050", "Escritorio", 300);
        assertThrows(IllegalArgumentException.class, () -> builder.marca(" "));
        assertThrows(IllegalArgumentException.class, () -> builder.caracteristica("Material", null));
        assertThrows(IllegalArgumentException.class, () -> builder.fechaAdquisicion(null));
    }

    @Test
    void directorConstruyeUnComputadorConSuInformacionRequerida() {
        ActivoFijo computador = new DirectorActivos().construirComputador(
                new ActivoFijo.Builder("AF001", "Computador portátil", 1200),
                "Dell", "Latitude 5440", "SN-88231", "Intel Core i7", 36);

        assertEquals(Optional.of("Dell"), computador.getMarca());
        assertEquals(Optional.of("Latitude 5440"), computador.getModelo());
        assertEquals(Optional.of("SN-88231"), computador.getNumeroSerie());
        assertEquals("Intel Core i7", computador.getCaracteristicas().get("Procesador"));
        assertEquals(Optional.of(36), computador.getGarantiaMeses());
        assertFalse(computador.getUbicacion().isPresent());
    }

    @Test
    void directorConstruyeUnMuebleConSuInformacionRequerida() {
        ActivoFijo mesa = new DirectorActivos().construirMueble(
                new ActivoFijo.Builder("AF002", "Mesa de reuniones", 450),
                "Madera", "2,40 × 1,20 m", "Sala 3", "María López");

        assertEquals(Map.of("Material", "Madera", "Dimensiones", "2,40 × 1,20 m"), mesa.getCaracteristicas());
        assertEquals(Optional.of("Sala 3"), mesa.getUbicacion());
        assertEquals(Optional.of("María López"), mesa.getCustodio());
        assertTrue(mesa.getMarca().isEmpty());
    }
}
