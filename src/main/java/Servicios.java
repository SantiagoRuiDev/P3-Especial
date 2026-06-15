import models.*;

import utils.CSVReader;
import utils.ResultadoGreedy;

import java.util.*;

public class Servicios {

    private List<Camion> camiones = new ArrayList<>();
    /**
     * Estructura para el servicio 1
     * - ¿Por qué?: Durante el uso del for, cargo los paquetes en un mapa aprovechando su codigo unico.
     * - Alternativa: La mejor opción que encontramos en este caso es HashMap para realizar una busqueda
     * mas rapida. Sin logica adicional.
     */
    private HashMap<String, Paquete> mapPaquetes = new HashMap<>();

    /**
     * Estructuras para el servicio 2.
     * - ¿Por qué?: Aprovecho el uso de un for en el constructor para definir que paquetes leidos.
     * Seran enviados a cada una de las listas.
     * - Alternativa: Otra opción es usar un HashMap<boolean, List<Paquete>> la complejidad es la misma.
     */
    private List<Paquete> paquetesConAlimentos = new ArrayList<>();
    private List<Paquete> paquetesSinAlimentos = new ArrayList<>();

    /**
     * Estructura para el servicio 3.
     * - ¿Por qué?: TreeMap mantiene las claves ordenadas permitiendo búsquedas eficientes
     *              por rango de urgencia.
     * - Alternativa: Recorrer toda la lista de paquetes tendría una complejidad O(n).
     */
    private TreeMap<Integer, List<Paquete>> paquetesPorUrgencia = new TreeMap<>();

    /**
     * Constructor encargado de inicializar las estructuras necesarias para los servicios.
     *
     * Expresar la complejidad temporal del constructor.
     * La complejidad del constructor sera de O(n) debido a que depende de las cantidad de registros que vayamos a leer e insertar (CSV)
     *
     * @param pathCamiones ruta del archivo CSV de camiones
     * @param pathPaquetes ruta del archivo CSV de paquetes
     */
    public Servicios(String pathCamiones, String pathPaquetes)
    {
        // La complejidad de leer el archivo y generar las listas es de O(n) donde n es el numero de registros del CSV.
        this.camiones = CSVReader.obtenerInformacionCamiones(pathCamiones);
        List<Paquete> paquetes = CSVReader.obtenerInformacionPaquetes(pathPaquetes);

        if(!paquetes.isEmpty()){
            // La complejidad de recorrer la lista de paquetes se expresa como O(n)
            for(Paquete pk : paquetes) {
                mapPaquetes.put(pk.getCodigoPaquete(), pk);

                if(pk.isContieneAlimentos()){
                    paquetesConAlimentos.add(pk);
                } else {
                    paquetesSinAlimentos.add(pk);
                }

                paquetesPorUrgencia.putIfAbsent(pk.getNivelUrgencia(), new ArrayList<>());
                paquetesPorUrgencia.get(pk.getNivelUrgencia()).add(pk);
            }
        }
    }

    /**
     * Retorna un paquete a partir de su código identificador.
     *
     * Expresar la complejidad temporal del servicio 1.
     * Acceder a este servicio tiene una complejidad de O(1)
     *
     * @param codigoPaquete código único del paquete
     * @return el paquete asociado al código o null si no existe
     */
    public Paquete servicio1(String codigoPaquete) {
        return mapPaquetes.get(codigoPaquete);
    }

    /**
     * Retorna una lista de paquetes según si contienen alimentos o no.
     *
     * Expresar la complejidad temporal del servicio 2.
     * Acceder a este servicio tiene una complejidad de O(1)
     *
     * @param contieneAlimentos indica si se buscan paquetes con alimentos
     * @return lista correspondiente de paquetes
     */
    public List<Paquete> servicio2(boolean contieneAlimentos) {
        return contieneAlimentos ? paquetesConAlimentos : paquetesSinAlimentos;
    }

    /**
     * Retorna los paquetes cuyo nivel de urgencia se encuentre dentro del rango indicado.
     *
     * Expresar la complejidad temporal del servicio 3.
     * La complejidad de este servicio es O(log n + k), donde:
     *      n representa la cantidad de niveles de urgencia registrados
     *      k representa la cantidad de paquetes retornados.
     *
     * @param urgenciaMinima nivel mínimo de urgencia
     * @param urgenciaMaxima nivel máximo de urgencia
     * @return lista de paquetes dentro del rango de urgencia
     */
    public List<Paquete> servicio3(int urgenciaMinima, int
            urgenciaMaxima) {
        List<Paquete> resultado = new ArrayList<>();

        NavigableMap<Integer, List<Paquete>> rango =
                paquetesPorUrgencia.subMap(
                        urgenciaMinima,
                        true,
                        urgenciaMaxima,
                        true
                );

        for(List<Paquete> lista : rango.values()) {
            resultado.addAll(lista);
        }

        return resultado;
    }

    /**
     * Realiza una asignación eficiente (greedy) de paquetes a camiones priorizando
     * el peso de los paquetes y respetando las restricciones de refrigeración.
     *
     * La complejidad de este servicio es O(p log p + p * c), donde:
     * p representa la cantidad total de paquetes (tanto con alimentos como sin alimentos)
     * c representa la cantidad total de camiones disponibles.
     *
     * @return un objeto ResultadoGreedy con la solución óptima, el peso no asignado y el conteo de candidatos
     */
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