/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

public class ColaProcesos {
    private NodoProceso frente, fin;
    private int tamano;

    public ColaProcesos() {
        this.frente = null;
        this.fin = null;
        this.tamano = 0;
    }

    public void encolar(Proceso p) {
        NodoProceso nuevo = new NodoProceso(p);
        if (fin == null) {
            frente = fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamano++;
    }

    public Proceso desencolar() { // FIFO normal
        if (frente == null) return null;
        Proceso p = frente.proceso;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamano--;
        return p;
    }
    
    // === NUEVO: MÉTODOS PARA OTRAS POLÍTICAS ===

    // Extrae un proceso específico de la cola (necesario para Prioridad y SJF)
    public boolean removerProceso(Proceso pObjetivo) {
        if (frente == null) return false;

        // Caso 1: Es el primero
        if (frente.proceso == pObjetivo) {
            frente = frente.siguiente;
            if (frente == null) fin = null;
            tamano--;
            return true;
        }

        // Caso 2: Buscar en el resto
        NodoProceso actual = frente;
        while (actual.siguiente != null) {
            if (actual.siguiente.proceso == pObjetivo) {
                actual.siguiente = actual.siguiente.siguiente; // Saltar el nodo
                if (actual.siguiente == null) fin = actual; // Actualizar fin si borramos el último
                tamano--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public NodoProceso getFrente() { return frente; }
    public boolean estaVacia() { return frente == null; }
    public int getTamano() { return tamano; }
}