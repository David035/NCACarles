/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package idearpartegrafica; // Assegura't que coincideix amb el teu package

public class Fill {
    private int idFill;
    private String nomCognoms;

    // Constructor buit (opcional, però útil)
    public Fill() {
    }

    // Constructor amb arguments (opcional, per a la creació ràpida d'objectes)
    public Fill(int idFill, String nomCognoms) {
        this.idFill = idFill;
        this.nomCognoms = nomCognoms;
    }

    // Getters per als atributs
    public int getIdFill() {
        return idFill;
    }

    public String getNomCognoms() {
        return nomCognoms;
    }

    // Setters per als atributs (si necessites modificar els valors després de la creació)
    public void setIdFill(int idFill) {
        this.idFill = idFill;
    }

    public void setNomCognoms(String nomCognoms) {
        this.nomCognoms = nomCognoms;
    }

    // Mètode toString() (opcional, però útil per a la depuració i la visualització)
    @Override
    public String toString() {
        return "Fill{" +
               "idFill=" + idFill +
               ", nomCognoms='" + nomCognoms + '\'' +
               '}';
    }
}