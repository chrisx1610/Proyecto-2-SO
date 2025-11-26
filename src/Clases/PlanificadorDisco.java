/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

public class PlanificadorDisco {
    private String politica; // "FIFO", "LIFO", "PRIORIDAD", "SJF"
    private ColaProcesos cola;

    public PlanificadorDisco(String politica) {
        this.politica = politica;
        this.cola = new ColaProcesos();
    }

    public void agregarProceso(Proceso p) {
        p.setEstado("LISTO");
        cola.encolar(p);
    }

    public Proceso siguienteProceso() {
        if (cola.estaVacia()) return null;

        Proceso seleccionado = null;

        switch (politica) {
            case "FIFO":
                seleccionado = cola.desencolar();
                break;

            case "LIFO":
                seleccionado = obtenerLIFO();
                break;

            case "PRIORIDAD":
                seleccionado = obtenerPrioridad();
                break;
                
            case "SJF": // Shortest Job First (Por tamaño)
                seleccionado = obtenerSJF();
                break;
                
            default:
                seleccionado = cola.desencolar(); // FIFO por defecto
        }

        if (seleccionado != null) {
            seleccionado.setEstado("EJECUTANDO");
        }
        return seleccionado;
    }
    
    // --- ALGORITMOS ---

    private Proceso obtenerLIFO() {
        // En una lista enlazada simple, el último es costoso de sacar.
        // Recorremos hasta el final.
        NodoProceso actual = cola.getFrente();
        Proceso ultimo = null;
        
        while(actual != null) {
            ultimo = actual.proceso;
            actual = actual.siguiente;
        }
        
        // Lo sacamos de la cola
        if(ultimo != null) cola.removerProceso(ultimo);
        return ultimo;
    }

    private Proceso obtenerPrioridad() {
        // Regla: Admin va primero. Si no hay admin, FIFO normal.
        NodoProceso actual = cola.getFrente();
        Proceso candidato = null;
        
        while(actual != null) {
            if (actual.proceso.getPropietario().isEsAdmin()) {
                candidato = actual.proceso;
                break; // Encontramos el primer admin, nos lo llevamos
            }
            actual = actual.siguiente;
        }
        
        // Si no hubo admin, tomamos el primero (FIFO)
        if (candidato == null) {
            return cola.desencolar();
        } else {
            cola.removerProceso(candidato);
            return candidato;
        }
    }
    
    private Proceso obtenerSJF() {
        // Regla: El proceso con MENOR tamaño (tamanoEnBloques) va primero.
        NodoProceso actual = cola.getFrente();
        Proceso candidato = actual.proceso;
        int menorTamano = candidato.getTamano();
        
        while(actual != null) {
            if (actual.proceso.getTamano() < menorTamano) {
                menorTamano = actual.proceso.getTamano();
                candidato = actual.proceso;
            }
            actual = actual.siguiente;
        }
        
        cola.removerProceso(candidato);
        return candidato;
    }

    public ColaProcesos getCola() { return cola; }
    public void setPolitica(String politica) { this.politica = politica; }
    public String getPolitica() { return politica; }
}