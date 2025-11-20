/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * */
public class TablaAsignacion {
    private NodoAsignacion cabeza;

    public TablaAsignacion() {
        this.cabeza = null;
    }

    // FIRMA ACTUALIZADA
    public void agregar(String nombreArchivo, int cantidadBloques, int primerBloque, String propietario, String color) {
        NodoAsignacion nuevo = new NodoAsignacion(nombreArchivo, cantidadBloques, primerBloque, propietario, color);
        
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoAsignacion aux = cabeza;
            while (aux.siguiente != null) {
                aux = aux.siguiente;
            }
            aux.siguiente = nuevo;
        }
    }

    public NodoAsignacion buscar(String nombreArchivo) {
        NodoAsignacion aux = cabeza;
        while (aux != null) {
            if (aux.getNombreArchivo().equals(nombreArchivo)) return aux;
            aux = aux.siguiente;
        }
        return null;
    }

    public boolean eliminar(String nombreArchivo) {
        if (cabeza == null) return false;

        if (cabeza.getNombreArchivo().equals(nombreArchivo)) {
            cabeza = cabeza.siguiente;
            return true;
        }

        NodoAsignacion aux = cabeza;
        while (aux.siguiente != null) {
            if (aux.siguiente.getNombreArchivo().equals(nombreArchivo)) {
                aux.siguiente = aux.siguiente.siguiente;
                return true;
            }
            aux = aux.siguiente;
        }
        return false;
    }

    public boolean actualizarBloques(String nombreArchivo, int nuevaCantidad) {
        NodoAsignacion n = buscar(nombreArchivo);
        if (n != null) {
            n.setCantidadBloques(nuevaCantidad);
            return true;
        }
        return false;
    }

    // Para debug
    public void listar() {
        System.out.println("=== Tabla de Asignación ===");
        NodoAsignacion aux = cabeza;
        while (aux != null) {
            System.out.println(aux);
            aux = aux.siguiente;
        }
    }

    public NodoAsignacion getCabeza() {
        return cabeza;
    }
}