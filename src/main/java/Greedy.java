import models.Camion;
import models.Paquete;
import utils.ResultadoGreedy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Greedy {
    private List<Paquete> paquetesConAlimentos;
    private List<Paquete> paquetesSinAlimentos;
    private List<Camion> camiones;

    public Greedy(List<Paquete> paquetesConAlimentos, List<Paquete> paquetesSinAlimentos, List<Camion> camiones) {
        this.paquetesConAlimentos = paquetesConAlimentos;
        this.paquetesSinAlimentos = paquetesSinAlimentos;
        this.camiones = camiones;
    }

    // Estrategia Greedy elegida
    // El algoritmo implementa una estrategia pensada en optimizar el espacio disponible
    // Es decir, organizar las listas de paquetes (refrigerados/no refrig) de mayor a menor por peso.
    // Con la premisa de que los paquetes mas pesados seran ubicados, debido a la disponibilidad de espacio en los candidatos.
    // Eso asegura su lugar al principio en la asignación.
    //
    // Tambien se realiza una restricción por categorias.
    // 1. Se procesa primero a los paquetes con alimentos, ya que solo pueden ser asignados a camiones refrigerados.
    // 2. Se procesa el resto de paquetes que puede ser asignados en todos los candidatos.
    //
    // En cada paso el algoritmo toma la decisión de asignar el paquete mas pesado al primer camión,
    // Con capacidad disponible que encuentre, con la esperanza de encontrar la solución optima de manera global.
    // La complejidad de este metodo es O(p log p + p * c), donde:
    //     * p representa la cantidad total de paquetes (tanto con alimentos como sin alimentos)
    //     * c representa la cantidad total de camiones disponibles.
    public ResultadoGreedy greedy () {
        HashMap<Integer, List<Paquete>> asignacion = new HashMap<>();
        HashMap<Integer, Integer> controlCapacidad = new HashMap<>();
        int pesoNoAsignado = 0;
        int candidatos = 0;

        // O(c)
        for(Camion ck: this.camiones) {
            // Establecemos en el mapa el camión y el listado de sus paquetes asignados.
            asignacion.put(ck.getIdCamion(), new ArrayList<>());
            // La capacidad disponible se establece a partir del camión y se va reduciendo conforme se asignen paquetes.
            controlCapacidad.put(ck.getIdCamion(), ck.getCapacidadKg());
        }

        // O(PLogP)
        this.paquetesConAlimentos.sort(Comparator.comparingInt(Paquete::getPesoKg).reversed());
        this.paquetesSinAlimentos.sort(Comparator.comparingInt(Paquete::getPesoKg).reversed());

        // O(c)
        List<Camion> refrigerados = this.camiones.stream().filter(Camion::isEstaRefrigerado).toList();

        // O(P*Cr)
        // Los paquetes alimenticios solo pueden ser transportados en camiones refrigerados.
        for(Paquete paqueteAlimenticio: this.paquetesConAlimentos) {
            boolean asignado = false;
            for(Camion camionRefrigerado: refrigerados) {
                candidatos++; // Aqui se considera un candidato para la posible asignación
                int capRestante = controlCapacidad.get(camionRefrigerado.getIdCamion());
                // Comprobamos si hay capacidad en este camión.
                if(paqueteAlimenticio.getPesoKg() <= capRestante) {
                    asignacion.get(camionRefrigerado.getIdCamion()).add(paqueteAlimenticio);
                    controlCapacidad.put(camionRefrigerado.getIdCamion(), capRestante - paqueteAlimenticio.getPesoKg());
                    // Corta la ejecución y sigue al siguiente paquete.
                    asignado = true;
                    break;
                }
            }
            // Cuando se termina de considerar los candidatos, si no se encuentra uno, se suma al peso sin asignar.
            if(!asignado) {
                pesoNoAsignado += paqueteAlimenticio.getPesoKg();
            }
        }

        // O(P*C)
        // Los paquetes sin alimentos pueden ser transladados en cualquier camión
        for(Paquete paquete: this.paquetesSinAlimentos) {
            boolean asignado = false;
            for(Camion camion: this.camiones) {
                candidatos++; // Aqui se considera un candidato para la posible asignación
                int capRestante = controlCapacidad.get(camion.getIdCamion());
                // Comprobamos si hay capacidad en este camión.
                if(paquete.getPesoKg() <= capRestante) {
                    asignacion.get(camion.getIdCamion()).add(paquete);
                    controlCapacidad.put(camion.getIdCamion(), capRestante - paquete.getPesoKg());
                    // Corta la ejecución y sigue al siguiente paquete.
                    asignado = true;
                    break;
                }
            }
            // Cuando se termina de considerar los candidatos, si no se encuentra uno, se suma al peso sin asignar.
            if(!asignado) {
                pesoNoAsignado += paquete.getPesoKg();
            }
        }

        return new ResultadoGreedy(asignacion, pesoNoAsignado, candidatos);
    }
}
