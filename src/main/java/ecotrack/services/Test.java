/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack.services;
import ecotrack.logica.Residuo;

/**
 *
 * @author User
 */
public class Test {
    
    public static void main(String[] args){
        CentroReciclaje pila = new CentroReciclaje();
        
        pila.recibirResiduo(new Residuo("B1", "Botella", "Plástico"));
        pila.recibirResiduo(new Residuo("V1", "Vaso", "Plástico"));
        pila.recibirResiduo(new Residuo("J1", "Juguete", "Plástico"));
        
        GrafoResiduo grafo = SistemaEcoTrack.crearGrafoTipo(pila);
        grafo.mostrarMatriz();
    }
}
