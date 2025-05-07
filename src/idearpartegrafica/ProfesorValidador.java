package idearpartegrafica;

import java.util.List;
import javax.swing.JOptionPane;

public class ProfesorValidador {

    public boolean validarCampos(String id, String nomCog, String telefon, String correu, List<String> assignatures) {
        if (id.isEmpty() || nomCog.isEmpty() || telefon.isEmpty() || correu.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los campos id, nom, telefon y correu son obligatorios.");
            return false;
        }

        if (!nomCog.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$")) {
            JOptionPane.showMessageDialog(null, "Nombre y apellido inválidos.");
            return false;
        }

        if (!telefon.matches("^\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "Teléfono inválido. Debe tener 9 dígitos.");
            return false;
        }

        if (!correu.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(null, "Correo electrónico inválido.");
            return false;
        }

        ProfesorDAO profesorDAO = new ProfesorDAO();
        if (profesorDAO.idExisteEnBD(id)) {
            JOptionPane.showMessageDialog(null, "El ID ya existe en la base de datos.");
            return false;
        }

        return true;
    }
    
    public boolean validarIdProfesor(String id){
        if (id.isEmpty()){
            JOptionPane.showMessageDialog(null, "El ID es obligatorio.");
            return false;
        }
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        if (!profesorDAO.idExisteEnBD(id)) {
            JOptionPane.showMessageDialog(null, "El ID no existe en la base de datos.");
            return false;
        }
        
        return true;
    }
}
