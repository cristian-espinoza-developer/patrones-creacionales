package escenario01.factorymethod;

import escenario01.factorymethod.CampoRequerido.TipoDato;

/**
 * Característica adicional de un tipo de vehículo definido en tiempo de ejecución.
 * Para datos numéricos, la tarifa es un valor en dólares por unidad;
 * para datos lógicos (Sí/No), es un recargo porcentual expresado como fracción (0.10 = 10 %).
 */
public record TarifaCaracteristica(String etiqueta, TipoDato tipo, double tarifa) {

    public TarifaCaracteristica {
        if (etiqueta == null || etiqueta.isBlank()) {
            throw new IllegalArgumentException("El nombre de la característica es obligatorio");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de dato de la característica es obligatorio");
        }
        if (tarifa < 0) {
            throw new IllegalArgumentException("La tarifa de la característica no puede ser negativa");
        }
        etiqueta = etiqueta.trim();
    }

    public CampoRequerido aCampoRequerido() {
        return new CampoRequerido(etiqueta, etiqueta, tipo);
    }
}
