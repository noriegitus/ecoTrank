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
public class GrafoResiduo {
    private Residuo[] vectores;
    private int capacity = 100;
    private int effectiveSize = 0;
    private int[][] matrizAd;
    private boolean isDirected;
    
    public GrafoResiduo(boolean dir){
        vectores = new Residuo[capacity];
        matrizAd = new int[capacity][capacity];
        isDirected = dir;
        iniciarMatriz();
    }
    
    private void iniciarMatriz(){
        for(int i=0; i < matrizAd.length; i++){
            for(int j=0; j < matrizAd.length; j++){
                matrizAd[i][j] = -1;
            }
        }
    }
    
    private void actualizarCapacidad(){
        Residuo[] tempR = new Residuo[capacity * 2];
        int[][] tempM = new int[capacity*2][capacity*2];
        capacity = capacity * 2;
        iniciarMatriz();
    }
    
    public void agregarResiduo(Residuo r){
        if(effectiveSize == capacity){
            actualizarCapacidad();
        }
        
        if(effectiveSize < capacity){
            vectores[effectiveSize] = r;
            effectiveSize++;
        }
        
        System.out.println("Vector agregado.");
    }
    
    public int findVector(String id){
        if(id == null){
            return -1;
        }
        
        for(int i=0; i < effectiveSize; i++){
            if(vectores[i].getId().equals(id)){
                return i;
            }
        }
        
        return -1;
    }
    
    public void conectar(String id1, String id2){
        if(id1 == null || id2 == null){
            return;
        }
        
        int vector1 = findVector(id1);
        int vector2 = findVector(id2);
        
        if(vector1 != -1 && vector2 != -1){
            matrizAd[vector1][vector2] = 1;
            if(!isDirected){
                matrizAd[vector2][vector1] = 1;
            }
            System.out.println("Vectores conectados");
        } else {
            System.out.println("Uno de los residuos no existe.");
        }
    }
    
    public void mostrarMatriz(){
        for(int i=0; i < effectiveSize; i++){
            System.out.println(vectores[i] + " ");
            for(int j=0; j < effectiveSize; j++){
                System.out.println("[" + vectores[i] + " " + vectores[j] + "]");
                System.out.println("Conexion: " + matrizAd[i][j] + " ");
            }
        }
    }

    public Residuo[] getVectores() {
        return vectores;
    }

    public int[][] getMatrizAd() {
        return matrizAd;
    }

    public int getEffectiveSize() {
        return effectiveSize;
    }
    
    
}
