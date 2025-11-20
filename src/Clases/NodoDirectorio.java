/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class NodoDirectorio {
    Directorio directorio;
    NodoDirectorio siguiente;

    public NodoDirectorio(Directorio directorio) {
        this.directorio = directorio;
        this.siguiente = null;
    }

    public Directorio getDirectorio() {
        return directorio;
    }

    public NodoDirectorio getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoDirectorio siguiente) {
        this.siguiente = siguiente;
    }
}

