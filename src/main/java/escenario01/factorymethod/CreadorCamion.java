package escenario01.factorymethod;

import escenario01.factorymethod.CampoRequerido.TipoDato;
import java.util.List;

/**
 * ConcreteCreator: crea vehículos de tipo Camión.
 */
public class CreadorCamion extends CreadorVehiculo {

    @Override
    protected Vehiculo crearVehiculo(SolicitudMatricula s) {
        return new Camion(s.placa(), s.marca(), s.modelo(), s.anioFabricacion(), s.avaluo(),
                s.caracteristicaDecimal("tonelaje"),
                s.caracteristicaEntera("numeroEjes"));
    }

    @Override
    public List<CampoRequerido> getCamposEspecificos() {
        return List.of(
                new CampoRequerido("tonelaje", "Tonelaje (t)", TipoDato.DECIMAL),
                new CampoRequerido("numeroEjes", "Número de ejes", TipoDato.ENTERO));
    }
}
