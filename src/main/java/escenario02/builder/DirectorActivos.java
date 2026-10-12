package escenario02.builder;

/**
 * Director (opcional en la variante moderna): encapsula las secuencias de construcción
 * habituales de la institución, garantizando que cada categoría de activo
 * incorpore la información que le corresponde.
 */
public class DirectorActivos {

    /** Un computador requiere marca, modelo, número de serie, procesador y garantía. */
    public ActivoFijo construirComputador(ActivoFijo.Builder builder, String marca, String modelo,
                                          String numeroSerie, String procesador, int garantiaMeses) {
        return builder
                .marca(marca)
                .modelo(modelo)
                .numeroSerie(numeroSerie)
                .caracteristica("Procesador", procesador)
                .garantiaMeses(garantiaMeses)
                .build();
    }

    /** Un mueble requiere material, dimensiones, ubicación y custodio. */
    public ActivoFijo construirMueble(ActivoFijo.Builder builder, String material, String dimensiones,
                                      String ubicacion, String custodio) {
        return builder
                .caracteristica("Material", material)
                .caracteristica("Dimensiones", dimensiones)
                .ubicacion(ubicacion)
                .custodio(custodio)
                .build();
    }
}
