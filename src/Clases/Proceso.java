/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

public class Proceso {
    private int id;
    private String tipoOperacion; 
    private String nombreArchivo;
    private String estado; 
    
    // Atributos base
    private Usuario propietario; 
    private int tamano;          

    // NUEVOS ATRIBUTOS (Para que la simulación sepa dónde crear y de qué color)
    private String rutaDestino;
    private String colorAsociado;

    // NUEVO ATRIBUTO PARA PLANIFICACIÓN DE DISCO
    private int cilindro; // Número de cilindro del disco para este proceso (para SSTF, SCAN, C-SCAN)

    // Constructor principal
    public Proceso(int id, String tipo, String archivo, Usuario propietario) {
        this.id = id;
        this.tipoOperacion = tipo.toUpperCase();
        this.nombreArchivo = archivo;
        this.propietario = propietario;
        this.estado = "NUEVO";
        this.tamano = 0;
        this.rutaDestino = "root"; // Valor por defecto
        this.colorAsociado = "Gris"; // Valor por defecto
        this.cilindro = 0; // Valor por defecto (puedes cambiarlo según necesidad)
    }
    
    // Constructor sobrecargado para CREAR (incluye tamaño)
    public Proceso(int id, String tipo, String archivo, Usuario propietario, int tamano) {
        this(id, tipo, archivo, propietario);
        this.tamano = tamano;
    }

    // NUEVO CONSTRUCTOR SOBRECARGADO PARA INCLUIR CILINDRO
    public Proceso(int id, String tipo, String archivo, Usuario propietario, int tamano, int cilindro) {
        this(id, tipo, archivo, propietario, tamano);
        this.cilindro = cilindro;
    }


    public void aListo() {
        this.estado = "LISTO";
    }

    public void aEjecutando() {
        this.estado = "EJECUTANDO";
    }
    
    public void aTerminado() {
        this.estado = "TERMINADO";
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // === GETTERS Y SETTERS ===

    public int getId() { return id; }
    public String getTipoOperacion() { return tipoOperacion; }
    public String getNombreArchivo() { return nombreArchivo; }
    public String getEstado() { return estado; }
    public Usuario getPropietario() { return propietario; }
    public int getTamano() { return tamano; }

    // Getters/Setters nuevos necesarios para la GUI
    public String getRutaDestino() { return rutaDestino; }
    public void setRutaDestino(String rutaDestino) { this.rutaDestino = rutaDestino; }

    public String getColorAsociado() { return colorAsociado; }
    public void setColorAsociado(String colorAsociado) { this.colorAsociado = colorAsociado; }


    public int getCilindro() { return cilindro; }
    public void setCilindro(int cilindro) { this.cilindro = cilindro; }

    @Override
    public String toString() {
        return id + " | " + tipoOperacion + " | " + nombreArchivo + " | " + estado;
    }
}