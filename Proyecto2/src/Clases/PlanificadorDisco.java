/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class PlanificadorDisco {
    private String politica; // "FIFO", "SSTF", "SCAN", "C-SCAN"
    private ColaProcesos cola;

    public PlanificadorDisco(String politica) {
        this.politica = politica;
        this.cola = new ColaProcesos();
    }

    public void agregarProceso(Proceso p) {
        cola.encolar(p);
    }

    public Proceso siguienteProceso() {
        // Lógica según la política (FIFO por defecto aquí)
        return cola.desencolar();
    }
}
