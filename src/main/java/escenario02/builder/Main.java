package escenario02.builder;

import java.time.LocalDate;

/**
 * Cliente de consola: construye activos paso a paso con el Builder
 * y mediante las secuencias predefinidas del Director.
 */
public class Main {

    public static void main(String[] args) {
        // Construcción directa con el Builder: solo se agregan los datos que aplican
        ActivoFijo impresora = new ActivoFijo.Builder("AF003", "Impresora multifunción", 380)
                .marca("Epson")
                .modelo("EcoTank L6270")
                .ubicacion("Secretaría general")
                .fechaAdquisicion(LocalDate.of(2025, 3, 15))
                .garantiaMeses(24)
                .proveedor("Comercial Andina S.A.")
                .build();

        // Construcción mediante el Director
        DirectorActivos director = new DirectorActivos();
        ActivoFijo computador = director.construirComputador(
                new ActivoFijo.Builder("AF001", "Computador portátil", 1200),
                "Dell", "Latitude 5440", "SN-88231", "Intel Core i7", 36);
        ActivoFijo mesa = director.construirMueble(
                new ActivoFijo.Builder("AF002", "Mesa de reuniones", 450),
                "Madera", "2,40 × 1,20 m", "Sala 3", "María López");

        for (ActivoFijo activo : new ActivoFijo[] {computador, mesa, impresora}) {
            System.out.println(activo.describir());
            System.out.println();
        }

        // Los datos obligatorios se validan al construir
        try {
            new ActivoFijo.Builder("", "Silla ergonómica", 0).build();
        } catch (IllegalStateException e) {
            System.out.println("Construcción rechazada:");
            System.out.println(e.getMessage());
        }
    }
}
