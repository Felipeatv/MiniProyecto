import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelTablaContactos extends JPanel {
    private JTable tablaContactos;
    private DefaultTableModel modeloTabla;

    public PanelTablaContactos() {
        setLayout(new BorderLayout());
        String[] columnas = {"Nombre", "Apellido", "Teléfono", "Correo", "Dirección", "Estado Civil"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaContactos = new JTable(modeloTabla);
        add(new JScrollPane(tablaContactos), BorderLayout.CENTER);
    }

    public void actualizarTabla(List<Contacto> listaContactos) {
        modeloTabla.setRowCount(0);
        for (Contacto contacto : listaContactos) {
            Object[] fila = {
                    contacto.getNombre(),
                    contacto.getApellido(),
                    contacto.getTelefono(),
                    contacto.getCorreo(),
                    contacto.getDireccion(),
                    contacto.getEstadoCivil()
            };
            modeloTabla.addRow(fila);
        }
    }
}

