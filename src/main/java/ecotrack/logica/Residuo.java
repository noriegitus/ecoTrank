/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack.logica;

 
 // @author Acosta Allan
 

import java.time.LocalDate;

public class Residuo {
    private String id; 
    private String nombre;
    private String tipo;
    private double peso;
    private LocalDate fechaRecoleccion;
    private String zona;
    private int prioridad;

    public Residuo(String id, String nombre, String tipo, double peso, String zona, int prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.fechaRecoleccion = LocalDate.now(); // Asigna fecha actual por defecto
        this.zona = zona;
        this.prioridad = prioridad;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setFechaRecoleccion(LocalDate fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public double getPeso() { return peso; }
    public int getPrioridad() { return prioridad; }

    @Override
    public String toString() {
        return "Residuo{ID='" + id + "', Tipo='" + tipo + "', Zona='" + zona + "', Prioridad=" + prioridad + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Residuo residuo = (Residuo) obj;
        return id.equals(residuo.id);
    }
}

