/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * */
public class ListaArchivos {
    private NodoArchivo cabeza;

    // Modificado: Devuelve boolean para saber si se pudo insertar
    public boolean insertar(Archivo archivo) {
        // 1. Validar que no exista un archivo con el mismo nombre
        if (buscar(archivo.getNombre()) != null) {
            return false; // Ya existe, no se inserta
        }

        NodoArchivo nuevo = new NodoArchivo(archivo);
        
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoArchivo aux = cabeza;
            while (aux.siguiente != null) {
                aux = aux.siguiente;
            }
            aux.siguiente = nuevo;
        }
        return true; // Inserción exitosa
    }

    public Archivo buscar(String nombre) {
        NodoArchivo aux = cabeza;
        while (aux != null) {
            if (aux.archivo.getNombre().equals(nombre))
                return aux.archivo;
            aux = aux.siguiente;
        }
        return null;
    }
    
    public boolean eliminar(String nombre) {
        if (cabeza == null) return false;
        
        if (cabeza.archivo.getNombre().equals(nombre)) {
            cabeza = cabeza.siguiente;
            return true;
        }
        
        NodoArchivo aux = cabeza;
        while (aux.siguiente != null) {
            if (aux.siguiente.archivo.getNombre().equals(nombre)) {
                aux.siguiente = aux.siguiente.siguiente;
                return true;
            }
            aux = aux.siguiente;
        }
        return false;
    }

    // IMPORTANTE: Necesario para que el JTree/JTable pueda recorrer la lista
    public NodoArchivo getCabeza() {
        return cabeza;
    }

    // Método auxiliar para debug en consola
    public void listar() {
        NodoArchivo aux = cabeza;
        if (aux == null) {
            System.out.println("   (Directorio vacío)");
            return;
        }
        while (aux != null) {
            System.out.println("   [Archivo] " + aux.archivo.getNombre());
            aux = aux.siguiente;
        }
    }
}
