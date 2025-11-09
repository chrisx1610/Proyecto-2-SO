/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class Directorio {
    private String nombre;
    private ListaDirectorios subdirectorios;
    private ListaArchivos archivos;
    private Directorio padre; // útil para rastrear rutas completas

    public Directorio(String nombre) {
        this.nombre = nombre;
        this.subdirectorios = new ListaDirectorios();
        this.archivos = new ListaArchivos();
        this.padre = null;
    }

    public void agregarArchivo(Archivo archivo) {
        archivos.insertar(archivo);
    }

    public void agregarSubdirectorio(Directorio dir) {
        dir.setPadre(this);
        subdirectorios.insertar(dir);
    }

    public Object buscar(String nombreBuscado) {
        Archivo a = archivos.buscar(nombreBuscado);
        if (a != null) return a;

        Directorio d = subdirectorios.buscar(nombreBuscado);
        if (d != null) return d;

        return null; // no encontrado
    }

    public boolean eliminar(String nombreObjetivo) {
        if (archivos.eliminar(nombreObjetivo)) {
            System.out.println("Archivo '" + nombreObjetivo + "' eliminado de " + this.nombre);
            return true;
        }

        if (subdirectorios.eliminar(nombreObjetivo)) {
            System.out.println("Directorio '" + nombreObjetivo + "' eliminado de " + this.nombre);
            return true;
        }

        System.out.println("No se encontró '" + nombreObjetivo + "' en " + this.nombre);
        return false;
    }

    public void listarContenido() {
        System.out.println("Contenido de " + nombre + ":");
        archivos.listar();
        subdirectorios.listar();
    }

    public String obtenerRutaCompleta() {
        if (padre == null) return nombre;
        return padre.obtenerRutaCompleta() + "/" + nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Directorio getPadre() {
        return padre;
    }

    public void setPadre(Directorio padre) {
        this.padre = padre;
    }
}
