package escenario01.factorymethod;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ConcreteCreator para los tipos de vehículo definidos en tiempo de ejecución.
 * Cada instancia crea vehículos de un tipo distinto según su DefinicionTipo.
 */
public class CreadorConfigurable extends CreadorVehiculo {

    private final DefinicionTipo definicion;

    public CreadorConfigurable(DefinicionTipo definicion) {
        if (definicion == null) {
            throw new IllegalArgumentException("La definición del tipo es obligatoria");
        }
        this.definicion = definicion;
    }

    @Override
    protected Vehiculo crearVehiculo(SolicitudMatricula s) {
        Map<String, Double> valores = new LinkedHashMap<>();
        for (TarifaCaracteristica c : definicion.caracteristicas()) {
            double valor = switch (c.tipo()) {
                case DECIMAL -> s.caracteristicaDecimal(c.etiqueta());
                case ENTERO -> s.caracteristicaEntera(c.etiqueta());
                case LOGICO -> s.caracteristicaLogica(c.etiqueta()) ? 1 : 0;
            };
            valores.put(c.etiqueta(), valor);
        }
        return new VehiculoConfigurable(s.placa(), s.marca(), s.modelo(), s.anioFabricacion(), s.avaluo(),
                definicion, valores);
    }

    @Override
    public List<CampoRequerido> getCamposEspecificos() {
        return definicion.caracteristicas().stream().map(TarifaCaracteristica::aCampoRequerido).toList();
    }

    public DefinicionTipo getDefinicion() {
        return definicion;
    }
}
