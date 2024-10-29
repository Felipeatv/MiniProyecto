// FormularioContacto.java
import javax.swing.*;
import java.awt.*;

public class FormularioContacto extends JPanel {
    private JTextField campoNombre, campoApellido, campoTelefono, campoCorreo, campoDireccion;
    private JRadioButton solteroRB, casadoRB, unionLibreRB, divorciadoRB;
    private ButtonGroup grupoEstadoCivil;

    public FormularioContacto() {
        setLayout(new GridLayout(10, 2, 5, 5));

        campoNombre = new JTextField(20);
        campoApellido = new JTextField(20);
        campoTelefono = new JTextField(20);
        campoCorreo = new JTextField(20);
        campoDireccion = new JTextField(20);

        add(new JLabel("Nombre:"));
        add(campoNombre);
        add(new JLabel("Apellido:"));
        add(campoApellido);
        add(new JLabel("Teléfono:"));
        add(campoTelefono);
        add(new JLabel("Correo:"));
        add(campoCorreo);
        add(new JLabel("Dirección:"));
        add(campoDireccion);

        grupoEstadoCivil = new ButtonGroup();
        solteroRB = new JRadioButton("Soltero");
        casadoRB = new JRadioButton("Casado");
        unionLibreRB = new JRadioButton("Unión Libre");
        divorciadoRB = new JRadioButton("Divorciado");
        grupoEstadoCivil.add(solteroRB);
        grupoEstadoCivil.add(casadoRB);
        grupoEstadoCivil.add(unionLibreRB);
        grupoEstadoCivil.add(divorciadoRB);

        JPanel panelEstadoCivil = new JPanel(new FlowLayout());
        panelEstadoCivil.add(solteroRB);
        panelEstadoCivil.add(casadoRB);
        panelEstadoCivil.add(unionLibreRB);
        panelEstadoCivil.add(divorciadoRB);
        add(new JLabel("Estado Civil:"));
        add(panelEstadoCivil);
    }

    public Contacto obtenerContacto() {
        return new Contacto(
                campoNombre.getText(),
                campoApellido.getText(),
                campoTelefono.getText(),
                campoCorreo.getText(),
                campoDireccion.getText(),
                getEstadoCivilSeleccionado()
        );
    }

    public void limpiarFormulario() {
        campoNombre.setText("");
        campoApellido.setText("");
        campoTelefono.setText("");
        campoCorreo.setText("");
        campoDireccion.setText("");
        grupoEstadoCivil.clearSelection();
    }

    private String getEstadoCivilSeleccionado() {
        if (solteroRB.isSelected()) return "Soltero";
        if (casadoRB.isSelected()) return "Casado";
        if (unionLibreRB.isSelected()) return "Unión Libre";
        if (divorciadoRB.isSelected()) return "Divorciado";
        return "";
    }
}


