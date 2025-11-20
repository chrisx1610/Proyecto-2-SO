/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * */
public class ListaDirectorios {
    private NodoDirectorio cabeza;

    public ListaDirectorios() {
        this.cabeza = null;
    }

    // Modificado: Devuelve boolean y valida duplicados
    public boolean insertar(Directorio dir) {
        // Validar duplicados
        if (buscar(dir.getNombre()) != null) {
            return false;
        }

        NodoDirectorio nuevo = new NodoDirectorio(dir);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoDirectorio aux = cabeza;
            while (aux.siguiente != null) {
                aux = aux.siguiente;
            }
            aux.siguiente = nuevo;
        }
        return true;
    }

    public Directorio buscar(String nombre) {
        NodoDirectorio aux = cabeza;
        while (aux != null) {
            if (aux.directorio.getNombre().equals(nombre)) {
                return aux.directorio;
            }
            aux = aux.siguiente;
        }
        return null; // No encontrado
    }


    public boolean eliminar(String nombre) {
        if (cabeza == null) return false;

        if (cabeza.directorio.getNombre().equals(nombre)) {
            cabeza = cabeza.siguiente;
            return true;
        }

        NodoDirectorio aux = cabeza;
        while (aux.siguiente != null) {
            if (aux.siguiente.directorio.getNombre().equals(nombre)) {
                aux.siguiente = aux.siguiente.siguiente;
                return true;
            }
            aux = aux.siguiente;
        }

        return false; // No encontrado
    }

    // Getter necesario para la GUI
    public NodoDirectorio getCabeza() {
        return cabeza;
    }
    
    // Auxiliar para consola
    public void listar() {
        NodoDirectorio aux = cabeza;
        if (aux == null) {
            System.out.println("   [Sin subdirectorios]");
            return;
        }
        while (aux != null) {
            System.out.println("   [Dir] " + aux.directorio.getNombre());
            aux = aux.siguiente;
        }
    }
}
