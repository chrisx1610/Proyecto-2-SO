/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * */
public class Archivo {
    private String nombre;
    private int tamanoEnBloques;
    private Bloque primerBloque; // Cabeza de la lista enlazada
    
    // NUEVOS ATRIBUTOS NECESARIOS
    private String propietario; // Para validar permisos (Admin vs Usuario) [cite: 13, 21]
    private String color;       // Para la Tabla de Asignación y JTree 
    private String extension;   // Opcional, pero útil para tipos de archivo (txt, doc, etc.)

    public Archivo(String nombre, int tamano, String propietario, String color) {
        this.nombre = nombre;
        this.tamanoEnBloques = tamano;
        this.primerBloque = null;
        this.propietario = propietario;
        this.color = color;
        this.extension = ""; // Valor por defecto o pedirlo en constructor
    }

    // Getters y Setters existentes...

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

    // NUEVOS GETTERS Y SETTERS

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    @Override
    public String toString() {
        return "Archivo{" +
                "nombre='" + nombre + '\'' +
                ", tamaño=" + tamanoEnBloques + " bloques" +
                ", propietario='" + propietario + '\'' +
                '}';
    }
}
