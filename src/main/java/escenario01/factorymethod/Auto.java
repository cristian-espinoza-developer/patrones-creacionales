package escenario01.factorymethod;

/**
 * ConcreteProduct: considera principalmente el avalúo y la antigüedad.
 */
public class Auto extends Vehiculo {

    private static final double TASA_AVALUO = 0.015;
    private static final double DESCUENTO_POR_ANIO = 0.02;
    private static final double DESCUENTO_MAXIMO = 0.20;

    public Auto(String placa, String marca, String modelo, int anioFabricacion, double avaluo) {
        super(placa, marca, modelo, anioFabricacion, avaluo);
    }

    @Override
    public double calcularMatricula() {
        double descuento = Math.min(getAntiguedad() * DESCUENTO_POR_ANIO, DESCUENTO_MAXIMO);
        return getAvaluo() * TASA_AVALUO * (1 - descuento) + TASA_FIJA;
    }

    @Override
    public String getTipo() {
        return "Auto";
    }
}
