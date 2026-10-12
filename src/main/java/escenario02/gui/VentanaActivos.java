package escenario02.gui;

import escenario02.builder.ActivoFijo;
import escenario02.builder.DirectorActivos;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Cliente gráfico del Builder: construye activos fijos paso a paso, invocando
 * únicamente los métodos del Builder que corresponden a los datos ingresados,
 * y muestra la secuencia de construcción ejecutada.
 */
public class VentanaActivos extends JFrame {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /** Dato opcional del formulario y el paso del Builder que lo incorpora. */
    private record CampoOpcional(String etiqueta, String metodo, JTextField campo,
                                 BiConsumer<ActivoFijo.Builder, String> paso,
                                 Function<String, String> argumento) {
    }

    /** Activo construido junto con la secuencia de construcción que lo produjo. */
    private record Registro(ActivoFijo activo, String proceso) {
    }

    private final DirectorActivos director = new DirectorActivos();

    private final JTextField txtCodigo = new JTextField(16);
    private final JTextField txtNombre = new JTextField(16);
    private final JTextField txtPrecio = new JTextField(16);
    private final List<CampoOpcional> camposOpcionales = new ArrayList<>();

    private final JTextField txtCaracteristica = new JTextField(10);
    private final JTextField txtValorCaracteristica = new JTextField(10);
    private final DefaultTableModel modeloCaracteristicas = tablaNoEditable("Característica", "Valor");
    private final JTable tablaCaracteristicas = new JTable(modeloCaracteristicas);

    private final DefaultTableModel modeloActivos =
            tablaNoEditable("Código", "Nombre o descripción", "Precio", "Ubicación", "Custodio", "Datos opcionales");
    private final JTable tablaActivos = new JTable(modeloActivos);
    private final JTextArea txtProceso = areaDeTexto(true);
    private final JTextArea txtFicha = areaDeTexto(false);

    private final List<Registro> registros = new ArrayList<>();
    private final Set<String> codigos = new HashSet<>();
    private int ejemplos = 0;

    public VentanaActivos() {
        super("Registro de activos fijos — Builder");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        definirCamposOpcionales();

        setLayout(new BorderLayout(10, 10));
        JScrollPane formulario = new JScrollPane(crearFormulario());
        formulario.setBorder(BorderFactory.createEmptyBorder());
        formulario.getVerticalScrollBar().setUnitIncrement(16);
        add(formulario, BorderLayout.WEST);
        add(crearResultados(), BorderLayout.CENTER);

        tablaActivos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarSeleccionado();
            }
        });

        setSize(1280, 760);
        setLocationRelativeTo(null);
    }

    private void definirCamposOpcionales() {
        texto("Marca", "marca", ActivoFijo.Builder::marca);
        texto("Modelo", "modelo", ActivoFijo.Builder::modelo);
        texto("Número de serie", "numeroSerie", ActivoFijo.Builder::numeroSerie);
        texto("Ubicación", "ubicacion", ActivoFijo.Builder::ubicacion);
        texto("Custodio responsable", "custodio", ActivoFijo.Builder::custodio);
        camposOpcionales.add(new CampoOpcional("Fecha de adquisición (dd/mm/aaaa)", "fechaAdquisicion",
                new JTextField(16),
                (b, v) -> b.fechaAdquisicion(leerFecha(v)),
                v -> {
                    LocalDate f = leerFecha(v);
                    return "LocalDate.of(" + f.getYear() + ", " + f.getMonthValue() + ", " + f.getDayOfMonth() + ")";
                }));
        camposOpcionales.add(new CampoOpcional("Garantía (meses)", "garantiaMeses", new JTextField(16),
                (b, v) -> b.garantiaMeses(leerEntero(v, "La garantía")),
                v -> String.valueOf(leerEntero(v, "La garantía"))));
        texto("Proveedor", "proveedor", ActivoFijo.Builder::proveedor);
        texto("Observaciones", "observaciones", ActivoFijo.Builder::observaciones);
    }

    private void texto(String etiqueta, String metodo, BiConsumer<ActivoFijo.Builder, String> paso) {
        camposOpcionales.add(new CampoOpcional(etiqueta, metodo, new JTextField(16), paso, VentanaActivos::literal));
    }

    private JPanel crearFormulario() {
        JPanel obligatorios = new JPanel(new GridBagLayout());
        obligatorios.setBorder(BorderFactory.createTitledBorder("Información obligatoria"));
        agregarFila(obligatorios, 0, "Código institucional *", txtCodigo);
        agregarFila(obligatorios, 1, "Nombre o descripción *", txtNombre);
        agregarFila(obligatorios, 2, "Precio de adquisición ($) *", txtPrecio);

        JPanel opcionales = new JPanel(new GridBagLayout());
        opcionales.setBorder(BorderFactory.createTitledBorder("Información opcional (solo la que aplique)"));
        for (int i = 0; i < camposOpcionales.size(); i++) {
            agregarFila(opcionales, i, camposOpcionales.get(i).etiqueta(), camposOpcionales.get(i).campo());
        }

        JPanel caracteristicas = new JPanel(new BorderLayout(4, 4));
        caracteristicas.setBorder(BorderFactory.createTitledBorder("Características técnicas"));
        JPanel entrada = new JPanel(new GridBagLayout());
        agregarFila(entrada, 0, "Característica", txtCaracteristica);
        agregarFila(entrada, 1, "Valor", txtValorCaracteristica);
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarCaracteristica());
        JButton btnQuitar = new JButton("Quitar seleccionada");
        btnQuitar.addActionListener(e -> quitarCaracteristica());
        JPanel botonesCaracteristica = new JPanel();
        botonesCaracteristica.add(btnAgregar);
        botonesCaracteristica.add(btnQuitar);
        JScrollPane scrollCaracteristicas = new JScrollPane(tablaCaracteristicas);
        scrollCaracteristicas.setPreferredSize(new Dimension(300, 90));
        tablaCaracteristicas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        caracteristicas.add(entrada, BorderLayout.NORTH);
        caracteristicas.add(botonesCaracteristica, BorderLayout.CENTER);
        caracteristicas.add(scrollCaracteristicas, BorderLayout.SOUTH);

        JButton btnConstruir = new JButton("Construir activo");
        btnConstruir.setFont(btnConstruir.getFont().deriveFont(Font.BOLD));
        btnConstruir.addActionListener(e -> construir());
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());
        JPanel acciones = new JPanel();
        acciones.add(btnConstruir);
        acciones.add(btnLimpiar);

        JPanel plantillas = new JPanel();
        plantillas.setBorder(BorderFactory.createTitledBorder("Ejemplos construidos por el Director"));
        JButton btnComputador = new JButton("Computador");
        btnComputador.addActionListener(e -> ejemploComputador());
        JButton btnMueble = new JButton("Mueble");
        btnMueble.addActionListener(e -> ejemploMueble());
        plantillas.add(btnComputador);
        plantillas.add(btnMueble);

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        formulario.add(obligatorios);
        formulario.add(opcionales);
        formulario.add(caracteristicas);
        formulario.add(acciones);
        formulario.add(plantillas);
        formulario.add(Box.createVerticalGlue());
        return formulario;
    }

    private JComponent crearResultados() {
        tablaActivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaActivos.setFillsViewportHeight(true);
        JScrollPane scrollActivos = new JScrollPane(tablaActivos);
        scrollActivos.setBorder(BorderFactory.createTitledBorder("Activos fijos registrados"));

        JPanel detalle = new JPanel(new GridLayout(1, 2, 10, 0));
        detalle.add(conTitulo(txtProceso, "Proceso de construcción (Builder)"));
        detalle.add(conTitulo(txtFicha, "Ficha del activo (solo datos registrados)"));

        scrollActivos.setPreferredSize(new Dimension(700, 220));
        detalle.setPreferredSize(new Dimension(700, 380));

        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollActivos, detalle);
        division.setResizeWeight(0.35);
        division.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        return division;
    }

    private void construir() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            double precio = leerPrecio();

            ActivoFijo.Builder builder = new ActivoFijo.Builder(codigo, nombre, precio);
            List<String> pasos = new ArrayList<>();
            pasos.add(String.format(Locale.ROOT, "new ActivoFijo.Builder(%s, %s, %.2f)",
                    literal(codigo), literal(nombre), precio));

            for (CampoOpcional c : camposOpcionales) {
                String valor = c.campo().getText().trim();
                if (!valor.isEmpty()) {
                    c.paso().accept(builder, valor);
                    pasos.add("." + c.metodo() + "(" + c.argumento().apply(valor) + ")");
                }
            }
            for (int i = 0; i < modeloCaracteristicas.getRowCount(); i++) {
                String nombreCar = (String) modeloCaracteristicas.getValueAt(i, 0);
                String valorCar = (String) modeloCaracteristicas.getValueAt(i, 1);
                builder.caracteristica(nombreCar, valorCar);
                pasos.add(".caracteristica(" + literal(nombreCar) + ", " + literal(valorCar) + ")");
            }

            verificarCodigoDisponible(codigo);
            ActivoFijo activo = builder.build();
            pasos.add(".build();");
            registrar(activo, String.join("\n        ", pasos));
            limpiar();
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    private void ejemploComputador() {
        String codigo = siguienteCodigoEjemplo();
        ActivoFijo activo = director.construirComputador(
                new ActivoFijo.Builder(codigo, "Computador de escritorio", 950),
                "HP", "ProDesk 400 G9", "MXL-4471", "Intel Core i5", 36);
        registrar(activo, "director.construirComputador(\n"
                + "        new ActivoFijo.Builder(" + literal(codigo) + ", \"Computador de escritorio\", 950.00),\n"
                + "        \"HP\", \"ProDesk 400 G9\", \"MXL-4471\", \"Intel Core i5\", 36);\n\n"
                + "// Pasos que ejecuta el Director sobre el Builder:\n"
                + "// .marca(...).modelo(...).numeroSerie(...)\n"
                + "// .caracteristica(\"Procesador\", ...).garantiaMeses(...).build()");
    }

    private void ejemploMueble() {
        String codigo = siguienteCodigoEjemplo();
        ActivoFijo activo = director.construirMueble(
                new ActivoFijo.Builder(codigo, "Mesa de reuniones", 450),
                "Madera", "2,40 × 1,20 m", "Sala 3", "María López");
        registrar(activo, "director.construirMueble(\n"
                + "        new ActivoFijo.Builder(" + literal(codigo) + ", \"Mesa de reuniones\", 450.00),\n"
                + "        \"Madera\", \"2,40 × 1,20 m\", \"Sala 3\", \"María López\");\n\n"
                + "// Pasos que ejecuta el Director sobre el Builder:\n"
                + "// .caracteristica(\"Material\", ...).caracteristica(\"Dimensiones\", ...)\n"
                + "// .ubicacion(...).custodio(...).build()");
    }

    private String siguienteCodigoEjemplo() {
        String codigo;
        do {
            codigo = String.format("EJ-%03d", ++ejemplos);
        } while (codigos.contains(codigo));
        return codigo;
    }

    private void registrar(ActivoFijo activo, String proceso) {
        codigos.add(activo.getCodigo().toUpperCase(Locale.ROOT));
        registros.add(new Registro(activo, proceso));
        modeloActivos.addRow(new Object[] {
                activo.getCodigo(),
                activo.getNombre(),
                String.format("$%,.2f", activo.getPrecioAdquisicion()),
                activo.getUbicacion().orElse("—"),
                activo.getCustodio().orElse("—"),
                contarDatosOpcionales(activo)});
        int fila = modeloActivos.getRowCount() - 1;
        tablaActivos.setRowSelectionInterval(fila, fila);
        tablaActivos.scrollRectToVisible(tablaActivos.getCellRect(fila, 0, true));
    }

    private void mostrarSeleccionado() {
        int fila = tablaActivos.getSelectedRow();
        if (fila < 0) {
            txtProceso.setText("");
            txtFicha.setText("");
            return;
        }
        Registro registro = registros.get(fila);
        txtProceso.setText(registro.proceso());
        txtFicha.setText(registro.activo().describir());
        txtProceso.setCaretPosition(0);
        txtFicha.setCaretPosition(0);
    }

    private void agregarCaracteristica() {
        String nombre = txtCaracteristica.getText().trim();
        String valor = txtValorCaracteristica.getText().trim();
        if (nombre.isEmpty() || valor.isEmpty()) {
            mostrarError("Ingrese el nombre y el valor de la característica.");
            return;
        }
        for (int i = 0; i < modeloCaracteristicas.getRowCount(); i++) {
            if (nombre.equalsIgnoreCase((String) modeloCaracteristicas.getValueAt(i, 0))) {
                mostrarError("La característica '" + nombre + "' ya fue agregada.");
                return;
            }
        }
        modeloCaracteristicas.addRow(new Object[] {nombre, valor});
        txtCaracteristica.setText("");
        txtValorCaracteristica.setText("");
        txtCaracteristica.requestFocusInWindow();
    }

    private void quitarCaracteristica() {
        int fila = tablaCaracteristicas.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione en la tabla la característica que desea quitar.");
            return;
        }
        modeloCaracteristicas.removeRow(fila);
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        camposOpcionales.forEach(c -> c.campo().setText(""));
        txtCaracteristica.setText("");
        txtValorCaracteristica.setText("");
        modeloCaracteristicas.setRowCount(0);
        txtCodigo.requestFocusInWindow();
    }

    private void verificarCodigoDisponible(String codigo) {
        if (codigos.contains(codigo.toUpperCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Ya existe un activo con el código " + codigo + ".");
        }
    }

    /** Un precio vacío se envía como 0 para que build() informe todos los datos obligatorios faltantes. */
    private double leerPrecio() {
        String valor = txtPrecio.getText().trim();
        if (valor.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(valor.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El precio de adquisición debe ser un número.");
        }
    }

    private static int contarDatosOpcionales(ActivoFijo a) {
        int total = a.getCaracteristicas().size();
        for (var dato : List.of(a.getMarca(), a.getModelo(), a.getNumeroSerie(), a.getUbicacion(),
                a.getCustodio(), a.getFechaAdquisicion(), a.getGarantiaMeses(), a.getProveedor(),
                a.getObservaciones())) {
            if (dato.isPresent()) {
                total++;
            }
        }
        return total;
    }

    private static LocalDate leerFecha(String valor) {
        try {
            return LocalDate.parse(valor, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha de adquisición debe tener el formato dd/mm/aaaa.");
        }
    }

    private static int leerEntero(String valor, String nombre) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nombre + " debe ser un número entero.");
        }
    }

    private static String literal(String valor) {
        return "\"" + valor.replace("\"", "\\\"") + "\"";
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "No se pudo construir el activo", JOptionPane.ERROR_MESSAGE);
    }

    private static DefaultTableModel tablaNoEditable(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private static JTextArea areaDeTexto(boolean monoespaciada) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setMargin(new Insets(6, 8, 6, 8));
        if (monoespaciada) {
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        }
        return area;
    }

    private static JScrollPane conTitulo(JComponent componente, String titulo) {
        JScrollPane scroll = new JScrollPane(componente);
        scroll.setBorder(BorderFactory.createTitledBorder(titulo));
        return scroll;
    }

    private static void agregarFila(JPanel panel, int fila, String etiqueta, JComponent componente) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 6, 3, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridy = fila;

        c.gridx = 0;
        panel.add(new JLabel(etiqueta), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        panel.add(componente, c);
    }
}
