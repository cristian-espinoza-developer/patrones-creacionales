package escenario01.factorymethod;

import java.time.Year;

/**
 * Product: contrato común de todos los vehículos que pueden crearse.
 */
public abstract class Vehiculo {

    protected static final double TASA_FIJA = 30.00;

    private final String placa;
    private final String marca;
    private final String modelo;
    private final int anioFabricacion;
    private final double avaluo;

    protected Vehiculo(String placa, String marca, String modelo, int anioFabricacion, double avaluo) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }
        if (anioFabricacion > Year.now().getValue()) {
            throw new IllegalArgumentException("El año de fabricación no puede ser futuro");
        }
        if (avaluo <= 0) {
            throw new IllegalArgumentException("El avalúo debe ser mayor que cero");
        }
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anioFabricacion = anioFabricacion;
        this.avaluo = avaluo;
    }

    /** Cada tipo de vehículo implementa su propia lógica de cálculo. */
    public abstract double calcularMatricula();

    public abstract String getTipo();

    public int getAntiguedad() {
        return Year.now().getValue() - anioFabricacion;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnioFabricacion() {
        return anioFabricacion;
    }

    public double getAvaluo() {
        return avaluo;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s (%d) placa %s, avalúo $%,.2f",
                getTipo(), marca, modelo, anioFabricacion, placa, avaluo);
    }
}
