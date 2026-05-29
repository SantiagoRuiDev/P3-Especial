package utils;

import models.*;

import org.apache.commons.csv.*;

import java.io.*;
import java.util.*;

/**
 * Clase utilitaria encargada de leer archivos CSV de camiones y paquetes.
 */
public class CSVReader {

    /**
     * Evita instanciación de la clase.
     */
    private CSVReader() {
    }

    /**
     * Obtiene la lista de camiones desde un archivo CSV.
     *
     * @param path ruta del archivo CSV
     * @return lista de camiones leídos
     */
    public static List<Camion> obtenerInformacionCamiones(String path) {
        List<Camion> camiones = new ArrayList<>();

        InputStream ruta = CSVReader.class.getResourceAsStream(path);

        if (ruta == null) {
            System.err.println("No se encontró el archivo CSV de camiones.");
            return camiones;
        }

        try (
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setDelimiter(';') // Separamos los elementos de cada fila en donde encontremos (;)
                        .build()
                        .parse(new InputStreamReader(ruta))
        ) {

            boolean primeraFila = true;

            for (CSVRecord row : parser) {
                // La primera fila contiene la cantidad total de registros
                if (primeraFila) {
                    primeraFila = false;
                    continue;
                }

                // Si la linea no tiene 4 campos separados por ;  la evitamos. (La primera fila siempre representa la totalidad de registros)
                if (row.size() < 4) {
                    continue;
                }

                int idCamion = Integer.parseInt(row.get(0)); // Obtenemos el ID.
                String patente = row.get(1); // Obtenemos la patente.
                // Transformamos el string en booleano.
                boolean estaRefrigerado = "1".equals(row.get(2));

                int capacidadKg = Integer.parseInt(row.get(3));

                Camion camion = new Camion(
                        idCamion,
                        patente,
                        estaRefrigerado,
                        capacidadKg
                );

                camiones.add(camion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return camiones;
    }

    /**
     * Obtiene la lista de paquetes desde un archivo CSV.
     *
     * @param path ruta del archivo CSV
     * @return lista de paquetes leídos
     */
    public static List<Paquete> obtenerInformacionPaquetes(String path) {
        List<Paquete> paquetes = new ArrayList<>();

        InputStream ruta = CSVReader.class.getResourceAsStream(path);

        if (ruta == null) {
            System.err.println("No se encontró el archivo CSV de paquetes.");
            return paquetes;
        }

        try (
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setDelimiter(';')
                        .build()
                        .parse(new InputStreamReader(ruta))
        ) {

            boolean primeraFila = true;

            for (CSVRecord row : parser) {
                if (primeraFila) {
                    primeraFila = false;
                    continue;
                }

                if (row.size() < 5) {
                    continue;
                }

                int idPaquete = Integer.parseInt(row.get(0));
                String codigoPaquete = row.get(1);
                int pesoKg = Integer.parseInt(row.get(2));
                boolean contieneAlimentos = "1".equals(row.get(3));
                int nivelUrgencia = Integer.parseInt(row.get(4));

                Paquete paquete = new Paquete(
                        idPaquete,
                        codigoPaquete,
                        pesoKg,
                        contieneAlimentos,
                        nivelUrgencia
                );

                paquetes.add(paquete);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paquetes;
    }
}