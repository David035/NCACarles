/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package idearpartegrafica; // Assegura't que coincideix amb el teu package

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TutorDAO {

    public boolean existeixTutor(String idTutor) {
        String sql = "SELECT COUNT(*) FROM tutors WHERE id = ?";
        try (Connection conn = dbConnection.getConexion(); // Utilitza la teva classe de connexió
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idTutor);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si existeix un tutor amb l'ID donat
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar si existeix el tutor: " + e.getMessage());
        }
        return false;
    }

    public boolean insertarTutor(String idTutor, String nomCognoms, String telefon, String correuElectronic, String relacio) {
        String sql = "INSERT INTO tutors (id, nom, telefon, correu, relacio) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConexion(); // Utilitza la teva classe de connexió
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idTutor);
            pstmt.setString(2, nomCognoms);
            pstmt.setString(3, telefon);
            pstmt.setString(4, correuElectronic);
            pstmt.setString(5, relacio);
            int filesAfectades = pstmt.executeUpdate();
            return filesAfectades > 0; // Retorna true si la inserció ha tingut èxit
        } catch (SQLException e) {
            System.err.println("Error al insertar el tutor: " + e.getMessage());
            return false;
        }
    }

    // Podries tenir altres mètodes per obtenir, actualitzar, eliminar tutors, etc.
}
