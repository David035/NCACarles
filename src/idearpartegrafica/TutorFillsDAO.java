/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package idearpartegrafica; // Assegura't que coincideix amb el teu package

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TutorFillsDAO {

    public boolean associarTutorFill(String idTutor, int idFill) {
        String sql = "INSERT INTO alumnetutor (id_tutor, id_alumne) VALUES (?, ?)";
        try (Connection conn = dbConnection.getConexion(); // Utilitza la teva classe de connexió
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idTutor);
            pstmt.setInt(2, idFill);
            int filesAfectades = pstmt.executeUpdate();
            return filesAfectades > 0; // Retorna true si l'associació ha tingut èxit
        } catch (SQLException e) {
            System.err.println("Error al associar el tutor amb el fill: " + e.getMessage());
            return false;
        }
    }

    // Podries tenir altres mètodes per desassociar tutors i fills,
    // obtenir els fills d'un tutor, o els tutors d'un fill, etc.

    public boolean desassociarTutorFill(String idTutor, int idFill) {
        String sql = "DELETE FROM alumnetutor WHERE id_tutor = ? AND id_alumne = ?";
        try (Connection conn = dbConnection.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idTutor);
            pstmt.setInt(2, idFill);
            int filesAfectades = pstmt.executeUpdate();
            return filesAfectades > 0; // Retorna true si la desassociació ha tingut èxit
        } catch (SQLException e) {
            System.err.println("Error al desassociar el tutor del fill: " + e.getMessage());
            return false;
        }
    }

    // Exemple de mètode per obtenir els IDs dels fills associats a un tutor
    // public List<Integer> obtenirFillsDeTutor(String idTutor) { ... }

    // Exemple de mètode per obtenir els IDs dels tutors associats a un fill
    // public List<String> obtenirTutorsDeFill(int idFill) { ... }
}
