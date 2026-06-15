package utils;

import models.Paquete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa un estructura auxiliar para almacenar los resultados de la ejecución del algoritmo greedy
 */
public class ResultadoGreedy {
    private HashMap<Integer, List<Paquete>> solucion;
    private int pesoNoAsignadoKg;
    private int candidatosConsiderados;

    /**
     * Guarda los resultados del algoritmo greedy
     *
     * @param solucion asignación de paquetes por camión
     * @param pesoNoAsignadoKg peso sin asignar resultante
     * @param candidatosConsiderados cantidad de candidatos considerados
     */
    public ResultadoGreedy(HashMap<Integer, List<Paquete>> solucion, int pesoNoAsignadoKg, int candidatosConsiderados) {
        this.solucion = solucion;
        this.pesoNoAsignadoKg = pesoNoAsignadoKg;
        this.candidatosConsiderados = candidatosConsiderados;
    }

    /**
     * Retorna el mapa de asignación de paquetes por camión.
     */
    public HashMap<Integer, List<Paquete>> getSolucion() { return solucion; }

    /**
     * Retorna el peso acumulado de aquellos paquetes que no fueron asignados.
     */
    public int getPesoNoAsignadoKg() { return pesoNoAsignadoKg; }

    /**
     * Retorna la cantidad de candidatos considerados durante la asignación.
     */
    public int getCandidatosConsiderados() { return candidatosConsiderados; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Solución obtenida:\n");

        // Recorremos el mapa de la solución para listar cada camión y sus paquetes
        if (solucion == null || solucion.isEmpty()) {
            sb.append("  No se asignaron paquetes a ningún camión.\n");
        } else {
            for (Map.Entry<Integer, List<Paquete>> entrada : solucion.entrySet()) {
                Integer idCamion = entrada.getKey();
                List<Paquete> paquetesAsignados = entrada.getValue();

                sb.append("  -> Camión ID [").append(idCamion).append("]: ");

                if (paquetesAsignados.isEmpty()) {
                    sb.append("Sin paquetes asignados.\n");
                } else {
                    sb.append(paquetesAsignados.size()).append(" paquetes asignados.\n");
                    for (Paquete p : paquetesAsignados) {
                        sb.append("     * Paquete: ").append(p.getCodigoPaquete())
                                .append(" (").append(p.getPesoKg()).append(" kg)")
                                .append(p.isContieneAlimentos() ? " [Alimento]" : "")
                                .append("\n");
                    }
                }
            }
        }

        sb.append("\nMetricas:\n");
        sb.append("- Peso no asignado: ").append(pesoNoAsignadoKg).append(" kg.\n");
        sb.append("- Candidatos considerados: ").append(candidatosConsiderados).append("\n");
        return sb.toString();
    }
}
