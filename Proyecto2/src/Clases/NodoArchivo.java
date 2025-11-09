/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class NodoArchivo {
    Archivo archivo;
    NodoArchivo siguiente;

    public NodoArchivo(Archivo archivo) {
        this.archivo = archivo;
        this.siguiente = null;
    }
}
