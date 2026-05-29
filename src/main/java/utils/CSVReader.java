package utils;

import models.Camion;
import models.Paquete;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<Camion> obtenerInformacionCamiones(String path) {
        InputStream ruta = CSVReader.class.getResourceAsStream(path);

        if (ruta == null) {
            System.err.println("No se encontró el archivo CSV.");
            return null;
        }

        try (
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setDelimiter(';') // Separamos los elementos de cada fila en donde encontremos (;)
                        .build()
                        .parse(new InputStreamReader(ruta))
        ) {
            List<Camion> camiones = new ArrayList<>();

            for (CSVRecord row : parser) {
                // Si la linea no tiene 4 campos separados por ;  la evitamos. (La primera fila siempre representa la totalidad de registros)
                if (row.size() < 4) {
                    continue;
                }

                String idCamion = row.get(0); // Obtenemos el ID
                String patente = row.get(1);  // Obtenemos la patente.

                // Transformamos el string en booleano.
                boolean estaRefrigerado = "1".equals(row.get(2));

                int capacidadKg = Integer.parseInt(row.get(3));

                Camion cc = new Camion(idCamion, patente, estaRefrigerado, capacidadKg);
                camiones.add(cc);
            }
            return camiones;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Funciona igual a la función anterior, solo cambia la cantidad de elementos dentro de cada registro (row)
    public static List<Paquete> obtenerInformacionPaquetes(String path) {
        InputStream ruta = CSVReader.class.getResourceAsStream(path);

        if (ruta == null) {
            System.err.println("No se encontró el archivo CSV de paquetes.");
            return null;
        }

        try (
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setDelimiter(';')
                        .build()
                        .parse(new InputStreamReader(ruta))
        ) {
            List<Paquete> paquetes = new ArrayList<>();

            for (CSVRecord row : parser) {
                if (row.size() < 5) {
                    continue;
                }

                String idPaquete = row.get(0);
                String codigoPaquete = row.get(1);
                int pesoKg = Integer.parseInt(row.get(2));

                boolean contieneAlimentos = "1".equals(row.get(3));

                int nivelUrgencia = Integer.parseInt(row.get(4));

                Paquete nuevoPaquete = new Paquete(idPaquete, codigoPaquete, pesoKg, contieneAlimentos, nivelUrgencia);
                paquetes.add(nuevoPaquete);
            }
            return paquetes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}