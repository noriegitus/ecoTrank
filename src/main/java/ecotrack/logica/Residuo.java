package ecotrack.logica;

import java.io.Serializable;
import java.time.LocalDate;

public class Residuo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id; 
    private String nombre;
    private String tipo;
    private double peso;
    private LocalDate fechaRecoleccion;
    private String zona;
    private int prioridad;

    public Residuo(String id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }
    
    
   
    public Residuo(String id, String nombre, String tipo, double peso, String zona, int prioridad) {
    validarCampos(id, nombre, tipo, zona, peso, prioridad);
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.zona = zona;
        this.prioridad = prioridad;
        this.fechaRecoleccion = LocalDate.now();
    }

    // Método privado para evitar repetir las mismas validaciones en constructor y setters
    private void validarCampos(String id, String nom, String tip, String zon, double p, int prio) {
        if (id == null || nom == null || tip == null || zon == null) 
            throw new IllegalArgumentException("Ningún campo puede ser nulo.");
        if (p < 0 || prio < 0) 
            throw new IllegalArgumentException("Peso y prioridad deben ser valores positivos.");
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
        if (peso < 0) {
            throw new IllegalArgumentException("Peso inválido: " + peso);
        }
        this.peso = peso;
    }

    public void setFechaRecoleccion(LocalDate fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public void setPrioridad(int prioridad) {
        if (prioridad < 0) {
            throw new IllegalArgumentException("Prioridad incorrecta:" + prioridad);
        }
        this.prioridad = prioridad;
    }

    // Getters
    public String getId() { 
        return id; 
    }
    public String getNombre() { 
        return nombre; 
    }
    public String getTipo() { 
        return tipo; 
    }
    public double getPeso() { 
        return peso; 
    }
    public int getPrioridad() { 
        return prioridad; 
    }
    public String getZona() {return zona;}

    @Override
    public String toString() {
        return "Residuo{ID='" + id + "', Tipo='" + tipo + "', Zona='" + zona + "', Prioridad=" + prioridad + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Residuo residuo = (Residuo) obj;
        if (id == null) {
            return residuo.id == null;
        }
        return id.equals(residuo.id);
    }
}

