/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 * Gestionar la cola de procesos de E/S.
 */
public class PlanificadorDisco {
    private String politica; // "FIFO", "SSTF", etc.
    private ColaProcesos cola;
    private boolean pausado; // Para controlar la simulación en la GUI

    public PlanificadorDisco(String politica) {
        this.politica = politica;
        this.cola = new ColaProcesos();
        this.pausado = false;
    }

    public void agregarProceso(Proceso p) {
        p.setEstado("LISTO");
        cola.encolar(p);
        System.out.println("Planificador: Proceso agregado [" + p.getId() + "]");
    }

    public Proceso siguienteProceso() {
        if (cola.estaVacia()) return null;

        // Aquí iría la lógica si fuera SSTF o SCAN.
        // Por ahora, implementamos FIFO (First In, First Out) por defecto.
        
        Proceso p = cola.desencolar();
        if (p != null) {
            p.setEstado("EJECUTANDO");
        }
        return p;
    }
    
    public ColaProcesos getCola() {
        return cola;
    }

    public String getPolitica() {
        return politica;
    }

    public void setPolitica(String politica) {
        this.politica = politica;
    }
}