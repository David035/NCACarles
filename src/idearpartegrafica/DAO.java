package idearpartegrafica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class DAO {

    public boolean idExisteEnBD(String id) {
        try (Connection conn = dbConnection.getConexion()) {
            String sql = "SELECT COUNT(*) FROM professors WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;  // ← Esto es correcto
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consultar el ID: " + e.getMessage());
        }
        return false;  // ← Asegúrate de devolver false si hay un error
    }

    public boolean insertarProfesor(String id, String nomCog, String telefon, String correu) {
        try (Connection conn = dbConnection.getConexion()) {
            String sql = "INSERT INTO professors VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, nomCog);
            ps.setString(3, telefon);
            ps.setString(4, correu);;
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    public boolean insertarModuloProfesor(int idProfesor, int idModulo) {
        String sql = "INSERT INTO moduloprofessor (idprofessor, idmodul) VALUES (?, ?)";
        try (Connection conn = dbConnection.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProfesor);
            ps.setInt(2, idModulo);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se insertó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Indica error
        }
    }
    
    public boolean actualizarProfesor(String idProf, String nouNomCog, String nouTelefon, String nouCorreu, List<String> novesAssignatures) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE professors SET ");
        List<Object> parametros = new ArrayList<>();
        boolean hayCamposParaActualizar = false;

        if (nouNomCog != null && !nouNomCog.isEmpty()) {
            sqlBuilder.append("nom = ?, ");
            parametros.add(nouNomCog);
            hayCamposParaActualizar = true;
        }
        if (nouTelefon != null && !nouTelefon.isEmpty()) {
            sqlBuilder.append("telefon = ?, ");
            parametros.add(nouTelefon);
            hayCamposParaActualizar = true;
        }
        if (nouCorreu != null && !nouCorreu.isEmpty()) {
            sqlBuilder.append("correu = ?, ");
            parametros.add(nouCorreu);
            hayCamposParaActualizar = true;
        }
        if (novesAssignatures != null && !novesAssignatures.isEmpty()) {
            String assignaturasComoCadena = novesAssignatures.stream()
                    .collect(Collectors.joining(","));
            sqlBuilder.append("assignatures = ?, ");
            parametros.add(assignaturasComoCadena);
            hayCamposParaActualizar = true;
        }

        // Eliminar la coma final si se añadieron campos
        if (!hayCamposParaActualizar) {
            return true; // No hay campos para actualizar, consideramos éxito
        }
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());

        sqlBuilder.append(" WHERE id = ?");
        parametros.add(idProf);

        try (Connection conn = dbConnection.getConexion(); PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el profesor: " + e.getMessage());
            return false;
        }
    }
    
    public List<String> obtenerNombresModuls() {
        List<String> nombresModuls = new ArrayList<>();
        String sql = "SELECT id, nom FROM moduls"; // Suponiendo que la columna con el nombre del módulo es "Nom"

        try (Connection conn = dbConnection.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idModul = rs.getInt("id"); // Obtén el valor de la columna "id"
                String nomModul = rs.getString("nom"); // Obtén el valor de la columna "nom"
                String textoAMostrar = idModul + "-" + nomModul; // Combina ID y nombre
                nombresModuls.add(textoAMostrar);;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción apropiadamente (mostrar mensaje, log, etc.)
        }
        return nombresModuls;
    }
        
}


