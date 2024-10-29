import java.util.ArrayList;
import java.util.List;

public class GestorContactos {
    private List<Contacto> listaContactos;

    public GestorContactos() {
        this.listaContactos = new ArrayList<>();
    }

    public void agregarContacto(Contacto contacto) {
        listaContactos.add(contacto);
    }

    public void eliminarContacto(Contacto contacto) {
        listaContactos.remove(contacto);
    }

    public List<Contacto> buscarContacto(String criterio) {
        List<Contacto> resultados = new ArrayList<>();
        for (Contacto contacto : listaContactos) {
            if (contacto.getNombre().toLowerCase().contains(criterio.toLowerCase()) ||
                    contacto.getTelefono().contains(criterio)) {
                resultados.add(contacto);
            }
        }
        return resultados;
    }


    public List<Contacto> getListaContactos() { return listaContactos; }
}
