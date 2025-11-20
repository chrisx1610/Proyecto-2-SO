/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * */
public class Disco {
    private Bloque[] bloques;
    private int totalBloques;

    public Disco(int totalBloques) {
        this.totalBloques = totalBloques;
        this.bloques = new Bloque[totalBloques];
        for (int i = 0; i < totalBloques; i++) {
            this.bloques[i] = new Bloque(i);
        }
    }

    public int obtenerBloqueLibre() {
        for (int i = 0; i < totalBloques; i++) {
            if (!bloques[i].isOcupado()) return i;
        }
        return -1; // No hay bloques disponibles
    }

    // NUEVO MÉTODO: Crucial para validar antes de crear archivo
    public int contarBloquesLibres() {
        int libres = 0;
        for (Bloque b : bloques) {
            if (!b.isOcupado()) {
                libres++;
            }
        }
        return libres;
    }

    // Permite acceder a un bloque específico (útil para la lógica de encadenamiento)
    public Bloque getBloque(int id) {
        if (id >= 0 && id < totalBloques) {
            return bloques[id];
        }
        return null;
    }

    public void liberarBloque(int id) {
        if (id >= 0 && id < totalBloques) {
            bloques[id].setOcupado(false);
            bloques[id].setArchivoAsociado(null);
            bloques[id].setSiguiente(null); // Importante romper el enlace
        }
    }

    public Bloque[] getBloques() {
        return bloques;
    }
    
    public int getTotalBloques() {
        return totalBloques;
    }
}