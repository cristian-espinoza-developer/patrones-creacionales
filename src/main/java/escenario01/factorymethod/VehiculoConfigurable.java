package escenario01.factorymethod;

import escenario01.factorymethod.CampoRequerido.TipoDato;
import java.util.Map;

/**
 * ConcreteProduct para los tipos de vehículo definidos en tiempo de ejecución.
 * Calcula la matrícula a partir de su DefinicionTipo:
 * avalúo × tasa × (1 − descuento por antigüedad) + Σ(valor × tarifa) + tasa fija,
 * con un recargo porcentual por cada característica Sí/No marcada.
 */
public class VehiculoConfigurable extends Vehiculo {

    private final DefinicionTipo definicion;
    private final Map<String, Double> valores;

    public VehiculoConfigurable(String placa, String marca, String modelo, int anioFabricacion, double avaluo,
                                DefinicionTipo definicion, Map<String, Double> valores) {
        super(placa, marca, modelo, anioFabricacion, avaluo);
        for (TarifaCaracteristica c : definicion.caracteristicas()) {
            Double valor = valores.get(c.etiqueta());
            if (valor == null) {
                throw new IllegalArgumentException("Falta la característica '" + c.etiqueta() + "'");
            }
            if (valor < 0) {
                throw new IllegalArgumentException("La característica '" + c.etiqueta() + "' no puede ser negativa");
            }
        }
        this.definicion = definicion;
        this.valores = Map.copyOf(valores);
    }

    @Override
    public double calcularMatricula() {
        double descuento = Math.min(getAntiguedad() * definicion.descuentoPorAnio(), definicion.descuentoMaximo());
        double valor = getAvaluo() * definicion.tasaAvaluo() * (1 - descuento) + TASA_FIJA;
        double recargo = 0;
        for (TarifaCaracteristica c : definicion.caracteristicas()) {
            double dato = valores.get(c.etiqueta());
            if (c.tipo() == TipoDato.LOGICO) {
                recargo += dato * c.tarifa();
            } else {
                valor += dato * c.tarifa();
            }
        }
        return valor * (1 + recargo);
    }

    @Override
    public String getTipo() {
        return definicion.nombre();
    }

    public DefinicionTipo getDefinicion() {
        return definicion;
    }

    public Map<String, Double> getValores() {
        return valores;
    }
}
