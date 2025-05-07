/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package idearpartegrafica; // Assegura't que coincideix amb el teu package

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FillDAO {

    public List<Fill> obtenirTotsElsFills() {
        List<Fill> llistaFills = new ArrayList<>();
        String sql = "SELECT id, nom, cicle_formatiu FROM alumnes"; // Adapta la consulta a la teva taula

        try (Connection conn = dbConnection.getConexion(); // Utilitza la teva classe de connexió
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Fill fill = new Fill();
                fill.setIdFill(rs.getInt("id"));
                fill.setNomCognoms(rs.getString("nom"));
                llistaFills.add(fill);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtenir els fills: " + e.getMessage());
        }
        return llistaFills;
    }

    // Podries tenir altres mètodes per inserir, actualitzar, eliminar fills, etc.
}