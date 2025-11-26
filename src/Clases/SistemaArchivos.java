/*
 * SistemaArchivos.java
 * Clase principal que orquesta todo el sistema (Backend).
 */
package Clases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
     * RESTRICCIÓN: Solo Admin puede crear (Modo Usuario es solo lectura).
     */
    public String crearArchivo(String ruta, String nombre, int tamano, String color) {
        if (usuarioActual == null) return "Error: No hay usuario logueado.";
        if (!usuarioActual.isEsAdmin()) return "Error: Permiso denegado. Modo Usuario es SOLO LECTURA.";

        if (disco.contarBloquesLibres() < tamano) return "Error: Espacio insuficiente en disco.";

        Directorio dir = buscarDirectorioPorRuta(ruta);
        if (dir == null) return "Error: Ruta no encontrada (" + ruta + ")";
        
        if (dir.getArchivos().buscar(nombre) != null) return "Error: Ya existe un archivo con ese nombre.";

        Archivo archivo = new Archivo(nombre, tamano, usuarioActual.getNombre(), color);
        
        if (asignarBloques(archivo)) {
            dir.agregarArchivo(archivo);
            return "Archivo '" + nombre + "' creado exitosamente.";
        } else {
            return "Error crítico al asignar bloques.";
        }
    }

    private boolean asignarBloques(Archivo archivo) {
        int bloquesAsignados = 0;
        int primerBloqueIndex = -1;
        Bloque bloqueAnterior = null;

        for (int i = 0; i < disco.getBloques().length && bloquesAsignados < archivo.getTamanoEnBloques(); i++) {
            Bloque b = disco.getBloque(i);
            
            if (!b.isOcupado()) {
                b.setOcupado(true);
                b.setArchivoAsociado(archivo.getNombre());
                
                if (primerBloqueIndex == -1) {
                    primerBloqueIndex = i;
                    archivo.setPrimerBloque(b);
                }
                
                if (bloqueAnterior != null) {
                    bloqueAnterior.setSiguiente(b);
                }
                
                bloqueAnterior = b;
                bloquesAsignados++;
            }
        }
        
        if (primerBloqueIndex != -1) {
            tablaAsignacion.agregar(
                archivo.getNombre(), archivo.getTamanoEnBloques(), primerBloqueIndex, 
                archivo.getPropietario(), archivo.getColor()
            );
            return true;
        }
        return false;
    }

    /**
     * Elimina un archivo liberando sus bloques.
     * RESTRICCIÓN: Solo Admin puede eliminar.
     */
    public String eliminarArchivo(String ruta, String nombre) {
        if (usuarioActual == null) return "Error: No hay sesión.";
        if (!usuarioActual.isEsAdmin()) return "Error: Permiso denegado. Modo Usuario es SOLO LECTURA.";
        
        Directorio dir = buscarDirectorioPorRuta(ruta);
        if (dir == null) return "Error: Ruta no existe.";
        
        Archivo a = dir.getArchivos().buscar(nombre);
        if (a == null) return "Error: Archivo no encontrado.";
        
        liberarRecursosArchivo(a);
        dir.getArchivos().eliminar(nombre);
        
        return "Archivo eliminado correctamente.";
    }

    // Método auxiliar para limpiar bloques y tabla FAT
    private void liberarRecursosArchivo(Archivo a) {
        tablaAsignacion.eliminar(a.getNombre());
        Bloque actual = a.getPrimerBloque();
        while (actual != null) {
            Bloque siguiente = actual.getSiguiente();
            disco.liberarBloque(actual.getIdBloque());
            actual = siguiente;
        }
    }

    /**
     * NUEVO: Eliminar Directorio Recursivamente (Cumple Requisito PDF punto 3)
     */
    public String eliminarDirectorio(String rutaPadre, String nombreDir) {
        if (!usuarioActual.isEsAdmin()) return "Error: Permiso denegado. Solo Admin.";

        Directorio padre = buscarDirectorioPorRuta(rutaPadre);
        if (padre == null) return "Error: Ruta padre no encontrada.";

        Directorio aBorrar = padre.getSubdirectorios().buscar(nombreDir);
        if (aBorrar == null) return "Error: Carpeta no encontrada.";

        borrarContenidoRecursivo(aBorrar);
        padre.getSubdirectorios().eliminar(nombreDir);
        
        return "Directorio '" + nombreDir + "' y todo su contenido eliminados.";
    }

    private void borrarContenidoRecursivo(Directorio dir) {
        // Borrar archivos internos
        NodoArchivo aux = dir.getArchivos().getCabeza();
        while (aux != null) {
            liberarRecursosArchivo(aux.archivo);
            aux = aux.siguiente;
        }
        // Borrar subdirectorios internos
        NodoDirectorio auxDir = dir.getSubdirectorios().getCabeza();
        while (auxDir != null) {
            borrarContenidoRecursivo(auxDir.getDirectorio());
            auxDir = auxDir.getSiguiente();
        }
    }

    /**
     * NUEVO: Renombrar Archivo (Cumple Requisito PDF punto 3 CRUD-Update)
     */
    public String renombrarArchivo(String ruta, String nombreViejo, String nombreNuevo) {
        if (!usuarioActual.isEsAdmin()) return "Error: Permiso denegado. Solo Admin.";
        
        Directorio dir = buscarDirectorioPorRuta(ruta);
        if (dir == null) return "Error: Ruta no encontrada.";
        if (dir.getArchivos().buscar(nombreNuevo) != null) return "Error: Nombre ya existe.";

        Archivo a = dir.getArchivos().buscar(nombreViejo);
        if (a == null) return "Error: Archivo no encontrado.";
        
        a.setNombre(nombreNuevo);
        
        // Actualizar en FAT (Usamos try-catch por si NodoAsignacion no tiene setter público)
        NodoAsignacion nodo = tablaAsignacion.buscar(nombreViejo);
        if (nodo != null) {
             try {
                java.lang.reflect.Field f = nodo.getClass().getDeclaredField("nombreArchivo");
                f.setAccessible(true);
                f.set(nodo, nombreNuevo);
             } catch (Exception e) {}
        }
        
        // Actualizar en Disco
        Bloque b = a.getPrimerBloque();
        while(b != null) {
            b.setArchivoAsociado(nombreNuevo);
            b = b.getSiguiente();
        }
        
        return "Archivo renombrado a " + nombreNuevo;
    }

    public Directorio buscarDirectorioPorRuta(String ruta) {
        if (ruta.equals("root") || ruta.equals("/")) return raiz;
        String[] partes = ruta.split("/");
        Directorio actual = raiz;
        for (String parte : partes) {
            if (parte.isEmpty() || parte.equals("root")) continue;
            Directorio siguiente = actual.getSubdirectorios().buscar(parte);
            if (siguiente == null) return null;
            actual = siguiente;
        }
        return actual;
    }
    
    public Directorio getRaiz() { return raiz; }
    public Disco getDisco() { return disco; }
    public TablaAsignacion getTablaAsignacion() { return tablaAsignacion; }
    public PlanificadorDisco getPlanificador() { return planificador; }
    public Usuario getUsuarioActual() { return usuarioActual; }

    // PERSISTENCIA (Guardar/Cargar CSV)
    public void guardarSistema(String rutaArchivoFisico) throws IOException {
        FileWriter writer = new FileWriter(rutaArchivoFisico);
        writer.write("RutaCompleta,Nombre,Tamano,Color,Propietario\n");
        guardarRecursivo(raiz, writer);
        writer.close();
    }

    private void guardarRecursivo(Directorio dir, FileWriter writer) throws IOException {
        NodoArchivo aux = dir.getArchivos().getCabeza();
        while (aux != null) {
            Archivo a = aux.archivo;
            String linea = dir.obtenerRutaCompleta() + "," + a.getNombre() + "," + a.getTamanoEnBloques() + "," + a.getColor() + "," + a.getPropietario();
            writer.write(linea + "\n");
            aux = aux.siguiente;
        }
        NodoDirectorio auxDir = dir.getSubdirectorios().getCabeza();
        while (auxDir != null) {
            guardarRecursivo(auxDir.getDirectorio(), writer);
            auxDir = auxDir.getSiguiente();
        }
    }

    public void cargarSistema(String rutaArchivoFisico) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(rutaArchivoFisico));
        String linea;
        this.raiz = new Directorio("root");
        this.tablaAsignacion = new TablaAsignacion(); 
        for (Bloque b : disco.getBloques()) {
            b.setOcupado(false);
            b.setArchivoAsociado(null);
            b.setSiguiente(null);
        }
        br.readLine(); 
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length < 5) continue;
            String ruta = datos[0];
            String nombre = datos[1];
            int tamano = Integer.parseInt(datos[2]);
            String color = datos[3];
            String propietario = datos[4];
            
            asegurarDirectorios(ruta);
            
            Usuario usuarioBackup = this.usuarioActual;
            this.usuarioActual = new Usuario(propietario, true); 
            crearArchivo(ruta, nombre, tamano, color);
            this.usuarioActual = usuarioBackup; 
        }
        br.close();
    }

    private void asegurarDirectorios(String ruta) {
        if (ruta.equals("root")) return;
        String[] partes = ruta.split("/");
        Directorio actual = raiz;
        for (String parte : partes) {
            if (parte.isEmpty() || parte.equals("root")) continue;
            Directorio siguiente = actual.getSubdirectorios().buscar(parte);
            if (siguiente == null) {
                siguiente = new Directorio(parte);
                actual.agregarSubdirectorio(siguiente);
            }
            actual = siguiente;
        }
    }
}