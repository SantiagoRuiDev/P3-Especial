package models;

/**
 * Representa un camión de reparto leído desde un archivo CSV.
 */
public class Camion {
    private int idCamion;
    private String patente;
    private boolean estaRefrigerado;
    private int capacidadKg;

    /**
     * Crea un camión con sus datos básicos.
     *
     * @param idCamion identificador único del camión
     * @param patente patente del camión
     * @param estaRefrigerado indica si el camión es refrigerado
     * @param capacidadKg capacidad máxima de carga en kilogramos
     */
    public Camion(int idCamion, String patente, boolean estaRefrigerado, int capacidadKg) {
        this.idCamion = idCamion;
        this.patente = patente;
        this.estaRefrigerado = estaRefrigerado;
        this.capacidadKg = capacidadKg;
    }

    /**
     * Retorna el ID del camión.
     */
    public int getIdCamion() {
        return idCamion;
    }

    /**
     * Retorna la patente del camión.
     */
    public String getPatente() {
        return patente;
    }

    /**
     * Indica si el camión es refrigerado.
     */
    public boolean isEstaRefrigerado() {
        return estaRefrigerado;
    }

    /**
     * Retorna la capacidad máxima del camión en kilogramos.
     */
    public int getCapacidadKg() {
        return capacidadKg;
    }

    @Override
    public String toString() {
        return "Camion{" +
                "idCamion=" + idCamion +
                ", patente='" + patente + '\'' +
                ", estaRefrigerado=" + estaRefrigerado +
                ", capacidadKg=" + capacidadKg +
                '}';
    }
}