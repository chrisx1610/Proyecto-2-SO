/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 * Clase principal que orquesta todo el sistema.
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

    /**
     * Crea un archivo verificando permisos, espacio y ruta.
     */
    public String crearArchivo(String ruta, String nombre, int tamano, String color) {
        // 1. Validar Sesión
        if (usuarioActual == null) return "Error: No hay usuario logueado.";

        // 2. Validar Espacio en Disco (Método que agregamos a Disco.java)
        if (disco.contarBloquesLibres() < tamano) {
            return "Error: Espacio insuficiente en disco.";
        }

        // 3. Buscar el directorio destino (Soporta rutas anidadas)
        Directorio dir = buscarDirectorioPorRuta(ruta);
        if (dir == null) {
            return "Error: Ruta no encontrada (" + ruta + ")";
        }
        
        // 4. Validar duplicados en el directorio
        if (dir.getArchivos().buscar(nombre) != null) {
            return "Error: Ya existe un archivo con ese nombre.";
        }

        // 5. Crear el Objeto Archivo (Con dueño y color)
        Archivo archivo = new Archivo(nombre, tamano, usuarioActual.getNombre(), color);
        
        // 6. Asignar Bloques Físicos
        boolean asignacionExitosa = asignarBloques(archivo);
        
        if (asignacionExitosa) {
            // Agregar al árbol lógico
            dir.agregarArchivo(archivo);
            return "Archivo '" + nombre + "' creado exitosamente.";
        } else {
            return "Error crítico al asignar bloques.";
        }
    }

    /**
     * Asigna bloques libres del disco al archivo usando lista enlazada.
     * 
     */
    private boolean asignarBloques(Archivo archivo) {
        int bloquesAsignados = 0;
        int primerBloqueIndex = -1;
        Bloque bloqueAnterior = null;

        // Recorremos el disco buscando bloques libres
        for (int i = 0; i < disco.getBloques().length && bloquesAsignados < archivo.getTamanoEnBloques(); i++) {
            Bloque b = disco.getBloque(i);
            
            if (!b.isOcupado()) {
                // Marcar ocupado
                b.setOcupado(true);
                b.setArchivoAsociado(archivo.getNombre());
                
                // Manejo de punteros (Lista Enlazada)
                if (primerBloqueIndex == -1) {
                    primerBloqueIndex = i;
                    archivo.setPrimerBloque(b); // Guardar cabeza en el archivo
                }
                
                if (bloqueAnterior != null) {
                    bloqueAnterior.setSiguiente(b); // Enlazar anterior con actual
                }
                
                bloqueAnterior = b;
                bloquesAsignados++;
            }
        }
        
        // Registrar en la Tabla de Asignación
        if (primerBloqueIndex != -1) {
            tablaAsignacion.agregar(
                archivo.getNombre(), 
                archivo.getTamanoEnBloques(), 
                primerBloqueIndex, 
                archivo.getPropietario(), 
                archivo.getColor()
            );
            return true;
        }
        return false;
    }

    /**
     * Recorre una ruta tipo "root/carpeta1/carpeta2" y devuelve el directorio final.
     * 
     */
    public Directorio buscarDirectorioPorRuta(String ruta) {
        if (ruta.equals("root") || ruta.equals("/")) return raiz;
        
        String[] partes = ruta.split("/");
        Directorio actual = raiz;

        // Empezamos desde 1 porque el split de "/root/..." puede dejar vacío el inicio o asumir root
        for (String parte : partes) {
            if (parte.isEmpty() || parte.equals("root")) continue;
            
            // Usamos la lista de subdirectorios de la clase Directorio
            Directorio siguiente = actual.getSubdirectorios().buscar(parte);
            if (siguiente == null) {
                return null; // Ruta rota
            }
            actual = siguiente;
        }
        return actual;
    }
    
    public String eliminarArchivo(String ruta, String nombre) {
        Directorio dir = buscarDirectorioPorRuta(ruta);
        if (dir == null) return "Error: Ruta no existe.";
        
        Archivo a = dir.getArchivos().buscar(nombre);
        if (a == null) return "Error: Archivo no encontrado.";
        
        // VALIDACIÓN DE PERMISOS
        // Solo Admin o el dueño pueden borrar
        if (!usuarioActual.isEsAdmin() && !a.getPropietario().equals(usuarioActual.getNombre())) {
            return "Error: Permiso denegado. No eres el dueño.";
        }
        
        // 1. Eliminar del Directorio Lógico
        dir.getArchivos().eliminar(nombre);
        
        // 2. Eliminar de Tabla de Asignación
        tablaAsignacion.eliminar(nombre);
        
        // 3. Liberar Bloques en el Disco (Recorriendo la lista enlazada del archivo)
        Bloque actual = a.getPrimerBloque();
        while (actual != null) {
            Bloque siguiente = actual.getSiguiente();
            
            // Limpiamos el bloque físico
            disco.liberarBloque(actual.getIdBloque());
            
            actual = siguiente;
        }
        
        return "Archivo eliminado correctamente.";
    }

    // Getters para conectar con la GUI
    public Directorio getRaiz() { return raiz; }
    public Disco getDisco() { return disco; }
    public TablaAsignacion getTablaAsignacion() { return tablaAsignacion; }
    public PlanificadorDisco getPlanificador() { return planificador; }
    public Usuario getUsuarioActual() { return usuarioActual; }
}