import models.Camion;
import models.Paquete;
import utils.CSVReader;

import java.util.List;

public class Test {
    public static void main (String[] args) {
        // Paqueño testing para comprobar que la importación funciona
        List<Camion> camiones = CSVReader.obtenerInformacionCamiones();
        System.out.println(camiones.getFirst().getPatente());
        List<Paquete> paquetes = CSVReader.obtenerInformacionPaquetes();
        System.out.println(paquetes.getFirst().getCodigoPaquete());
    }
}
