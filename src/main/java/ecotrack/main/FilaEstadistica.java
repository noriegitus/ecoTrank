package ecotrack.main;

public class FilaEstadistica {

    private String tipo;
    private double pesoTotal;

    public FilaEstadistica(String tipo, double pesoTotal) {
        this.tipo = tipo;
        this.pesoTotal = pesoTotal;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPesoTotal() {
        return pesoTotal;
    }
}

