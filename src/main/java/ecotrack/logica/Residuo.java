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

    public Residuo(String id, String nombre, String tipo, double peso, String zona, int prioridad) {
        // Validaciones de campos obligatorios
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del residuo no puede ser null o vacío");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del residuo no puede ser null o vacío");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo del residuo no puede ser null o vacío");
        }
        if (zona == null || zona.trim().isEmpty()) {
            throw new IllegalArgumentException("La zona del residuo no puede ser null o vacía");
        }
        if (peso < 0) {
            throw new IllegalArgumentException("El peso del residuo no puede ser negativo. Valor recibido: " + peso);
        }
        if (prioridad < 0) {
            throw new IllegalArgumentException("La prioridad del residuo no puede ser negativa. Valor recibido: " + prioridad);
        }
        
        this.id = id.trim();
        this.nombre = nombre.trim();
        this.tipo = tipo.trim();
        this.peso = peso;
        this.fechaRecoleccion = LocalDate.now(); // Asigna fecha actual por defecto
        this.zona = zona.trim();
        this.prioridad = prioridad;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del residuo no puede ser null o vacío");
        }
        this.id = id.trim();
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del residuo no puede ser null o vacío");
        }
        this.nombre = nombre.trim();
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo del residuo no puede ser null o vacío");
        }
        this.tipo = tipo.trim();
    }

    public void setPeso(double peso) {
        if (peso < 0) {
            throw new IllegalArgumentException("El peso del residuo no puede ser negativo. Valor recibido: " + peso);
        }
        this.peso = peso;
    }

    public void setFechaRecoleccion(LocalDate fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public void setZona(String zona) {
        if (zona == null || zona.trim().isEmpty()) {
            throw new IllegalArgumentException("La zona del residuo no puede ser null o vacía");
        }
        this.zona = zona.trim();
    }

    public void setPrioridad(int prioridad) {
        if (prioridad < 0) {
            throw new IllegalArgumentException("La prioridad del residuo no puede ser negativa. Valor recibido: " + prioridad);
        }
        this.prioridad = prioridad;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public double getPeso() { return peso; }
    public int getPrioridad() { return prioridad; }
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

