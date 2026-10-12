package escenario01.factorymethod;

import escenario01.factorymethod.CampoRequerido.TipoDato;
import java.util.List;

/**
 * ConcreteCreator: crea vehículos de tipo Camioneta.
 */
public class CreadorCamioneta extends CreadorVehiculo {

    @Override
    protected Vehiculo crearVehiculo(SolicitudMatricula s) {
        return new Camioneta(s.placa(), s.marca(), s.modelo(), s.anioFabricacion(), s.avaluo(),
                s.caracteristicaDecimal("capacidadCarga"),
                s.caracteristicaLogica("usoComercial"));
    }

    @Override
    public List<CampoRequerido> getCamposEspecificos() {
        return List.of(
                new CampoRequerido("capacidadCarga", "Capacidad de carga (t)", TipoDato.DECIMAL),
                new CampoRequerido("usoComercial", "Uso comercial", TipoDato.LOGICO));
    }
}
