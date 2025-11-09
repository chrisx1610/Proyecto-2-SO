/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class TablaAsignacion {
    private NodoAsignacion cabeza;

    public TablaAsignacion() {
        this.cabeza = null;
    }

    public void agregar(String nombreArchivo, int cantidadBloques, int primerBloque) {
        NodoAsignacion nuevo = new NodoAsignacion(nombreArchivo, cantidadBloques, primerBloque);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoAsignacion aux = cabeza;
            while (aux.siguiente != null) aux = aux.siguiente;
            aux.siguiente = nuevo;
        }
    }

    public NodoAsignacion buscar(String nombreArchivo) {
        NodoAsignacion aux = cabeza;
        while (aux != null) {
            if (aux.nombreArchivo.equals(nombreArchivo)) return aux;
            aux = aux.siguiente;
        }
        return null;
    }

    public boolean eliminar(String nombreArchivo) {
        if (cabeza == null) return false;

        if (cabeza.nombreArchivo.equals(nombreArchivo)) {
            cabeza = cabeza.siguiente;
            return true;
        }

        NodoAsignacion aux = cabeza;
        while (aux.siguiente != null) {
            if (aux.siguiente.nombreArchivo.equals(nombreArchivo)) {
                aux.siguiente = aux.siguiente.siguiente;
                return true;
            }
            aux = aux.siguiente;
        }
        return false;
    }

    public void listar() {
        System.out.println("=== Tabla de Asignación de Archivos ===");
        if (cabeza == null) {
            System.out.println("(vacía)");
            return;
        }
        NodoAsignacion aux = cabeza;
        while (aux != null) {
            System.out.println(aux);
            aux = aux.siguiente;
        }
    }

    public boolean actualizarBloques(String nombreArchivo, int nuevaCantidad) {
        NodoAsignacion n = buscar(nombreArchivo);
        if (n != null) {
            n.cantidadBloques = nuevaCantidad;
            return true;
        }
        return false;
    }

    public int contar() {
        int total = 0;
        NodoAsignacion aux = cabeza;
        while (aux != null) {
            total++;
            aux = aux.siguiente;
        }
        return total;
    }

    public NodoAsignacion getCabeza() {
        return cabeza;
    }
}
