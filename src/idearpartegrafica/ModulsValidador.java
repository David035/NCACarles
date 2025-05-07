package idearpartegrafica;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

public class ModulsValidador {

    public boolean validarIdModulo(String idModuloTexto) {
        if (idModuloTexto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese el ID del módulo.");
            return false;
        }
        try {
            Integer.parseInt(idModuloTexto);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID del módulo debe ser un número entero.");
            return false;
        }
    }

    public boolean validarNombreModulo(String nombreModulo) {
        if (nombreModulo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese el nombre del módulo.");
            return false;
        }
        return true;
    }

    public boolean validarCicleFormatiu(ButtonGroup cicleFormatiu) {
        if (cicleFormatiu.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione el ciclo formativo.");
            return false; // Indica que la validación falló
        }
        return true; // Indica que la validación fue exitosa
    }

    public boolean validarCredits(String creditsTexto) {
        if (creditsTexto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese los créditos.");
            return false;
        }
        try {
            Integer.parseInt(creditsTexto);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Los créditos deben ser un número entero.");
            return false;
        }
    }

    public boolean validarProfesorResponsable(Object profesorResponsable) {
        if (profesorResponsable == null || profesorResponsable.toString().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un profesor responsable.");
            return false;
        }
        return true;
    }

    // Método de validación general que ahora recibe el ButtonGroup
    public boolean validarCamposModulo(String idModuloTexto, String nombreModulo, ButtonGroup cicleFormatiu, String creditsTexto, Object profesorResponsable) {
    boolean cicleValidat = validarCicleFormatiu(cicleFormatiu); // Llama a la versión que devuelve boolean
    return validarIdModulo(idModuloTexto)
           && validarNombreModulo(nombreModulo)
           && cicleValidat // Usamos el boolean directamente
           && validarCredits(creditsTexto)
           && validarProfesorResponsable(profesorResponsable);
}
}
