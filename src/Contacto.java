public class Contacto {
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String direccion;
    private String estadoCivil;

    public Contacto(String nombre, String apellido, String telefono, String correo, String direccion, String estadoCivil) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.estadoCivil = estadoCivil;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public String getEstadoCivil() { return estadoCivil; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
}
