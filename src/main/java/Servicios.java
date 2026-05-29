import models.Camion;
import models.Paquete;
import utils.CSVReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Servicios {

    /**
     * Estructura para el servicio 1
     * - ¿Por qué?: Durante el uso del for, cargo los paquetes en un mapa aprovechando su codigo unico.
     * - Alternativa: La mejor opción que encontramos en este caso es HashMap para realizar una busqueda
     *                tan rapida. Sin logica adicional.
     * */
    private HashMap<String, Paquete> mapPaquetes = new HashMap<>();

    /**
     * Estructuras para el servicio 2.
     * - ¿Por qué?: Aprovecho el uso de un for en el constructor para definir que paquetes leidos.
     *              Seran enviados a cada una de las listas.
     * - Alternativa: Otra opción es usar un HashMap<boolean, List<Paquete>> la complejidad es la misma.
     * */
    private List<Paquete> paquetesConAlimentos = new ArrayList<>();
    private List<Paquete> paquetesSinAlimentos = new ArrayList<>();

    /*
     * Expresar la complejidad temporal del constructor.
     * La complejidad del constructor sera de O(n) debido a que depende de las cantidad de registros que vayamos a leer e insertar (CSV)
     */
    public Servicios(String pathCamiones, String pathPaquetes)
    {
        // La complejidad de leer el archivo y generar las listas es de O(n) donde n es el numero de registros del CSV.
        List<Camion> camiones = CSVReader.obtenerInformacionCamiones(pathCamiones);
        List<Paquete> paquetes = CSVReader.obtenerInformacionPaquetes(pathPaquetes);

        if(!paquetes.isEmpty()){
            // La complejidad de recorrer la lista de paquetes se expresa como O(n)
            for(Paquete pk : paquetes) {
                mapPaquetes.put(pk.getCodigoPaquete(), pk); // Complejidad de O(1)

                if(pk.isContieneAlimentos()){
                    paquetesConAlimentos.add(pk);
                } else {
                    paquetesSinAlimentos.add(pk);
                }
            }
        }
    }
    /*
     * Expresar la complejidad temporal del servicio 1.
     * Acceder a este servicio tiene una complejidad de O(1)
     */
    public Paquete servicio1(String codigoPaquete) {
        return mapPaquetes.get(codigoPaquete);
    }
    /*
     * Expresar la complejidad temporal del servicio 2.
     * Acceder a este servicio tiene una complejidad de O(1)
     */
    public List<Paquete> servicio2(boolean contieneAlimentos) {
        return contieneAlimentos ? paquetesConAlimentos : paquetesSinAlimentos;
    }
    /*
     * Expresar la complejidad temporal del servicio 3.
     */
    public List<Paquete> servicio3(int urgenciaMinima, int
            urgenciaMaxima) {
        return new ArrayList<>();
    }
}