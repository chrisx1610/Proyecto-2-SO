/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * */
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

    // Modificado para propagar si hubo éxito o error (duplicado)
    public boolean agregarArchivo(Archivo archivo) {
        return archivos.insertar(archivo);
    }

    // Modificado para propagar éxito o error
    public boolean agregarSubdirectorio(Directorio dir) {
        dir.setPadre(this);
        return subdirectorios.insertar(dir);
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
            // System.out.println se puede quitar luego para limpiar código
            return true;
        }

        if (subdirectorios.eliminar(nombreObjetivo)) {
            return true;
        }

        return false;
    }
    
    public String obtenerRutaCompleta() {
        if (padre == null) return nombre;
        // Si padre es la raíz ("root"), a veces se prefiere "/nombre" en vez de "root/nombre"
        // Dependerá de cómo nombres tu raíz.
        return padre.obtenerRutaCompleta() + "/" + nombre;
    }

    // === GETTERS CRÍTICOS PARA EL JTREE ===
    
    public ListaArchivos getArchivos() {
        return archivos;
    }

    public ListaDirectorios getSubdirectorios() {
        return subdirectorios;
    }
    
    // ======================================

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