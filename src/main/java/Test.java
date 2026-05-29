import models.Paquete;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        Servicios servicios = new Servicios(
                "/Camiones.csv",
                "/Paquetes.csv"
        );

        /*
         * TEST SERVICIO 1
         * Buscar paquete por código
         */
        System.out.println("SERVICIO 1");

        Paquete paqueteBuscado = servicios.servicio1("P001");

        if (paqueteBuscado != null) {
            System.out.println(paqueteBuscado);
        } else {
            System.out.println("No se encontró el paquete solicitado.");
        }

        /*
         * TEST SERVICIO 2
         * Buscar paquetes con alimentos
         */
        System.out.println("\nSERVICIO 2");

        List<Paquete> paquetesConAlimentos =
                servicios.servicio2(true);

        for (Paquete pk : paquetesConAlimentos)
            System.out.println(pk);

        /*
         * TEST SERVICIO 3
         * Buscar paquetes por rango de urgencia
         */
        System.out.println("\nSERVICIO 3");

        List<Paquete> paquetesUrgentes =
                servicios.servicio3(10, 100);

        for (Paquete pk : paquetesUrgentes)
            System.out.println(pk);
    }
}