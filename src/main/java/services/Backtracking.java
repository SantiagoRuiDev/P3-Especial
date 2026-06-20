package services;

import models.Camion;
import models.Paquete;
import utils.ResultadoBacktracking;

import java.util.*;

/**
 * Clase encargada de resolver la asignación de paquetes a camiones mediante el uso del algoritmo de Backtracking.
 * * <p><b>Estrategia Backtracking elegida:</b><br>
 * El algoritmo implementa una estrategia de exploración de un árbol de decisión, buscando como objetivo
 * principal hallar la solución óptima global (es decir, reducir la cantidad de paquetes sin asignar).
 * En cada recursión el algoritmo evalúa todas las ramas posibles donde el paquete puede ser asignado.
 * * <p>Para evitar que la complejidad computacional escale y se vuelva lento, tomamos algunas decisiones:
 * <ul>
 * <li><b>1. Ordenamiento:</b> Ordenar la totalidad de los paquetes de mayor a menor.</li>
 * <li><b>2. Poda con restricción:</b> Para los paquetes alimenticios en camiones no refrigerados.</li>
 * <li><b>3. Poda por peso:</b> Si una solución supera el mejor peso sin asignar, se descarta.</li>
 * </ul>
 * * <p>Al finalizar la exploración, el algoritmo asegura obtener la mejor solución con el menor
 * desperdicio (kg) posible.
 * <br>La complejidad se define en: O(p log p + c * (c+1)^p)
 */
public class Backtracking {

    /**
     * Lista completa de paquetes a procesar.
     */
    private List<Paquete> paquetes;

    /**
     * Lista de camiones disponibles para la asignación.
     */
    private List<Camion> camiones;

    /**
     * Estado interno para almacenar la mejor distribución de paquetes encontrada.
     */
    private HashMap<Integer, List<Paquete>> mejorSolucion;

    /**
     * Récord del menor peso sin asignar alcanzado durante la exploración.
     */
    private int mejorPesoNoAsignado;

    /**
     * Constructor de la clase Backtracking.
     *
     * @param paquetes lista de todos los paquetes a procesar.
     * @param camiones lista de todos los camiones disponibles.
     */
    public Backtracking(List<Paquete> paquetes, List<Camion> camiones) {
        this.paquetes = paquetes;
        this.camiones = camiones;
    }

    /**
     * Inicia la ejecución del algoritmo de Backtracking preparando las estructuras de datos y el estado inicial.
     *
     * @return un objeto ResultadoBacktracking con la solución óptima y las métricas de ejecución.
     */
    public ResultadoBacktracking backtracking() {
        // Inicialización de estado para la búsqueda
        this.mejorPesoNoAsignado = Integer.MAX_VALUE;
        this.mejorSolucion = new HashMap<>();

        // Ordena los paquetes de mayor a menor (todos los paquetes)
        paquetes.sort(Comparator.comparingInt(Paquete::getPesoKg).reversed());

        // Mapa que servira para construir la asignación de paquetes de manera recursiva.
        HashMap<Integer, List<Paquete>> solucionActual = new HashMap<>();
        // Mapa para controlar la capacidad de cada camión.
        HashMap<Integer, Integer> capacidad = new HashMap<>();

        long[] estados = {0};

        // O(c)
        for(Camion ck: camiones) {
            solucionActual.put(ck.getIdCamion(), new ArrayList<>());
            // La capacidad disponible se establece a partir del camión y se va reduciendo conforme se asignen paquetes.
            capacidad.put(ck.getIdCamion(), ck.getCapacidadKg());
        }

        backtracking(0, paquetes, solucionActual, capacidad, estados, 0);

        return new ResultadoBacktracking(this.mejorSolucion, this.mejorPesoNoAsignado, estados[0]);
    }

    /**
     * Método recursivo privado que explora el árbol de soluciones posibles (Sobrecarga).
     *
     * @param index          índice del paquete actual.
     * @param paquetes       lista de paquetes ordenados.
     * @param solucionActual mapa de distribución de paquetes actual.
     * @param capacidades    capacidad restante de los camiones.
     * @param estados        contador de nodos visitados.
     * @param pesoNoAsignado peso acumulado de paquetes no asignados.
     */
    private void backtracking(
            int index,
            List<Paquete> paquetes,
            HashMap<Integer, List<Paquete>> solucionActual,
            HashMap<Integer, Integer> capacidades,
            long[] estados,
            int pesoNoAsignado) {

        // Incrementar estados para la metrica.
        estados[0]++;

        // Si el peso sin asignar que tengo ahora mismo, es mayor al mejor significa que este caminó no es mejor que el que ya tenemos.
        if(pesoNoAsignado >= this.mejorPesoNoAsignado) {
            return;
        }

        // Si he alcanzado ya la mejor solución no sigo con el algoritmo.
        if(index == paquetes.size()) {
            this.mejorPesoNoAsignado = pesoNoAsignado;
            this.mejorSolucion = copiarMapa(solucionActual);
            return;
        }

        // Busco el paquete actual.
        Paquete pk = paquetes.get(index);

        // Para cada camion se intentara asignar el paquete.
        for(Camion ck: camiones) {
            if(pk.isContieneAlimentos() && !ck.isEstaRefrigerado()) {
                continue;
            }

            int capacidadRestante = capacidades.get(ck.getIdCamion());

            if(capacidadRestante >= pk.getPesoKg()) {
                // Actualizo el mapa que controla la capacidad restante
                capacidades.put(ck.getIdCamion(), capacidadRestante - pk.getPesoKg());
                // Busco la lista de paquetes vinculada al camion y agrego el paquete.
                solucionActual.get(ck.getIdCamion()).add(pk);

                // Realizo backtracking para el resto de paquetes.
                backtracking(index + 1, paquetes, solucionActual, capacidades, estados, pesoNoAsignado);

                // Deshago los cambios anteriormente hechos (Regresar)
                capacidades.put(ck.getIdCamion(), capacidadRestante);
                solucionActual.get(ck.getIdCamion()).removeLast();
            }
        }

        // Si no es asignado a ningun camión seguir el backtracking con el desperdicio sumado.
        backtracking(index + 1, paquetes, solucionActual, capacidades, estados, pesoNoAsignado + pk.getPesoKg());
    }

    /**
     * Realiza una copia profunda del mapa de asignación de paquetes para
     * evitar la sobreescritura de referencias durante la recursión.
     *
     * @param original el mapa de asignaciones en el estado actual de la rama.
     * @return un nuevo HashMap cuyas listas internas son independientes del mapa original.
     */
    private HashMap<Integer, List<Paquete>> copiarMapa(Map<Integer, List<Paquete>> original) {
        HashMap<Integer, List<Paquete>> copia = new HashMap<>();
        for (Map.Entry<Integer, List<Paquete>> entrada : original.entrySet()) {
            copia.put(entrada.getKey(), new ArrayList<>(entrada.getValue()));
        }
        return copia;
    }
}