package escenario01.factorymethod;

import java.util.Map;

/**
 * Información proporcionada por el cliente en tiempo de ejecución.
 * Las características propias de cada tipo de vehículo viajan en un mapa,
 * de modo que nuevos tipos no obligan a modificar esta clase.
 */
public record SolicitudMatricula(String tipo, String placa, String marca, String modelo,
                                 int anioFabricacion, double avaluo,
                                 Map<String, String> caracteristicas) {

    public SolicitudMatricula {
        caracteristicas = caracteristicas == null ? Map.of() : Map.copyOf(caracteristicas);
    }

    public String caracteristica(String nombre) {
        String valor = caracteristicas.get(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "Falta la característica '" + nombre + "' para el tipo " + tipo);
        }
        return valor;
    }

    public double caracteristicaDecimal(String nombre) {
        String valor = caracteristica(nombre);
        try {
            return Double.parseDouble(valor.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La característica '" + nombre + "' debe ser un número: " + valor);
        }
    }

    public int caracteristicaEntera(String nombre) {
        String valor = caracteristica(nombre);
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La característica '" + nombre + "' debe ser un número entero: " + valor);
        }
    }

    public boolean caracteristicaLogica(String nombre) {
        return Boolean.parseBoolean(caracteristica(nombre));
    }
}
