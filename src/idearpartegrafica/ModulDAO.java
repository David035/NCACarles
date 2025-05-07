package idearpartegrafica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ModulDAO { // Renombrado desde DAO si solo maneja módulos

    public List<String> obtenerNombresModuls() {
        List<String> nombresModuls = new ArrayList<>();
        String sql = "SELECT id, nom FROM moduls";

        try (Connection conn = dbConnection.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idModul = rs.getInt("id");
                String nomModul = rs.getString("nom");
                String textoAMostrar = idModul + "-" + nomModul;
                nombresModuls.add(textoAMostrar);;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción apropiadamente
        }
        return nombresModuls;
    }
    
    public boolean insertarModuloConResponsable(int idModulo, String nombreModulo, String cicleFormatiuSeleccionado, int idProfesorResponsable, int credits) {
        String sql = "INSERT INTO moduls (id, nom, cicle_formatiu, id_professor, credits) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idModulo);
            ps.setString(2, nombreModulo);
            ps.setString(3, cicleFormatiuSeleccionado);
            ps.setInt(4, idProfesorResponsable);
            ps.setInt(5, credits);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar el módulo en la base de datos.");
            return false;
        }
    }
}