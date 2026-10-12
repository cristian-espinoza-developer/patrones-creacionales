package escenario01.gui;

import static escenario01.gui.Formularios.agregarFila;
import static escenario01.gui.Formularios.leerDecimal;

import escenario01.factorymethod.CampoRequerido.TipoDato;
import escenario01.factorymethod.CatalogoCreadores;
import escenario01.factorymethod.CreadorConfigurable;
import escenario01.factorymethod.DefinicionTipo;
import escenario01.factorymethod.TarifaCaracteristica;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Permite definir nuevos tipos de vehículo mientras la aplicación se ejecuta.
 * Cada tipo se registra en el catálogo con su propio CreadorConfigurable,
 * por lo que se crea y se utiliza igual que Auto, Camioneta o Camión.
 */
public class PanelNuevoTipo extends JPanel {

    private static final String[] TIPOS_DATO = {"Decimal", "Entero", "Sí/No"};

    private final CatalogoCreadores catalogo;
    private final Consumer<String> alCrearTipo;

    private final JTextField txtNombre = new JTextField(14);
    private final JTextField txtTasaAvaluo = new JTextField(14);
    private final JTextField txtDescuentoAnio = new JTextField(14);
    private final JTextField txtDescuentoMaximo = new JTextField(14);

    private final JTextField txtEtiqueta = new JTextField(14);
    private final JComboBox<String> cboTipoDato = new JComboBox<>(TIPOS_DATO);
    private final JTextField txtTarifa = new JTextField(14);
    private final JLabel lblTarifa = new JLabel();

    private final List<TarifaCaracteristica> caracteristicas = new ArrayList<>();
    private final DefaultTableModel modeloCaracteristicas = new DefaultTableModel(
            new String[] {"Característica", "Tipo de dato", "Tarifa"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tablaCaracteristicas = new JTable(modeloCaracteristicas);

    public PanelNuevoTipo(CatalogoCreadores catalogo, Consumer<String> alCrearTipo) {
        super(new BorderLayout(10, 10));
        this.catalogo = catalogo;
        this.alCrearTipo = alCrearTipo;

        add(crearAyuda(), BorderLayout.NORTH);
        add(crearFormulario(), BorderLayout.WEST);
        add(crearTabla(), BorderLayout.CENTER);

        cboTipoDato.addActionListener(e -> actualizarEtiquetaTarifa());
        limpiar();
    }

    private JLabel crearAyuda() {
        JLabel ayuda = new JLabel("<html>Defina un nuevo tipo de vehículo. Su matrícula se calcula como "
                + "<b>avalúo × tasa × (1 − descuento por antigüedad) + Σ(valor × tarifa) + $30</b>; "
                + "cada característica Sí/No marcada aplica un recargo porcentual sobre el total.</html>");
        ayuda.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        return ayuda;
    }

    private JPanel crearFormulario() {
        JPanel tarifas = new JPanel(new GridBagLayout());
        tarifas.setBorder(BorderFactory.createTitledBorder("Datos del tipo"));
        agregarFila(tarifas, 0, "Nombre del tipo", txtNombre);
        agregarFila(tarifas, 1, "Tasa sobre el avalúo (%)", txtTasaAvaluo);
        agregarFila(tarifas, 2, "Descuento por año de antigüedad (%)", txtDescuentoAnio);
        agregarFila(tarifas, 3, "Descuento máximo (%)", txtDescuentoMaximo);

        JPanel caracteristica = new JPanel(new GridBagLayout());
        caracteristica.setBorder(BorderFactory.createTitledBorder("Característica adicional"));
        agregarFila(caracteristica, 0, "Nombre", txtEtiqueta);
        agregarFila(caracteristica, 1, "Tipo de dato", cboTipoDato);
        agregarFila(caracteristica, 2, "Tarifa", txtTarifa);
        agregarFila(caracteristica, 3, "", lblTarifa);

        JButton btnAgregar = new JButton("Agregar característica");
        btnAgregar.addActionListener(e -> agregarCaracteristica());
        JButton btnQuitar = new JButton("Quitar seleccionada");
        btnQuitar.addActionListener(e -> quitarCaracteristica());
        JPanel accionesCaracteristica = new JPanel();
        accionesCaracteristica.add(btnAgregar);
        accionesCaracteristica.add(btnQuitar);

        JButton btnCrear = new JButton("Crear tipo de vehículo");
        btnCrear.addActionListener(e -> crearTipo());
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());
        JPanel acciones = new JPanel();
        acciones.add(btnCrear);
        acciones.add(btnLimpiar);

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        formulario.add(tarifas);
        formulario.add(caracteristica);
        formulario.add(accionesCaracteristica);
        formulario.add(acciones);
        formulario.add(Box.createVerticalGlue());
        return formulario;
    }

    private JScrollPane crearTabla() {
        tablaCaracteristicas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCaracteristicas.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(tablaCaracteristicas);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 10, 10),
                BorderFactory.createTitledBorder("Características del nuevo tipo")));
        return scroll;
    }

    private TipoDato tipoDatoSeleccionado() {
        return TipoDato.values()[cboTipoDato.getSelectedIndex()];
    }

    private void actualizarEtiquetaTarifa() {
        lblTarifa.setText(tipoDatoSeleccionado() == TipoDato.LOGICO
                ? "% de recargo si se marca"
                : "$ por cada unidad");
    }

    private void agregarCaracteristica() {
        try {
            TipoDato tipo = tipoDatoSeleccionado();
            double tarifa = leerDecimal(txtTarifa, "La tarifa");
            if (tipo == TipoDato.LOGICO) {
                tarifa /= 100;
            }
            TarifaCaracteristica nueva = new TarifaCaracteristica(txtEtiqueta.getText(), tipo, tarifa);
            for (TarifaCaracteristica c : caracteristicas) {
                if (c.etiqueta().equalsIgnoreCase(nueva.etiqueta())) {
                    throw new IllegalArgumentException("La característica '" + nueva.etiqueta() + "' ya fue agregada.");
                }
            }
            caracteristicas.add(nueva);
            modeloCaracteristicas.addRow(new Object[] {
                    nueva.etiqueta(), cboTipoDato.getSelectedItem(), describirTarifa(nueva)});
            txtEtiqueta.setText("");
            txtTarifa.setText("");
            txtEtiqueta.requestFocusInWindow();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    private void quitarCaracteristica() {
        int fila = tablaCaracteristicas.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione en la tabla la característica que desea quitar.");
            return;
        }
        caracteristicas.remove(fila);
        modeloCaracteristicas.removeRow(fila);
    }

    private void crearTipo() {
        try {
            DefinicionTipo definicion = new DefinicionTipo(txtNombre.getText(),
                    leerDecimal(txtTasaAvaluo, "La tasa sobre el avalúo") / 100,
                    leerDecimal(txtDescuentoAnio, "El descuento por año") / 100,
                    leerDecimal(txtDescuentoMaximo, "El descuento máximo") / 100,
                    caracteristicas);
            if (catalogo.contiene(definicion.nombre())) {
                throw new IllegalArgumentException("Ya existe un tipo de vehículo llamado '" + definicion.nombre() + "'.");
            }

            catalogo.registrar(definicion.nombre(), new CreadorConfigurable(definicion));
            alCrearTipo.accept(definicion.nombre());
            JOptionPane.showMessageDialog(this,
                    "El tipo '" + definicion.nombre() + "' fue creado.\n"
                            + "Ya puede registrar vehículos de este tipo en la pestaña \"Registrar vehículo\".",
                    "Tipo de vehículo creado", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtTasaAvaluo.setText("1,5");
        txtDescuentoAnio.setText("0");
        txtDescuentoMaximo.setText("0");
        txtEtiqueta.setText("");
        txtTarifa.setText("");
        cboTipoDato.setSelectedIndex(0);
        actualizarEtiquetaTarifa();
        caracteristicas.clear();
        modeloCaracteristicas.setRowCount(0);
    }

    private static String describirTarifa(TarifaCaracteristica c) {
        return c.tipo() == TipoDato.LOGICO
                ? String.format("+%,.2f %% de recargo", c.tarifa() * 100)
                : String.format("$%,.2f por unidad", c.tarifa());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos inválidos", JOptionPane.ERROR_MESSAGE);
    }
}
