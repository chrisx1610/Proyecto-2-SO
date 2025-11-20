/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * */
public class NodoAsignacion {
    private String nombreArchivo;
    private int cantidadBloques;
    private int primerBloque;
    private String propietario; // Requerido por el enunciado
    private String color;       // Para la GUI
    
    public NodoAsignacion siguiente;

    public NodoAsignacion(String nombreArchivo, int cantidadBloques, int primerBloque, String propietario, String color) {
        this.nombreArchivo = nombreArchivo;
        this.cantidadBloques = cantidadBloques;
        this.primerBloque = primerBloque;
        this.propietario = propietario;
        this.color = color;
        this.siguiente = null;
    }

    // Getters necesarios para llenar el JTable en la GUI
    public String getNombreArchivo() { return nombreArchivo; }
    public int getCantidadBloques() { return cantidadBloques; }
    public int getPrimerBloque() { return primerBloque; }
    public String getPropietario() { return propietario; }
    public String getColor() { return color; }
    
    // Setters por si el archivo cambia de tamaño
    public void setCantidadBloques(int cantidadBloques) { this.cantidadBloques = cantidadBloques; }

    @Override
    public String toString() {
        return String.format("Archivo: %-10s | Bloques: %-3d | Inicio: %-3d | Dueño: %s",
                nombreArchivo, cantidadBloques, primerBloque, propietario);
    }
}