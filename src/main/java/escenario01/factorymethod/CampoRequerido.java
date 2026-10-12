package escenario01.factorymethod;

/**
 * Describe un dato adicional que un creador concreto necesita para construir
 * su vehículo. Permite que la interfaz gráfica genere los campos sin conocer
 * los tipos concretos de vehículo.
 */
public record CampoRequerido(String nombre, String etiqueta, TipoDato tipo) {

    public enum TipoDato {
        DECIMAL, ENTERO, LOGICO
    }
}
