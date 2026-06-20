package utils;

import models.Paquete;

import java.util.*;

/**
 * Representa una estructura auxiliar para almacenar los resultados
 * de la ejecución del algoritmo de backtracking.
 */
public class ResultadoBacktracking {

    /**
     * Mapa que contiene la relación entre el ID del camión y la lista de paquetes asignados.
     */
    private HashMap<Integer, List<Paquete>> solucion;

    /**
     * Sumatoria del peso en kilogramos de los paquetes que no pudieron ser asignados.
     */
    private int pesoNoAsignadoKg;

    /**
     * Contador de la cantidad de estados (nodos) explorados en el árbol de recursión.
     */
    private long estadosGenerados;

    /**
     * Constructor para guardar los resultados del algoritmo de backtracking.
     *
     * @param solucion asignación de paquetes por camión.
     * @param pesoNoAsignadoKg peso sin asignar resultante de la mejor solución.
     * @param estadosGenerados cantidad de estados o nodos explorados en el árbol.
     */
    public ResultadoBacktracking(HashMap<Integer, List<Paquete>> solucion, int pesoNoAsignadoKg, long estadosGenerados) {
        this.solucion = solucion;
        this.pesoNoAsignadoKg = pesoNoAsignadoKg;
        this.estadosGenerados = estadosGenerados;
    }

    /**
     * Retorna el mapa de asignación de paquetes por camión de la solución óptima.
     * @return Mapa con la solución obtenida.
     */
    public HashMap<Integer, List<Paquete>> getSolucion() {
        return solucion;
    }

    /**
     * Retorna el peso acumulado de aquellos paquetes que no fueron asignados.
     * @return Peso en kilogramos no asignado.
     */
    public int getPesoNoAsignadoKg() {
        return pesoNoAsignadoKg;
    }

    /**
     * Retorna la cantidad de estados generados durante la búsqueda.
     * @return Cantidad de estados explorados.
     */
    public long getEstadosGenerados() {
        return estadosGenerados;
    }

    /**
     * Genera una representación en cadena de la solución y sus métricas.
     * @return String con el formato requerido por la consigna.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Solución obtenida:\n");

        if (solucion == null || solucion.isEmpty()) {
            sb.append("  No se asignaron paquetes a ningún camión.\n");
        } else {
            for (Map.Entry<Integer, List<Paquete>> entrada : solucion.entrySet()) {
                sb.append("  -> Camión ID [").append(entrada.getKey()).append("]: ");
                List<Paquete> paquetesAsignados = entrada.getValue();

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

        sb.append("\nMétricas:\n");
        sb.append("- Peso no asignado: ").append(pesoNoAsignadoKg).append(" kg.\n");
        sb.append("- Estados generados: ").append(estadosGenerados).append("\n");
        return sb.toString();
    }
}