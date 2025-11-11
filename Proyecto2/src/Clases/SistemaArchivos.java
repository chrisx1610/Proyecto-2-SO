/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * 
 */
public class SistemaArchivos {
    private Directorio raiz;
    private Disco disco;
    private TablaAsignacion tablaAsignacion;
    private PlanificadorDisco planificador;
    private Usuario usuarioActual;

    public SistemaArchivos(int totalBloques) {
        this.raiz = new Directorio("root");
        this.disco = new Disco(totalBloques);
        this.tablaAsignacion = new TablaAsignacion();
        this.planificador = new PlanificadorDisco("FIFO");
        this.usuarioActual = null;
    }

    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
        System.out.println("Sesión iniciada como: " + usuario.getNombre());
    }

    // CRUD integrados :)
    public void crearArchivo(String ruta, String nombre, int tamano) {
        if (!usuarioActual.puedeEditar()) {
            System.out.println("Permiso denegado: solo administradores pueden crear archivos.");
            return;
        }

        Directorio dir = buscarDirectorio(ruta);
        if (dir == null) {
            System.out.println("Ruta no encontrada: " + ruta);
            return;
        }

        Archivo archivo = new Archivo(nombre, tamano);
        dir.agregarArchivo(archivo);

        asignarBloques(archivo);

        System.out.println("Archivo '" + nombre + "' creado en " + ruta);
    }

    private void asignarBloques(Archivo archivo) {
        int bloquesAsignados = 0;
        int primerBloque = -1;
        int anterior = -1;

        while (bloquesAsignados < archivo.getTamanoEnBloques()) {
            int bloqueLibre = disco.obtenerBloqueLibre();
            if (bloqueLibre == -1) {
                System.out.println("Error: sin espacio en disco.");
                return;
            }
            Bloque b = disco.getBloques()[bloqueLibre];
            b.setOcupado(true);
            b.setArchivoAsociado(archivo.getNombre());

            if (primerBloque == -1) primerBloque = bloqueLibre;
            if (anterior != -1)
                disco.getBloques()[anterior].setSiguiente(b);

            anterior = bloqueLibre;
            bloquesAsignados++;
        }

        archivo.setPrimerBloque(disco.getBloques()[primerBloque]);
        tablaAsignacion.agregar(archivo.getNombre(), archivo.getTamanoEnBloques(), primerBloque);
    }

    public Directorio buscarDirectorio(String nombre) {
        if (nombre.equals("root")) return raiz;
        return raiz.buscar(nombre) instanceof Directorio ? (Directorio) raiz.buscar(nombre) : null;
    }

    public void mostrarEstructura() {
        System.out.println("=== Estructura del Sistema ===");
        raiz.listarContenido();
        System.out.println();
        tablaAsignacion.listar();
    }

    public void eliminarArchivo(String nombre) {
        Directorio dir = raiz;
        if (dir.eliminar(nombre)) {
            tablaAsignacion.eliminar(nombre);
            for (Bloque b : disco.getBloques()) {
                if (nombre.equals(b.getArchivoAsociado())) {
                    disco.liberarBloque(b.getIdBloque());
                }
            }
        }
    }

    public Directorio getRaiz() {
        return raiz;
    }

    public Disco getDisco() {
        return disco;
    }

    public TablaAsignacion getTablaAsignacion() {
        return tablaAsignacion;
    }

    public PlanificadorDisco getPlanificador() {
        return planificador;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
}

