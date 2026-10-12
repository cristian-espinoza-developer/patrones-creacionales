package escenario01.gui;

import static escenario01.gui.Formularios.agregarFila;
import static escenario01.gui.Formularios.leerDecimal;
import static escenario01.gui.Formularios.leerEntero;

import escenario01.factorymethod.CampoRequerido;
import escenario01.factorymethod.CatalogoCreadores;
import escenario01.factorymethod.CreadorVehiculo;
import escenario01.factorymethod.SolicitudMatricula;
import escenario01.factorymethod.Vehiculo;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Cliente gráfico del Factory Method. No conoce Auto, Camioneta ni Camión:
 * obtiene los tipos y sus campos adicionales desde el catálogo de creadores
 * y trabaja con los vehículos únicamente a través de la abstracción Vehiculo.
 */
public class VentanaMatricula extends JFrame {

    private final CatalogoCreadores catalogo;
    private final JTabbedPane pestanas = new JTabbedPane();

    private final JComboBox<String> cboTipo;
    private final JTextField txtPlaca = new JTextField(14);
    private final JTextField txtMarca = new JTextField(14);
    private final JTextField txtModelo = new JTextField(14);
    private final JTextField txtAnio = new JTextField(14);
    private final JTextField txtAvaluo = new JTextField(14);

    private final JPanel panelEspecificos = new JPanel(new GridBagLayout());
    private final Map<String, JComponent> camposEspecificos = new LinkedHashMap<>();
    private final JLabel lblResultado = new JLabel(" ");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[] {"Tipo", "Placa", "Marca", "Modelo", "Año", "Avalúo", "Matrícula"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    public VentanaMatricula(CatalogoCreadores catalogo) {
        super("Matriculación vehicular — Factory Method");
        this.catalogo = catalogo;
        this.cboTipo = new JComboBox<>(catalogo.getTipos().toArray(new String[0]));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel registro = new JPanel(new BorderLayout(10, 10));
        registro.add(crearFormulario(), BorderLayout.WEST);
        registro.add(crearTabla(), BorderLayout.CENTER);
        pestanas.addTab("Registrar vehículo", registro);
        pestanas.addTab("Nuevos tipos de vehículo", new PanelNuevoTipo(catalogo, this::tipoCreado));
        add(pestanas);

        cboTipo.addActionListener(e -> actualizarCamposEspecificos());
        actualizarCamposEspecificos();

        setSize(1100, 560);
        setLocationRelativeTo(null);
    }

    private JPanel crearFormulario() {
        JPanel generales = new JPanel(new GridBagLayout());
        generales.setBorder(BorderFactory.createTitledBorder("Datos generales"));
        agregarFila(generales, 0, "Tipo de vehículo", cboTipo);
        agregarFila(generales, 1, "Placa", txtPlaca);
        agregarFila(generales, 2, "Marca", txtMarca);
        agregarFila(generales, 3, "Modelo", txtModelo);
        agregarFila(generales, 4, "Año de fabricación", txtAnio);
        agregarFila(generales, 5, "Avalúo ($)", txtAvaluo);

        panelEspecificos.setBorder(BorderFactory.createTitledBorder("Datos específicos del tipo"));

        JButton btnCalcular = new JButton("Calcular matrícula");
        btnCalcular.addActionListener(e -> calcular());
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());
        JPanel acciones = new JPanel();
        acciones.add(btnCalcular);
        acciones.add(btnLimpiar);

        lblResultado.setFont(lblResultado.getFont().deriveFont(Font.BOLD, 16f));
        JPanel resultado = new JPanel();
        resultado.add(lblResultado);

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        formulario.add(generales);
        formulario.add(panelEspecificos);
        formulario.add(acciones);
        formulario.add(resultado);
        formulario.add(Box.createVerticalGlue());
        return formulario;
    }

    private JScrollPane crearTabla() {
        JTable tabla = new JTable(modeloTabla);
        tabla.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 10),
                BorderFactory.createTitledBorder("Vehículos registrados")));
        return scroll;
    }

    /** Agrega al combo el tipo recién creado y lo deja listo para registrar vehículos. */
    private void tipoCreado(String tipo) {
        cboTipo.addItem(tipo);
        cboTipo.setSelectedItem(tipo);
        pestanas.setSelectedIndex(0);
    }

    /** Dibuja los campos adicionales que declara el creador del tipo seleccionado. */
    private void actualizarCamposEspecificos() {
        panelEspecificos.removeAll();
        camposEspecificos.clear();

        CreadorVehiculo creador = catalogo.obtener((String) cboTipo.getSelectedItem());
        int fila = 0;
        for (CampoRequerido campo : creador.getCamposEspecificos()) {
            JComponent componente = campo.tipo() == CampoRequerido.TipoDato.LOGICO
                    ? new JCheckBox()
                    : new JTextField(14);
            camposEspecificos.put(campo.nombre(), componente);
            agregarFila(panelEspecificos, fila++, campo.etiqueta(), componente);
        }
        if (fila == 0) {
            panelEspecificos.add(new JLabel("Este tipo no requiere datos adicionales."));
        }

        panelEspecificos.revalidate();
        panelEspecificos.repaint();
    }

    private void calcular() {
        try {
            String tipo = (String) cboTipo.getSelectedItem();
            SolicitudMatricula solicitud = new SolicitudMatricula(tipo,
                    txtPlaca.getText().trim().toUpperCase(),
                    txtMarca.getText().trim(),
                    txtModelo.getText().trim(),
                    leerEntero(txtAnio, "Año de fabricación"),
                    leerDecimal(txtAvaluo, "Avalúo"),
                    leerCamposEspecificos());

            Vehiculo vehiculo = catalogo.obtener(tipo).registrarVehiculo(solicitud);
            double valor = vehiculo.calcularMatricula();

            lblResultado.setText(String.format("Valor de matrícula: $%,.2f", valor));
            modeloTabla.addRow(new Object[] {
                    vehiculo.getTipo(), vehiculo.getPlaca(), vehiculo.getMarca(), vehiculo.getModelo(),
                    vehiculo.getAnioFabricacion(),
                    String.format("$%,.2f", vehiculo.getAvaluo()),
                    String.format("$%,.2f", valor)});
        } catch (IllegalArgumentException e) {
            lblResultado.setText(" ");
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos inválidos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Map<String, String> leerCamposEspecificos() {
        Map<String, String> valores = new HashMap<>();
        camposEspecificos.forEach((nombre, componente) -> valores.put(nombre,
                componente instanceof JCheckBox casilla
                        ? String.valueOf(casilla.isSelected())
                        : ((JTextField) componente).getText().trim()));
        return valores;
    }

    private void limpiar() {
        for (JTextField campo : new JTextField[] {txtPlaca, txtMarca, txtModelo, txtAnio, txtAvaluo}) {
            campo.setText("");
        }
        actualizarCamposEspecificos();
        lblResultado.setText(" ");
        txtPlaca.requestFocusInWindow();
    }
}
