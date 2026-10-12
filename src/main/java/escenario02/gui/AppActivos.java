package escenario02.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la interfaz gráfica del Escenario 02.
 */
public class AppActivos {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Si no está disponible, se usa el look and feel por defecto de Swing.
            }
            new VentanaActivos().setVisible(true);
        });
    }
}
