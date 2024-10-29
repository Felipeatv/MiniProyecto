import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GestorContactosApp extends JFrame {
    private GestorContactos gestorContactos;
    private JTable tablaContactos;
    private JTextField campoNombre, campoApellido, campoTelefono, campoCorreo, campoDireccion, campoBusqueda;
    private JRadioButton solteroRB, casadoRB, unionLibreRB, divorciadoRB;
    private JButton botonAgregar, botonBuscar, botonEliminar, botonEditar, botonImagen, botonGuardar, botonCancelar;
    private DefaultTableModel modeloTabla;
    private JProgressBar barraProgreso;

    public GestorContactosApp() {
        gestorContactos = new GestorContactos();
        setTitle("Gestor de Contactos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        inicializarComponentes();
        crearMenu();
    }

    private void inicializarComponentes() {
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        campoBusqueda = new JTextField(20);
        botonBuscar = new JButton("Buscar");
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(botonBuscar, BorderLayout.EAST);

        // Panel de tabla
        String[] columnas = {"Nombre", "Apellido", "Teléfono", "Correo", "Dirección", "Estado Civil"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaContactos = new JTable(modeloTabla);
        JScrollPane panelTabla = new JScrollPane(tablaContactos);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(10, 2, 5, 5));
        campoNombre = new JTextField(20);
        campoApellido = new JTextField(20);
        campoTelefono = new JTextField(20);
        campoCorreo = new JTextField(20);
        campoDireccion = new JTextField(20);

        // Validaciones de campos
        campoTelefono.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        campoCorreo.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                actualizarProgreso();
            }
        });

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(campoNombre);
        panelFormulario.add(new JLabel("Apellido:"));
        panelFormulario.add(campoApellido);
        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(campoTelefono);
        panelFormulario.add(new JLabel("Correo:"));
        panelFormulario.add(campoCorreo);
        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(campoDireccion);

        // Estado civil
        ButtonGroup grupoEstadoCivil = new ButtonGroup();
        solteroRB = new JRadioButton("Soltero");
        casadoRB = new JRadioButton("Casado");
        unionLibreRB = new JRadioButton("Unión Libre");
        divorciadoRB = new JRadioButton("Divorciado");
        grupoEstadoCivil.add(solteroRB);
        grupoEstadoCivil.add(casadoRB);
        grupoEstadoCivil.add(unionLibreRB);
        grupoEstadoCivil.add(divorciadoRB);

        panelFormulario.add(new JLabel("Estado Civil:"));
        JPanel panelEstadoCivil = new JPanel(new FlowLayout());
        panelEstadoCivil.add(solteroRB);
        panelEstadoCivil.add(casadoRB);
        panelEstadoCivil.add(unionLibreRB);
        panelEstadoCivil.add(divorciadoRB);
        panelFormulario.add(panelEstadoCivil);

        botonImagen = new JButton("Agregar Imagen");
        panelFormulario.add(new JLabel("Imagen:"));
        panelFormulario.add(botonImagen);

        botonAgregar = new JButton("Agregar Contacto");
        botonGuardar = new JButton("Guardar");
        botonCancelar = new JButton("Cancelar");

        botonAgregar.addActionListener(this::agregarContacto);
        botonGuardar.addActionListener(this::guardarContacto);

        panelFormulario.add(botonAgregar);
        panelFormulario.add(botonGuardar);
        panelFormulario.add(botonCancelar);

        // Barra de progreso
        barraProgreso = new JProgressBar(0, 100);
        add(barraProgreso, BorderLayout.SOUTH);  // Colocación en el borde inferior

        // Añadir componentes al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        add(panelFormulario, BorderLayout.EAST);

        // Eventos
        botonBuscar.addActionListener(this::buscarContacto);

        // Evento de mouse para seleccionar contacto en la tabla
        tablaContactos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    llenarFormularioDesdeTabla();
                }
            }
        });

        // Atajos de teclado
        configurarAtajosDeTeclado();
    }

    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu archivoMenu = new JMenu("Archivo");
        JMenuItem nuevoItem = new JMenuItem("Nuevo");
        JMenuItem guardarItem = new JMenuItem("Guardar");
        JMenuItem salirItem = new JMenuItem("Salir");

        archivoMenu.add(nuevoItem);
        archivoMenu.add(guardarItem);
        archivoMenu.addSeparator();
        archivoMenu.add(salirItem);

        JMenu ayudaMenu = new JMenu("Ayuda");
        JMenuItem acercaDeItem = new JMenuItem("Acerca de");

        ayudaMenu.add(acercaDeItem);

        menuBar.add(archivoMenu);
        menuBar.add(ayudaMenu);

        setJMenuBar(menuBar);

        // Acciones del menú
        nuevoItem.addActionListener(e -> limpiarFormulario());
        guardarItem.addActionListener(this::guardarContacto);
        salirItem.addActionListener(e -> System.exit(0));
        acercaDeItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Gestor de Contactos v1.0"));
    }

    private void actualizarProgreso() {
        int progreso = 0;
        if (!campoNombre.getText().isEmpty()) progreso += 20;
        if (!campoApellido.getText().isEmpty()) progreso += 20;
        if (!campoTelefono.getText().isEmpty()) progreso += 20;
        if (!campoCorreo.getText().isEmpty()) progreso += 20;
        if (!campoDireccion.getText().isEmpty()) progreso += 20;
        barraProgreso.setValue(progreso);
    }

    private void configurarAtajosDeTeclado() {
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "nuevoContacto");
        actionMap.put("nuevoContacto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "editarContacto");
        actionMap.put("editarContacto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                llenarFormularioDesdeTabla();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "eliminarContacto");
        actionMap.put("eliminarContacto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarContactoSeleccionado();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "guardarContacto");
        actionMap.put("guardarContacto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarContacto(e);
            }
        });
    }

    private void llenarFormularioDesdeTabla() {
        int selectedRow = tablaContactos.getSelectedRow();
        if (selectedRow >= 0) {
            campoNombre.setText((String) modeloTabla.getValueAt(selectedRow, 0));
            campoApellido.setText((String) modeloTabla.getValueAt(selectedRow, 1));
            campoTelefono.setText((String) modeloTabla.getValueAt(selectedRow, 2));
            campoCorreo.setText((String) modeloTabla.getValueAt(selectedRow, 3));
            campoDireccion.setText((String) modeloTabla.getValueAt(selectedRow, 4));

            String estadoCivil = (String) modeloTabla.getValueAt(selectedRow, 5);
            solteroRB.setSelected(estadoCivil.equals("Soltero"));
            casadoRB.setSelected(estadoCivil.equals("Casado"));
            unionLibreRB.setSelected(estadoCivil.equals("Unión Libre"));
            divorciadoRB.setSelected(estadoCivil.equals("Divorciado"));
        }
    }

    private void limpiarFormulario() {
        campoNombre.setText("");
        campoApellido.setText("");
        campoTelefono.setText("");
        campoCorreo.setText("");
        campoDireccion.setText("");
        solteroRB.setSelected(false);
        casadoRB.setSelected(false);
        unionLibreRB.setSelected(false);
        divorciadoRB.setSelected(false);
        barraProgreso.setValue(0);  // Reiniciar barra de progreso
    }

    private void agregarContacto(ActionEvent e) {
        String nombre = campoNombre.getText();
        String apellido = campoApellido.getText();
        String telefono = campoTelefono.getText();
        String correo = campoCorreo.getText();
        String direccion = campoDireccion.getText();
        String estadoCivil = getEstadoCivilSeleccionado();

        if (!validarCorreo(correo)) {
            JOptionPane.showMessageDialog(this, "Correo electrónico inválido.");
            return;
        }

        Contacto nuevoContacto = new Contacto(nombre, apellido, telefono, correo, direccion, estadoCivil);
        gestorContactos.agregarContacto(nuevoContacto);
        actualizarTabla();
        limpiarFormulario();  // Limpiar formulario después de agregar
    }

    private boolean validarCorreo(String correo) {
        return correo.contains("@") && correo.endsWith(".com");
    }

    private void guardarContacto(ActionEvent e) {
        int selectedRow = tablaContactos.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de guardar los cambios?");
            if (confirm == JOptionPane.YES_OPTION) {
                Contacto contacto = gestorContactos.getListaContactos().get(selectedRow);
                contacto.setNombre(campoNombre.getText());
                contacto.setApellido(campoApellido.getText());
                contacto.setTelefono(campoTelefono.getText());
                contacto.setCorreo(campoCorreo.getText());
                contacto.setDireccion(campoDireccion.getText());
                contacto.setEstadoCivil(getEstadoCivilSeleccionado());
                actualizarTabla();
                limpiarFormulario();  // Limpiar formulario después de guardar
            }
        }
    }

    private void eliminarContactoSeleccionado() {
        int selectedRow = tablaContactos.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el contacto?");
            if (confirm == JOptionPane.YES_OPTION) {
                Contacto contacto = gestorContactos.getListaContactos().get(selectedRow);
                gestorContactos.eliminarContacto(contacto);
                actualizarTabla();
            }
        }
    }

    private void buscarContacto(ActionEvent e) {
        String criterio = campoBusqueda.getText();
        List<Contacto> resultados = gestorContactos.buscarContacto(criterio);
        actualizarTabla(resultados);
    }

    private String getEstadoCivilSeleccionado() {
        if (solteroRB.isSelected()) return "Soltero";
        if (casadoRB.isSelected()) return "Casado";
        if (unionLibreRB.isSelected()) return "Unión Libre";
        if (divorciadoRB.isSelected()) return "Divorciado";
        return "";
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Contacto contacto : gestorContactos.getListaContactos()) {
            modeloTabla.addRow(new Object[]{
                    contacto.getNombre(),
                    contacto.getApellido(),
                    contacto.getTelefono(),
                    contacto.getCorreo(),
                    contacto.getDireccion(),
                    contacto.getEstadoCivil()
            });
        }
    }

    private void actualizarTabla(List<Contacto> contactos) {
        modeloTabla.setRowCount(0);
        for (Contacto contacto : contactos) {
            modeloTabla.addRow(new Object[]{
                    contacto.getNombre(),
                    contacto.getApellido(),
                    contacto.getTelefono(),
                    contacto.getCorreo(),
                    contacto.getDireccion(),
                    contacto.getEstadoCivil()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestorContactosApp app = new GestorContactosApp();
            app.setVisible(true);
        });
    }
}
