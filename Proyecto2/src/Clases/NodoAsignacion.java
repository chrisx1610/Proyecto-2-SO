/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class NodoAsignacion {
    String nombreArchivo;
    int cantidadBloques;
    int primerBloque;
    NodoAsignacion siguiente;

    public NodoAsignacion(String nombreArchivo, int cantidadBloques, int primerBloque) {
        this.nombreArchivo = nombreArchivo;
        this.cantidadBloques = cantidadBloques;
        this.primerBloque = primerBloque;
        this.siguiente = null;
    }

    @Override
    public String toString() {
        return String.format("Archivo: %-15s | Bloques: %-3d | Primer bloque: %d",
                nombreArchivo, cantidadBloques, primerBloque);
    }
}

