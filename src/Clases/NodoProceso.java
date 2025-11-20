/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author Dell
 */
public class NodoProceso {
    Proceso proceso;
    NodoProceso siguiente;

    public NodoProceso(Proceso proceso) {
        this.proceso = proceso;
        this.siguiente = null;
    }
}
