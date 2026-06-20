package services;

import models.Camion;
import models.Paquete;
import utils.CSVReader;

import java.util.*;

/**
 * Clase principal de servicios encargada de la lectura de datos, inicialización de estructuras
 * y provisión de métodos de consulta (búsquedas y filtrados) sobre los camiones y paquetes.
 * Actúa como el proveedor central de datos para los algoritmos de asignación (Greedy y Backtracking).
 */
public class Servicios {

    /**
     * Lista que almacena la totalidad de los camiones leídos desde el archivo CSV.
     */
    private List<Camion> camiones = new ArrayList<>();

    /**
     * Estructura para el Servicio 1.
     * <p>
     * - <b>¿Por qué?:</b> Durante la lectura, cargo los paquetes en un mapa aprovechando su código único.
     * - <b>Alternativa:</b> La mejor opción encontrada para este caso es un {@code HashMap} para realizar
     * una búsqueda más rápida de orden O(1), sin lógica adicional.
     */
    private HashMap<String, Paquete> mapPaquetes = new HashMap<>();

    /**
     * Estructura para el Servicio 2 (Paquetes con alimentos).
     * <p>
     * - <b>¿Por qué?:</b> Aprovecho el bucle del constructor para definir qué paquetes leídos serán enviados a cada lista.
     * - <b>Alternativa:</b> Otra opción es usar un {@code HashMap<Boolean, List<Paquete>>}; la complejidad espacial y temporal es la misma.
     */
    private List<Paquete> paquetesConAlimentos = new ArrayList<>();

    /**
     * Estructura para el Servicio 2 (Paquetes sin alimentos).
     */
    private List<Paquete> paquetesSinAlimentos = new ArrayList<>();

    /**
     * Estructura que guarda todos los paquetes leídos en el orden en que fueron procesados, sin ordenamiento especial.
     */
    private List<Paquete> paquetes = new ArrayList<>();

    /**
     * Estructura para el Servicio 3.
     * <p>
     * - <b>¿Por qué?:</b> {@code TreeMap} mantiene las claves ordenadas (árbol Rojo-Negro), permitiendo
     * búsquedas eficientes por rango de urgencia.
     * - <b>Alternativa:</b> Recorrer toda la lista de paquetes para filtrar tendría una complejidad de O(n) por cada consulta.
     */
    private TreeMap<Integer, List<Paquete>> paquetesPorUrgencia = new TreeMap<>();

    /**
     * Constructor encargado de inicializar las estructuras necesarias para los servicios.
     * <p>
     * <b>Complejidad temporal:</b> O(n), debido a que depende de la cantidad de registros que
     * vayamos a leer e insertar desde el archivo CSV, donde <i>n</i> es el número de paquetes.
     *
     * @param pathCamiones ruta del archivo CSV de camiones.
     * @param pathPaquetes ruta del archivo CSV de paquetes.
     */
    public Servicios(String pathCamiones, String pathPaquetes) {
        this.camiones = CSVReader.obtenerInformacionCamiones(pathCamiones);
        List<Paquete> paquetesLeidos = CSVReader.obtenerInformacionPaquetes(pathPaquetes);

        if (!paquetesLeidos.isEmpty()) {
            for (Paquete pk : paquetesLeidos) {
                mapPaquetes.put(pk.getCodigoPaquete(), pk);

                if (pk.isContieneAlimentos()) {
                    paquetesConAlimentos.add(pk);
                } else {
                    paquetesSinAlimentos.add(pk);
                }

                paquetesPorUrgencia.putIfAbsent(pk.getNivelUrgencia(), new ArrayList<>());
                paquetesPorUrgencia.get(pk.getNivelUrgencia()).add(pk);
            }
            this.paquetes.addAll(paquetesLeidos);
        }
    }

    /**
     * Retorna una copia de la lista total de camiones disponibles.
     * @return Lista de camiones.
     */
    public List<Camion> getCamiones() {
        return new ArrayList<>(this.camiones);
    }

    /**
     * Retorna una copia de la lista de paquetes que no contienen alimentos.
     * @return Lista de paquetes sin alimentos.
     */
    public List<Paquete> getPaquetesSinAlimentos() {
        return new ArrayList<>(this.paquetesSinAlimentos);
    }

    /**
     * Retorna una copia de la lista de paquetes que contienen alimentos.
     * @return Lista de paquetes con alimentos.
     */
    public List<Paquete> getPaquetesConAlimentos() {
        return new ArrayList<>(this.paquetesConAlimentos);
    }

    /**
     * Retorna una copia de la lista total de paquetes leídos.
     * @return Lista con todos los paquetes.
     */
    public List<Paquete> getPaquetes() {
        return new ArrayList<>(this.paquetes);
    }

    /**
     * SERVICIO 1: Retorna un paquete a partir de su código identificador.
     * <p>
     * <b>Complejidad temporal:</b> O(1). Acceder a este servicio es constante gracias a la función hash del mapa.
     *
     * @param codigoPaquete código único del paquete a buscar.
     * @return el objeto {@link Paquete} asociado al código, o {@code null} si no existe.
     */
    public Paquete servicio1(String codigoPaquete) {
        return mapPaquetes.get(codigoPaquete);
    }

    /**
     * SERVICIO 2: Retorna una lista de paquetes según si contienen alimentos o no.
     * <p>
     * <b>Complejidad temporal:</b> O(1). Acceder a este servicio es constante porque las listas ya están pre-clasificadas.
     *
     * @param contieneAlimentos {@code true} si se buscan paquetes con alimentos, {@code false} en caso contrario.
     * @return Lista correspondiente de paquetes.
     */
    public List<Paquete> servicio2(boolean contieneAlimentos) {
        return contieneAlimentos ? paquetesConAlimentos : paquetesSinAlimentos;
    }

    /**
     * SERVICIO 3: Retorna los paquetes cuyo nivel de urgencia se encuentre dentro del rango indicado.
     * <p>
     * <b>Complejidad temporal:</b> O(log n + k), donde:
     * <ul>
     * <li><i>n</i> representa la cantidad de niveles de urgencia distintos registrados en el árbol.</li>
     * <li><i>k</i> representa la cantidad de paquetes retornados dentro de ese subrango.</li>
     * </ul>
     *
     * @param urgenciaMinima nivel mínimo de urgencia (inclusivo).
     * @param urgenciaMaxima nivel máximo de urgencia (inclusivo).
     * @return Lista de paquetes que se encuentran dentro del rango de urgencia especificado.
     */
    public List<Paquete> servicio3(int urgenciaMinima, int urgenciaMaxima) {
        List<Paquete> resultado = new ArrayList<>();
        NavigableMap<Integer, List<Paquete>> rango =
                paquetesPorUrgencia.subMap(urgenciaMinima, true, urgenciaMaxima, true);

        for (List<Paquete> lista : rango.values()) {
            resultado.addAll(lista);
        }
        return resultado;
    }
}