package escenario01.factorymethod;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Único punto donde se asocia el tipo solicitado con su creador concreto.
 * Agregar un nuevo tipo de vehículo solo requiere registrar su creador aquí,
 * sin modificar la lógica que utiliza los vehículos.
 */
public class CatalogoCreadores {

    private final Map<String, CreadorVehiculo> creadores = new HashMap<>();
    private final List<String> tipos = new ArrayList<>();

    public void registrar(String tipo, CreadorVehiculo creador) {
        if (creadores.put(normalizar(tipo), creador) == null) {
            tipos.add(tipo);
        }
    }

    public CreadorVehiculo obtener(String tipo) {
        CreadorVehiculo creador = creadores.get(normalizar(tipo));
        if (creador == null) {
            throw new IllegalArgumentException("Tipo de vehículo no soportado: " + tipo);
        }
        return creador;
    }

    public boolean contiene(String tipo) {
        return creadores.containsKey(normalizar(tipo));
    }

    /** Tipos registrados, en el orden en que fueron agregados. */
    public List<String> getTipos() {
        return List.copyOf(tipos);
    }

    public static CatalogoCreadores porDefecto() {
        CatalogoCreadores catalogo = new CatalogoCreadores();
        catalogo.registrar("Auto", new CreadorAuto());
        catalogo.registrar("Camioneta", new CreadorCamioneta());
        catalogo.registrar("Camión", new CreadorCamion());
        return catalogo;
    }

    /** Ignora mayúsculas, espacios y tildes: "Camión", "camion" y " CAMION " son el mismo tipo. */
    private static String normalizar(String tipo) {
        if (tipo == null) {
            return "";
        }
        return Normalizer.normalize(tipo.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }
}
