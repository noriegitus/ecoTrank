package ecotrack.logica;

import java.io.Serializable;

public class Zona implements Serializable {
    String nombreZona;
    int p_Recolectado;
    int p_Pendiente;

    public Zona(String nombre){
        nombreZona = nombre;
        p_Recolectado = 0;
        p_Pendiente = 0;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public int getP_Recolectado() {
        return p_Recolectado;
    }
    
    // Getters para JavaFX PropertyValueFactory
    public int getPesoRecolectado() {
        return p_Recolectado;
    }

    public void registrarResiduoPendiente(int peso){
        if (peso < 0) {
            throw new IllegalArgumentException("El peso no puede ser negativo. Valor recibido: " + peso);
        }
        p_Pendiente += peso;
    }

    public int getP_Pendiente() {
        return p_Pendiente;
    }
    
    // Getter para JavaFX PropertyValueFactory
    public int getPesoPendiente() {
        return p_Pendiente;
    }
    
    // Getter para utilidad (para mostrar en tabla)
    public int getUtilidad() {
        return calcularUtilidad();
    }

    public void registrarResiduoRecolectado(int peso){
        if (peso < 0) {
            throw new IllegalArgumentException("El peso no puede ser negativo. Valor recibido: " + peso);
        }
        p_Pendiente = Math.max(0, p_Pendiente - peso); // Prevenir valores negativos
        p_Recolectado += peso;
    }

    @Override
    public String toString() {
        return "Zona [nombreZona=" + nombreZona + ", p_Recolectado=" + p_Recolectado + ", p_Pendiente=" + p_Pendiente
                + "]";
    }

    public int calcularUtilidad(){
        return p_Recolectado - p_Pendiente;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nombreZona == null) ? 0 : nombreZona.hashCode());
        result = prime * result + p_Recolectado;
        result = prime * result + p_Pendiente;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Zona other = (Zona) obj;
        if (nombreZona == null) {
            if (other.nombreZona != null)
                return false;
        } else if (!nombreZona.equals(other.nombreZona))
            return false;
        if (p_Recolectado != other.p_Recolectado)
            return false;
        if (p_Pendiente != other.p_Pendiente)
            return false;
        return true;
    }

    

}
