/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class Proceso {
    private int id;
    private String tipoOperacion; // "CREAR", "LEER", "ACTUALIZAR", "ELIMINAR"
    private String nombreArchivo;
    private String estado; // "NUEVO", "LISTO", "EJECUTANDO", "BLOQUEADO", "TERMINADO"

    public Proceso(int id, String tipo, String archivo) {
        this.id = id;
        this.tipoOperacion = tipo.toUpperCase();
        this.nombreArchivo = archivo;
        this.estado = "NUEVO";
    }

    public void aListo() {
        if (!estado.equals("TERMINADO")) {
            estado = "LISTO";
            System.out.println("Proceso " + id + " -> LISTO");
        }
    }

    public void aEjecutando() {
        if (estado.equals("LISTO")) {
            estado = "EJECUTANDO";
            System.out.println("Proceso " + id + " -> EJECUTANDO");
        } else {
            System.out.println("No se puede ejecutar el proceso " + id + " desde el estado actual: " + estado);
        }
    }

    public void aBloqueado() {
        if (estado.equals("EJECUTANDO")) {
            estado = "BLOQUEADO";
            System.out.println("Proceso " + id + " -> BLOQUEADO");
        } else {
            System.out.println("No se puede bloquear el proceso " + id + " desde el estado actual: " + estado);
        }
    }

    public void aTerminado() {
        if (estado.equals("EJECUTANDO") || estado.equals("BLOQUEADO")) {
            estado = "TERMINADO";
            System.out.println("Proceso " + id + " -> TERMINADO");
        } else {
            System.out.println("No se puede terminar el proceso " + id + " desde el estado actual: " + estado);
        }
    }

    // Reinicia el proceso (útil para simular reintentos o reasignaciones)
    public void reiniciar() {
        estado = "NUEVO";
        System.out.println("Proceso " + id + " reiniciado -> NUEVO");
    }

    public int getId() {
        return id;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Proceso{" +
                "id=" + id +
                ", tipoOperacion='" + tipoOperacion + '\'' +
                ", archivo='" + nombreArchivo + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
