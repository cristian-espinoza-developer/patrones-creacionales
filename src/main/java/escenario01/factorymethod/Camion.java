package escenario01.factorymethod;

/**
 * ConcreteProduct: requiere tonelaje y número de ejes.
 */
public class Camion extends Vehiculo {

    private static final double TASA_AVALUO = 0.01;
    private static final double VALOR_POR_TONELADA = 20.00;
    private static final double VALOR_POR_EJE = 10.00;

    private final double tonelaje;
    private final int numeroEjes;

    public Camion(String placa, String marca, String modelo, int anioFabricacion, double avaluo,
                  double tonelaje, int numeroEjes) {
        super(placa, marca, modelo, anioFabricacion, avaluo);
        if (tonelaje <= 0) {
            throw new IllegalArgumentException("El tonelaje debe ser mayor que cero");
        }
        if (numeroEjes < 2) {
            throw new IllegalArgumentException("Un camión tiene al menos 2 ejes");
        }
        this.tonelaje = tonelaje;
        this.numeroEjes = numeroEjes;
    }

    @Override
    public double calcularMatricula() {
        return getAvaluo() * TASA_AVALUO + tonelaje * VALOR_POR_TONELADA
                + numeroEjes * VALOR_POR_EJE + TASA_FIJA;
    }

    @Override
    public String getTipo() {
        return "Camión";
    }

    public double getTonelaje() {
        return tonelaje;
    }

    public int getNumeroEjes() {
        return numeroEjes;
    }
}
