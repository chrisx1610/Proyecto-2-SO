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

    public Proceso desencolar() {
        if (frente == null) return null;
        Proceso p = frente.proceso;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamano--;
        return p;
    }
    
    // === ESTE ES EL MÉTODO QUE TE FALTARÁ LUEGO ===
    public boolean estaVacia() {
        return frente == null;
    }
    // ==============================================
    
    public NodoProceso getFrente() {
        return frente;
    }
    
    public int getTamano() {
        return tamano;
    }
}