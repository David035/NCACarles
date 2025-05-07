package idearpartegrafica;

import java.sql.*;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ProfesorDAO {

    public boolean idExisteEnBD(String id) {
        try (Connection conn = dbConnection.getConexion()) {
            String sql = "SELECT COUNT(*) FROM professors WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consultar el ID: " + e.getMessage());
        }
        return false;
    }

    public boolean insertarProfesor(String id, String nomCog, String telefon, String correu) {
        try (Connection conn = dbConnection.getConexion()) {
            String sql = "INSERT INTO professors VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, nomCog);
            ps.setString(3, telefon);
            ps.setString(4, correu);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarProfesor(String id, String nomCog, String telefon, String correu) {
        String sql = "UPDATE professors SET nom = ?, telefon = ?, correu = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomCog);
            ps.setString(2, telefon);
            ps.setString(3, correu);
            ps.setString(4, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean actualizarAsignaturasProfesor(String idProfesor, List<String> nuevasAsignaturas) {
        boolean exito = true;
        Connection conn = null;
        PreparedStatement psEliminar = null;
        PreparedStatement psInsertar = null;
        PreparedStatement psVerificar = null;

        try {
            conn = dbConnection.getConexion();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Eliminar las asignaturas existentes para el profesor
            String sqlEliminar = "DELETE FROM modulprofessor WHERE idprofessor = ?";
            psEliminar = conn.prepareStatement(sqlEliminar);
            psEliminar.setString(1, idProfesor);
            psEliminar.executeUpdate();

            // 2. Insertar las nuevas asignaturas, verificando que no haya otro profesor para el módulo
            String sqlInsertar = "INSERT INTO modulprofessor (idprofessor, idmodul) VALUES (?, ?)";
            psInsertar = conn.prepareStatement(sqlInsertar);
            String sqlVerificar = "SELECT COUNT(*) FROM modulprofessor WHERE idmodul = ?";
            psVerificar = conn.prepareStatement(sqlVerificar);

            for (String asignaturaTexto : nuevasAsignaturas) {
                String[] partes = asignaturaTexto.split("-");
                if (partes.length > 0) {
                    try {
                        int idModulo = Integer.parseInt(partes[0].trim());

                        // *** NUEVA VERIFICACIÓN ***
                        psVerificar.setInt(1, idModulo);
                        ResultSet rs = psVerificar.executeQuery();
                        if (rs.next() && rs.getInt(1) > 0) {
                            exito = false;
                            JOptionPane.showMessageDialog(null, "El módulo " + asignaturaTexto + " ya tiene un profesor asignado.");
                            break; // No seguir insertando para este profesor
                        } else {
                            psInsertar.setString(1, idProfesor);
                            psInsertar.setInt(2, idModulo);
                            psInsertar.executeUpdate();
                        }
                        if (rs != null) {
                            rs.close();
                        }

                    } catch (NumberFormatException e) {
                        exito = false;
                        e.printStackTrace();
                        break;
                    }
                } else {
                    exito = false;
                    break;
                }
            }

            if (exito) {
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            exito = false;
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (psEliminar != null) {
                    psEliminar.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (psInsertar != null) {
                    psInsertar.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (psVerificar != null) {
                    psVerificar.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }
    
    public boolean eliminarProfesor(String idProfesor) {
        Connection conn = null;
        PreparedStatement psEliminarModulos = null;
        PreparedStatement psEliminarProfesor = null;

        try {
            conn = dbConnection.getConexion();
            conn.setAutoCommit(false); // Iniciar transacción para asegurar la integridad

            // 1. Eliminar las relaciones del profesor en modulprofessor
            String sqlEliminarModulos = "DELETE FROM modulprofessor WHERE idprofessor = ?";
            psEliminarModulos = conn.prepareStatement(sqlEliminarModulos);
            psEliminarModulos.setString(1, idProfesor);
            psEliminarModulos.executeUpdate();

            // 2. Eliminar el profesor de la tabla professors
            String sqlEliminarProfesor = "DELETE FROM professors WHERE id = ?"; // Asumo que la PK en professors es 'id'
            psEliminarProfesor = conn.prepareStatement(sqlEliminarProfesor);
            psEliminarProfesor.setString(1, idProfesor);
            int filasAfectadas = psEliminarProfesor.executeUpdate();

            conn.commit(); // Si ambas operaciones tienen éxito, confirmar la transacción
            return filasAfectadas > 0;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Si hay algún error, deshacer la transacción
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el profesor y sus asignaturas.");
            return false;
        } finally {
            try {
                if (psEliminarModulos != null) {
                    psEliminarModulos.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (psEliminarProfesor != null) {
                    psEliminarProfesor.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public DefaultTableModel obtenerModeloTablaProfesoresConModulos() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Teléfono", "Correo", "Módulos"}, // Aquí especificamos los nombres de las columnas
                0 // Inicialmente 0 filas
        );

        String sql = "SELECT p.id, p.nom, p.telefon, p.correu, "
                + "GROUP_CONCAT(m.nom SEPARATOR ', ') AS modulos "
                + "FROM professors p "
                + "LEFT JOIN modulprofessor mp ON p.id = mp.idprofessor " // Ajusta 'id' si es necesario
                + "LEFT JOIN moduls m ON mp.idmodul = m.id "       // Ajusta 'id' si es necesario
                     +"GROUP BY p.id, p.nom, p.telefon, p.correu";

        try (Connection conn = dbConnection.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Object[] row = {
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("telefon"),
                    rs.getString("correu"),
                    rs.getString("modulos") == null ? "" : rs.getString("modulos")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la información de los profesores y sus módulos.");
        }
        return model;
    }
    
    public DefaultListModel<String> obtenerProfesoresParaLista() {
        DefaultListModel<String> model = new DefaultListModel<>();
        String sql = "SELECT id, nom FROM professors"; // Asumo que la PK es 'id' y el nombre es 'nomCog'
        try (Connection conn = dbConnection.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String idProfesor = rs.getString("id");
                String nombreProfesor = rs.getString("nom");
                model.addElement(idProfesor + " - " + nombreProfesor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error al cargar la lista de profesores.");
        }
        return model;
    }
}
