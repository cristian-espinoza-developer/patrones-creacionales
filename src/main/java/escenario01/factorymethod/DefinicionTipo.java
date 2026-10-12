package escenario01.factorymethod;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Definición de un tipo de vehículo creado en tiempo de ejecución: su nombre,
 * las tasas que se aplican sobre el avalúo y las características adicionales tarifadas.
 * Los porcentajes se expresan como fracción (0.015 = 1,5 %).
 */
public record DefinicionTipo(String nombre, double tasaAvaluo, double descuentoPorAnio,
                             double descuentoMaximo, List<TarifaCaracteristica> caracteristicas) {

    public DefinicionTipo {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del tipo de vehículo es obligatorio");
        }
        if (tasaAvaluo < 0) {
            throw new IllegalArgumentException("La tasa sobre el avalúo no puede ser negativa");
        }
        if (descuentoPorAnio < 0) {
            throw new IllegalArgumentException("El descuento por año no puede ser negativo");
        }
        if (descuentoMaximo < 0 || descuentoMaximo > 1) {
            throw new IllegalArgumentException("El descuento máximo debe estar entre 0 % y 100 %");
        }
        nombre = nombre.trim();
        caracteristicas = caracteristicas == null ? List.of() : List.copyOf(caracteristicas);

        Set<String> etiquetas = new HashSet<>();
        for (TarifaCaracteristica c : caracteristicas) {
            if (!etiquetas.add(c.etiqueta().toLowerCase(Locale.ROOT))) {
                throw new IllegalArgumentException("La característica '" + c.etiqueta() + "' está repetida");
            }
        }
    }
}
