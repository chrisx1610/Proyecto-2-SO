/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;


public class PlanificadorDisco {
    private String politica; 
    private ColaProcesos cola;
    private boolean pausado; 
    private int cabezaActual; 
    private boolean direccion; 
    private int maxCilindro; 

    public PlanificadorDisco(String politica) {
        this.politica = politica;
        this.cola = new ColaProcesos();
        this.pausado = false;
        this.cabezaActual = 0; 
        this.direccion = true; 
        this.maxCilindro = 199; 
    }

    public void agregarProceso(Proceso p) {
        p.setEstado("LISTO");
        cola.encolar(p);
        System.out.println("Planificador: Proceso agregado [" + p.getId() + "]");
    }

    public Proceso siguienteProceso() {
        if (cola.estaVacia()) return null;

        Proceso p = null;

        if (politica.equals("FIFO")) {
            p = cola.desencolar();
        } else if (politica.equals("SSTF")) {
            // SSTF: selecciona el proceso con la menor distancia a la cabeza actual
            int minDist = Integer.MAX_VALUE;
            int index = -1;
            for (int i = 0; i < cola.getTamano(); i++) {
                Proceso proc = cola.getProceso(i);
                int dist = Math.abs(cabezaActual - proc.getCilindro());
                if (dist < minDist) {
                    minDist = dist;
                    index = i;
                }
            }
            if (index != -1) {
                p = cola.removerPorIndice(index);
                cabezaActual = p.getCilindro();
            }
        } else if (politica.equals("SCAN")) {
            // SCAN: selecciona el siguiente en la dirección actual, o invierte si no hay
            int minDist = Integer.MAX_VALUE;
            int index = -1;
            for (int i = 0; i < cola.getTamano(); i++) {
                Proceso proc = cola.getProceso(i);
                int cil = proc.getCilindro();
                if (direccion && cil >= cabezaActual) {
                    int dist = cil - cabezaActual;
                    if (dist < minDist) {
                        minDist = dist;
                        index = i;
                    }
                } else if (!direccion && cil <= cabezaActual) {
                    int dist = cabezaActual - cil;
                    if (dist < minDist) {
                        minDist = dist;
                        index = i;
                    }
                }
            }
            if (index == -1) {
                // No hay en la dirección actual, invertir dirección
                direccion = !direccion;
                for (int i = 0; i < cola.getTamano(); i++) {
                    Proceso proc = cola.getProceso(i);
                    int cil = proc.getCilindro();
                    if (direccion && cil >= cabezaActual) {
                        int dist = cil - cabezaActual;
                        if (dist < minDist) {
                            minDist = dist;
                            index = i;
                        }
                    } else if (!direccion && cil <= cabezaActual) {
                        int dist = cabezaActual - cil;
                        if (dist < minDist) {
                            minDist = dist;
                            index = i;
                        }
                    }
                }
            }
            if (index != -1) {
                p = cola.removerPorIndice(index);
                cabezaActual = p.getCilindro();
            }
        } else if (politica.equals("C-SCAN")) {
            // C-SCAN: selecciona el siguiente en dirección creciente, salta a 0 si no hay
            int minDist = Integer.MAX_VALUE;
            int index = -1;
            for (int i = 0; i < cola.getTamano(); i++) {
                Proceso proc = cola.getProceso(i);
                int cil = proc.getCilindro();
                if (cil >= cabezaActual) {
                    int dist = cil - cabezaActual;
                    if (dist < minDist) {
                        minDist = dist;
                        index = i;
                    }
                }
            }
            if (index == -1) {
                // No hay en dirección creciente, saltar a 0 y buscar el menor >= 0
                for (int i = 0; i < cola.getTamano(); i++) {
                    Proceso proc = cola.getProceso(i);
                    int cil = proc.getCilindro();
                    if (cil >= 0) {
                        int dist = cil; // Desde 0
                        if (dist < minDist) {
                            minDist = dist;
                            index = i;
                        }
                    }
                }
                if (index != -1) {
                    cabezaActual = 0; // Salto a 0
                }
            }
            if (index != -1) {
                p = cola.removerPorIndice(index);
                cabezaActual = p.getCilindro();
            }
        }

        if (p != null) {
            p.setEstado("EJECUTANDO");
        }
        return p;
    }
    
    public ColaProcesos getCola() {
        return cola;
    }

    public String getPolitica() {
        return politica;
    }

    public void setPolitica(String politica) {
        this.politica = politica;
    }

    public int getCabezaActual() {
        return cabezaActual;
    }

    public void setCabezaActual(int cabezaActual) {
        this.cabezaActual = cabezaActual;
    }

    public boolean isDireccion() {
        return direccion;
    }

    public void setDireccion(boolean direccion) {
        this.direccion = direccion;
    }

    public int getMaxCilindro() {
        return maxCilindro;
    }

    public void setMaxCilindro(int maxCilindro) {
        this.maxCilindro = maxCilindro;
    }
}