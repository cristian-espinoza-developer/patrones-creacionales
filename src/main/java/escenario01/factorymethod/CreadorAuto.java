package escenario01.factorymethod;

/**
 * ConcreteCreator: crea vehículos de tipo Auto.
 */
public class CreadorAuto extends CreadorVehiculo {

    @Override
    protected Vehiculo crearVehiculo(SolicitudMatricula s) {
        return new Auto(s.placa(), s.marca(), s.modelo(), s.anioFabricacion(), s.avaluo());
    }
}
