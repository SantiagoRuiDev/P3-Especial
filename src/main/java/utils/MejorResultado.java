package utils;

import models.Paquete;

import java.util.HashMap;
import java.util.List;

// Esta clase auxiliar me ayuda a llevar registro del peso no asignado (paquetes sin asignar a ningun camion) y la mejor solución encontrada.
public class MejorResultado {
    HashMap<Integer, List<Paquete>> solucion = new HashMap<>();
    int pesoNoAsignadoKg = Integer.MAX_VALUE; // Peor escenario posible

    public HashMap<Integer, List<Paquete>> getSolucion() {
        return solucion;
    }

    public int getPesoNoAsignadoKg() {
        return pesoNoAsignadoKg;
    }

    public void setSolucion(HashMap<Integer, List<Paquete>> solucion) {
        this.solucion = solucion;
    }

    public void setPesoNoAsignadoKg(int pesoNoAsignadoKg) {
        this.pesoNoAsignadoKg = pesoNoAsignadoKg;
    }
}