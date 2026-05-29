import models.*;

import utils.CSVReader;

import java.util.*;

public class Servicios {

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
        List<Camion> camiones = CSVReader.obtenerInformacionCamiones(pathCamiones);
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
}