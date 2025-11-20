/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 *
 */
public class Bloque {
    private int idBloque;
    private boolean ocupado;
    private Bloque siguiente; // para la asignación encadenada
    private String archivoAsociado;
    
    public Bloque(int id) {
        this.idBloque = id;
        this.ocupado = false;
        this.siguiente = null;
        this.archivoAsociado = null;
        
        
}

    public int getIdBloque() {
        return idBloque;
    }

    public void setIdBloque(int idBloque) {
        this.idBloque = idBloque;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public Bloque getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Bloque siguiente) {
        this.siguiente = siguiente;
    }

    public String getArchivoAsociado() {
        return archivoAsociado;
    }

    public void setArchivoAsociado(String archivoAsociado) {
        this.archivoAsociado = archivoAsociado;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Bloque{" +
                "id=" + idBloque +
                ", ocupado=" + ocupado +
                ", archivo='" + archivoAsociado + '\'' +
                '}';
    }
}
