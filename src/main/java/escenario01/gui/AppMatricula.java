package escenario01.gui;

import escenario01.factorymethod.CatalogoCreadores;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la interfaz gráfica del Escenario 01.
 */
public class AppMatricula {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Si no está disponible, se usa el look and feel por defecto de Swing.
            }
            new VentanaMatricula(CatalogoCreadores.porDefecto()).setVisible(true);
        });
    }
}
