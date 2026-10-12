package escenario01.factorymethod;

import java.util.List;

/**
 * Creator: declara el Factory Method y define la operación que trabaja
 * únicamente con la abstracción Vehiculo.
 */
public abstract class CreadorVehiculo {

    /** Factory Method: cada subclase decide qué vehículo concreto instanciar. */
    protected abstract Vehiculo crearVehiculo(SolicitudMatricula solicitud);

    /** Datos adicionales que el creador necesita; por defecto, ninguno. */
    public List<CampoRequerido> getCamposEspecificos() {
        return List.of();
    }

    public final Vehiculo registrarVehiculo(SolicitudMatricula solicitud) {
        Vehiculo vehiculo = crearVehiculo(solicitud);
        System.out.printf("Registrado: %s%n", vehiculo);
        return vehiculo;
    }
}
