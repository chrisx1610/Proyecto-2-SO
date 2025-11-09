/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class Archivo {
    private String nombre;
    private int tamanoEnBloques;
    private Bloque primerBloque;

    public Archivo(String nombre, int tamano) {
        this.nombre = nombre;
        this.tamanoEnBloques = tamano;
        this.primerBloque = null;
        
        
}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTamanoEnBloques() {
        return tamanoEnBloques;
    }

    public void setTamanoEnBloques(int tamanoEnBloques) {
        this.tamanoEnBloques = tamanoEnBloques;
    }

    public Bloque getPrimerBloque() {
        return primerBloque;
    }

    public void setPrimerBloque(Bloque primerBloque) {
        this.primerBloque = primerBloque;
    }
    
    @Override
    public String toString() {
        return "Archivo{" +
                "nombre='" + nombre + '\'' +
                ", tamaño=" + tamanoEnBloques + " bloques" +
                '}';
    }
}
