package utils;

import models.Paquete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa un estructura auxiliar para almacenar los resultados de la ejecución del algoritmo backtracking
 */
public class ResultadoBacktracking {
    private HashMap<Integer, List<Paquete>> solucion;
    private int pesoNoAsignadoKg;
    private long estadosEvaluados;

    public ResultadoBacktracking(HashMap<Integer, List<Paquete>> solucion, int pesoNoAsignadoKg, long estadosEvaluados) {
        this.solucion = solucion;
        this.pesoNoAsignadoKg = pesoNoAsignadoKg;
        this.estadosEvaluados = estadosEvaluados;
    }

    /**
     * Retorna la solución final de la asignación de paquetes luego de la ejecución del algoritmo
     */
    public HashMap<Integer, List<Paquete>> getSolucion() { return solucion; }

    /**
     * Retorna el peso no asignado durante la ejecución
     */
    public int getPesoNoAsignadoKg() { return pesoNoAsignadoKg; }

    /**
     * Retorna la cantidad de estados evaluados durante la ejecución
     */
    public long getEstadosEvaluados() { return estadosEvaluados; }

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
        sb.append("- Estados evaluados: ").append(estadosEvaluados).append("\n");
        return sb.toString();
    }
}