/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class Usuario {
    private String nombre;
    private boolean esAdmin;

    public Usuario(String nombre, boolean esAdmin) {
        this.nombre = nombre;
        this.esAdmin = esAdmin;
    }

    public boolean puedeEditar() {
        return esAdmin;
    }

    public boolean puedeLeer() {
        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }
    
}
