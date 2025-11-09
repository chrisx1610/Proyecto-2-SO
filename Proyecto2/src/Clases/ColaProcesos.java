/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class ColaProcesos {
    private NodoProceso frente, fin;

    public void encolar(Proceso p) {
        NodoProceso nuevo = new NodoProceso(p);
        if (fin == null) {
            frente = fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
    }

    public Proceso desencolar() {
        if (frente == null) return null;
        Proceso p = frente.proceso;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        return p;
    }
}

