package models;

// Clase reservada para representar a los paquetes (Leidos desde el CSV)
public class Paquete {
    private String idPaquete;
    private String codigoPaquete;
    private int pesoKg;
    private boolean contieneAlimentos;
    private int nivelUrgencia;

    public Paquete(String idPaquete, String codigoPaquete, int pesoKg, boolean contieneAlimentos, int nivelUrgencia) {
        this.idPaquete = idPaquete;
        this.codigoPaquete = codigoPaquete;
        this.pesoKg = pesoKg;
        this.contieneAlimentos = contieneAlimentos;
        this.nivelUrgencia = nivelUrgencia;
    }

    public String getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(String idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getCodigoPaquete() {
        return codigoPaquete;
    }

    public void setCodigoPaquete(String codigoPaquete) {
        this.codigoPaquete = codigoPaquete;
    }

    public int getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(int pesoKg) {
        this.pesoKg = pesoKg;
    }

    public boolean isContieneAlimentos() {
        return contieneAlimentos;
    }

    public void setContieneAlimentos(boolean contieneAlimentos) {
        this.contieneAlimentos = contieneAlimentos;
    }

    public int getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(int nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }
}
