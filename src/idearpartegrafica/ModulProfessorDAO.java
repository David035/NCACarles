package idearpartegrafica;

import java.sql.*;

public class ModulProfessorDAO {

    public boolean insertarModuloProfesor(int idProfesor, int idModulo) {
        String sql = "INSERT INTO modulprofessor (idprofessor, idmodul) VALUES (?, ?)";
        try (Connection conn = dbConnection.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProfesor);
            ps.setInt(2, idModulo);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean existeProfesorParaModulo(int idModulo) {
        String sql = "SELECT COUNT(*) FROM modulprofessor WHERE idmodul = ?";
        try (Connection conn = dbConnection.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idModulo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Devuelve true si ya existe al menos un profesor para este módulo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // En caso de error, asumimos que no existe (o puedes manejarlo de otra manera)
    }
}