/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotank;

/**
 *
 * @author nori
 */
public class Zona {
    String nombreZona;
    int p_Recolectado;
    int p_Pendiente;

    public Zona(String nombre, int pendiente){
        nombreZona = nombre;
        p_Recolectado = 0;
        p_Pendiente = pendiente;
    }

    public int calcularUtilidad(){
        return p_Recolectado - p_Pendiente;
    }
}
