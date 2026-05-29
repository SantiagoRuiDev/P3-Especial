package models;

/**
 * Representa un paquete leído desde un archivo CSV.
 */
public class Paquete {

    private int idPaquete;
    private String codigoPaquete;
    private int pesoKg;
    private boolean contieneAlimentos;
    private int nivelUrgencia;

    /**
     * Crea un paquete con sus datos básicos.
     *
     * @param idPaquete identificador único del paquete
     * @param codigoPaquete código identificador del paquete
     * @param pesoKg peso del paquete en kilogramos
     * @param contieneAlimentos indica si el paquete contiene alimentos
     * @param nivelUrgencia nivel de urgencia entre 1 y 100
     */
    public Paquete(int idPaquete,
                   String codigoPaquete,
                   int pesoKg,
                   boolean contieneAlimentos,
                   int nivelUrgencia) {

        this.idPaquete = idPaquete;
        this.codigoPaquete = codigoPaquete;
        this.pesoKg = pesoKg;
        this.contieneAlimentos = contieneAlimentos;
        this.nivelUrgencia = nivelUrgencia;
    }

    /**
     * Retorna el ID del paquete.
     */
    public int getIdPaquete() {
        return idPaquete;
    }

    /**
     * Retorna el código identificador del paquete.
     */
    public String getCodigoPaquete() {
        return codigoPaquete;
    }

    /**
     * Retorna el peso del paquete en kilogramos.
     */
    public int getPesoKg() {
        return pesoKg;
    }

    /**
     * Indica si el paquete contiene alimentos.
     */
    public boolean isContieneAlimentos() {
        return contieneAlimentos;
    }

    /**
     * Retorna el nivel de urgencia del paquete.
     */
    public int getNivelUrgencia() {
        return nivelUrgencia;
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "idPaquete=" + idPaquete +
                ", codigoPaquete='" + codigoPaquete + '\'' +
                ", pesoKg=" + pesoKg +
                ", contieneAlimentos=" + contieneAlimentos +
                ", nivelUrgencia=" + nivelUrgencia +
                '}';
    }
}