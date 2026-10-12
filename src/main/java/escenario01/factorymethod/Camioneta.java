package escenario01.factorymethod;

/**
 * ConcreteProduct: incorpora la capacidad de carga y el tipo de uso.
 */
public class Camioneta extends Vehiculo {

    private static final double TASA_AVALUO = 0.018;
    private static final double VALOR_POR_TONELADA = 15.00;
    private static final double RECARGO_USO_COMERCIAL = 0.10;

    private final double capacidadCarga;
    private final boolean usoComercial;

    public Camioneta(String placa, String marca, String modelo, int anioFabricacion, double avaluo,
                     double capacidadCarga, boolean usoComercial) {
        super(placa, marca, modelo, anioFabricacion, avaluo);
        if (capacidadCarga < 0) {
            throw new IllegalArgumentException("La capacidad de carga no puede ser negativa");
        }
        this.capacidadCarga = capacidadCarga;
        this.usoComercial = usoComercial;
    }

    @Override
    public double calcularMatricula() {
        double valor = getAvaluo() * TASA_AVALUO + capacidadCarga * VALOR_POR_TONELADA + TASA_FIJA;
        return usoComercial ? valor * (1 + RECARGO_USO_COMERCIAL) : valor;
    }

    @Override
    public String getTipo() {
        return "Camioneta";
    }

    public double getCapacidadCarga() {
        return capacidadCarga;
    }

    public boolean isUsoComercial() {
        return usoComercial;
    }
}
