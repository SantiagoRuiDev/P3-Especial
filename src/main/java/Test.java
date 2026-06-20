import models.Paquete;
import services.Backtracking;
import services.Greedy;
import services.Servicios;
import java.util.List;

/**
 * Clase de prueba principal para verificar la correcta funcionalidad
 * de los servicios y los algoritmos de resolución de asignación.
 */
public class Test {

    /**
     * Punto de entrada principal de la aplicación.
     * @param args argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Inicialización del servicio con las rutas de los archivos de datos
        Servicios servicios = new Servicios(
                "/Camiones.csv",
                "/Paquetes.csv"
        );

        /* TEST SERVICIO 1: Buscar paquete por código */
        System.out.println("--- SERVICIO 1: Búsqueda por código ---");
        Paquete paqueteBuscado = servicios.servicio1("P001");
        if (paqueteBuscado != null) {
            System.out.println(paqueteBuscado);
        } else {
            System.out.println("No se encontró el paquete solicitado.");
        }

        /* TEST SERVICIO 2: Buscar paquetes con alimentos */
        System.out.println("\n--- SERVICIO 2: Paquetes con alimentos ---");
        List<Paquete> paquetesConAlimentos = servicios.servicio2(true);
        paquetesConAlimentos.forEach(System.out::println);

        /* TEST SERVICIO 3: Buscar paquetes por rango de urgencia */
        System.out.println("\n--- SERVICIO 3: Rango de urgencia (10-100) ---");
        List<Paquete> paquetesUrgentes = servicios.servicio3(10, 100);
        paquetesUrgentes.forEach(System.out::println);

        /* TEST GREEDY: Asignación iterativa */
        System.out.println("\n--- ALGORITMO GREEDY ---");
        Greedy estrategiaIterativa = new Greedy(
                servicios.getPaquetesConAlimentos(),
                servicios.getPaquetesSinAlimentos(),
                servicios.getCamiones()
        );
        System.out.println(estrategiaIterativa.greedy());

        /* TEST BACKTRACKING: Asignación recursiva óptima */
        System.out.println("\n--- ALGORITMO BACKTRACKING ---");
        Backtracking estrategiaRecursiva = new Backtracking(
                servicios.getPaquetes(),
                servicios.getCamiones()
        );
        System.out.println(estrategiaRecursiva.backtracking());
    }
}