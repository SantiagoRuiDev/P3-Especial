import models.Camion;
import models.Paquete;
import utils.MejorResultado;
import utils.ResultadoBacktracking;

import java.util.*;

public class Backtracking {
    private List<Paquete> paquetes;
    private List<Camion> camiones;

    public Backtracking(List<Paquete> paquetes, List<Camion> camiones) {
        this.paquetes = paquetes;
        this.camiones = camiones;
    }

    // Estrategia Backtracking elegida
    // El algoritmo implementa una estrategia de exploración de un arbol de decisión
    // Buscando como objetivo principal hallar la solución optima global (Es decir reducir la cantidad de paquetes sin asignar)
    // En cada recursión el algoritmo evalúa todas la ramas posibles donde el paquete puede ser asignado.
    //
    // Para evitar la complejidad computacional escale y se vuelva lento tomamos algunas decisiones.
    // 1. Ordenar la totalidad de los paquetes de mayor a menor, para asegurar que llenaremos los camiones de manera rapida,
    //    y nuestro record de peso sin asignar sea lo mas bajo posible.
    // 2. Poda con restricción para los paquetes alimenticios, es decir si estoy intentando asignar un paquete alimenticio
    //    a un camión que no esta refrigerado, puedo evitar esa rama y asi no realizar esa recursión.
    // 3. Poda por peso si una solución alcanza o supera el mejor peso sin asignar se deduce que esa solución no es optima.
    //
    // Al finalizar la exploración y deshacer los cambios el algoritmo asegura obtener la mejor solución con el menor despericio (kg) posible.
    // La complejidad de este metodo podemos definirla en: O(p log p + c * (c+1)^p)
    public ResultadoBacktracking backtracking () {
        // Ordena los paquetes de mayor a menor (todos los paquetes)
        paquetes.sort(Comparator.comparingInt(Paquete::getPesoKg).reversed());

        // Mapa que servira para construir la asignación de paquetes de manera recursiva.
        HashMap<Integer, List<Paquete>> solucionActual = new HashMap<>();
        // Mapa para controlar la capacidad de cada camión.
        HashMap<Integer, Integer> capacidad = new HashMap<>();

        // Clase auxiliar para guardar la información final. (Metricas y solución optima)
        MejorResultado mejor = new MejorResultado();
        int[] estados = {0};

        // O(c)
        for(Camion ck: camiones) {
            solucionActual.put(ck.getIdCamion(), new ArrayList<>());
            // La capacidad disponible se establece a partir del camión y se va reduciendo conforme se asignen paquetes.
            capacidad.put(ck.getIdCamion(), ck.getCapacidadKg());
        }

        backtracking(0, paquetes, solucionActual, capacidad, mejor, estados, 0);

        return new ResultadoBacktracking(mejor.getSolucion(), mejor.getPesoNoAsignadoKg(), estados[0]);
    }

    // Complejidad computacional: O((c+1)^p)
    // Esta complejidad es el peor escenario, si contamos con las podas y las optimizaciones no deberia llegar a este escenario.
    private void backtracking (
            int index,
            List<Paquete> paquetes,
            HashMap<Integer, List<Paquete>> solucionActual,
            HashMap<Integer, Integer> capacidades,
            MejorResultado mejor,
            int[] estados,
            int pesoNoAsignado){
        // Incrementar estados para la metrica.
        estados[0]++;

        // Si el peso sin asignar que tengo ahora mismo, es mayor al mejor significa que este caminó no es mejor que el que ya tenemos.
        if(pesoNoAsignado >= mejor.getPesoNoAsignadoKg()) {
            return;
        }

        // Si he alcanzado ya la mejor solución no sigo con el algoritmo.
        if(index == paquetes.size()) {
            mejor.setPesoNoAsignadoKg(pesoNoAsignado);
            mejor.setSolucion(copiarMapa(solucionActual));
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
                backtracking(index + 1, paquetes, solucionActual, capacidades, mejor, estados, pesoNoAsignado);

                // Deshago los cambios anteriormente hechos (Regresar)
                capacidades.put(ck.getIdCamion(), capacidadRestante);
                solucionActual.get(ck.getIdCamion()).removeLast();
            }
        }

        // Si no es asignado a ningun camión seguir el backtracking con el desperdicio sumado.
        backtracking(index + 1, paquetes, solucionActual, capacidades, mejor, estados, pesoNoAsignado + pk.getPesoKg());
    }

    // metodo para copiar mapa.
    private HashMap<Integer, List<Paquete>> copiarMapa (Map<Integer, List<Paquete>> original) {
        HashMap<Integer, List<Paquete>> copia = new HashMap<>();
        for(Map.Entry<Integer, List<Paquete>> entrada: original.entrySet()) {
            copia.put(entrada.getKey(), new ArrayList<>(entrada.getValue()));
        }
        return copia;
    }
}
