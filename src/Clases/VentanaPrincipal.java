/*
 * VentanaPrincipal.java
 * Simulador de Sistema de Archivos con Interfaz Gráfica
 */
package Clases;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    // === CONEXIÓN CON EL BACKEND ===
    private SistemaArchivos sistema;

    // === COMPONENTES VISUALES ===
    private JTree arbolVisual;
    private DefaultTreeModel modeloArbol;
    
    private JPanel panelDisco; 
    private JButton[] botonesBloques; 
    
    private JTable tablaAsignacionVisual;
    private DefaultTableModel modeloTablaAsignacion;

    private JTable tablaProcesosVisual;
    private DefaultTableModel modeloTablaProcesos;

    // Colores para la interfaz
    private final Color COLOR_LIBRE = Color.LIGHT_GRAY;
    private final Color COLOR_OCUPADO_DEFECTO = Color.RED;

    public VentanaPrincipal() {
        // 1. Inicializar Backend (Disco de 64 bloques)
        sistema = new SistemaArchivos(64); 
        
        // Iniciar sesión como admin por defecto para pruebas
        Usuario admin = new Usuario("admin", true);
        sistema.iniciarSesion(admin);

        // 2. Configuración Ventana
        setTitle("Simulador SO - Gestión de Archivos y Procesos");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Centrar en pantalla

        // 3. Cargar componentes y dibujar estado inicial
        inicializarComponentes();
        actualizarTodo();
    }

    private void inicializarComponentes() {
        // --- PANEL IZQUIERDO: ÁRBOL DE DIRECTORIOS ---
        modeloArbol = new DefaultTreeModel(new DefaultMutableTreeNode("root"));
        arbolVisual = new JTree(modeloArbol);
        JScrollPane scrollArbol = new JScrollPane(arbolVisual);
        scrollArbol.setPreferredSize(new Dimension(200, 0));
        scrollArbol.setBorder(BorderFactory.createTitledBorder("Directorios"));
        add(scrollArbol, BorderLayout.WEST);

        // --- PANEL CENTRAL: DISCO Y TABLAS ---
        JPanel panelCentro = new JPanel(new GridLayout(3, 1, 5, 5));

        // 1. Simulación de Disco (SD) - Arriba
        panelDisco = new JPanel();
        int totalBloques = sistema.getDisco().getTotalBloques();
        int filas = (int) Math.sqrt(totalBloques); // Calcular filas para que sea cuadrado
        panelDisco.setLayout(new GridLayout(filas, 0, 2, 2));
        panelDisco.setBorder(BorderFactory.createTitledBorder("Simulación de Disco (SD)"));
        
        botonesBloques = new JButton[totalBloques];
        for (int i = 0; i < totalBloques; i++) {
            botonesBloques[i] = new JButton(String.valueOf(i));
            botonesBloques[i].setBackground(COLOR_LIBRE);
            botonesBloques[i].setOpaque(true);
            botonesBloques[i].setBorderPainted(false);
            botonesBloques[i].setFont(new Font("Arial", Font.PLAIN, 9));
            panelDisco.add(botonesBloques[i]);
        }
        panelCentro.add(panelDisco);

        // 2. Tabla de Asignación (FAT) - Medio
        String[] colAsig = {"Nombre", "Bloques", "Inicio", "Dueño", "Color"};
        modeloTablaAsignacion = new DefaultTableModel(colAsig, 0);
        tablaAsignacionVisual = new JTable(modeloTablaAsignacion);
        JScrollPane scrollTablaAsig = new JScrollPane(tablaAsignacionVisual);
        scrollTablaAsig.setBorder(BorderFactory.createTitledBorder("Tabla de Asignación (FAT)"));
        panelCentro.add(scrollTablaAsig);

        // 3. Cola de Procesos (Planificador) - Abajo
        String[] colProc = {"ID", "Operación", "Archivo", "Estado", "Usuario"};
        modeloTablaProcesos = new DefaultTableModel(colProc, 0);
        tablaProcesosVisual = new JTable(modeloTablaProcesos);
        JScrollPane scrollTablaProc = new JScrollPane(tablaProcesosVisual);
        scrollTablaProc.setBorder(BorderFactory.createTitledBorder("Cola de Procesos (Planificador)"));
        panelCentro.add(scrollTablaProc);

        add(panelCentro, BorderLayout.CENTER);

        // --- BARRA SUPERIOR: BOTONES DE ACCIÓN ---
        JPanel panelHerramientas = new JPanel();
        
        JButton btnLogin = new JButton("Cambiar Usuario");
        JButton btnSolicitarCrear = new JButton("Solicitar Crear Archivo");
        JButton btnSolicitarBorrar = new JButton("Solicitar Eliminar");
        JButton btnCrearCarpeta = new JButton("Crear Carpeta");
        String[] politicas = {"FIFO", "SSTF", "SCAN", "C-SCAN"};
JComboBox<String> comboPolitica = new JComboBox<>(politicas);
comboPolitica.setSelectedItem(sistema.getPlanificador().getPolitica()); // Predeterminado
comboPolitica.addActionListener(e -> {
    String seleccion = (String) comboPolitica.getSelectedItem();
    sistema.getPlanificador().setPolitica(seleccion);
    JOptionPane.showMessageDialog(this, "Política de planificación cambiada a: " + seleccion);
});
        
        // BOTÓN VERDE: Simula el CPU ejecutando el proceso
        JButton btnEjecutarPaso = new JButton("▶ EJECUTAR SIGUIENTE PROCESO");
        btnEjecutarPaso.setBackground(new Color(144, 238, 144)); 
        btnEjecutarPaso.setFont(new Font("Arial", Font.BOLD, 12));

        // Asignar Eventos
        btnLogin.addActionListener(e -> cambiarUsuario());
        btnSolicitarCrear.addActionListener(e -> abrirDialogoCrearArchivo());
        btnSolicitarBorrar.addActionListener(e -> abrirDialogoEliminar());
        btnCrearCarpeta.addActionListener(e -> abrirDialogoCrearCarpeta());
        btnEjecutarPaso.addActionListener(e -> procesarSiguienteEnCola());

        panelHerramientas.add(btnLogin);
        panelHerramientas.add(btnSolicitarCrear);
        panelHerramientas.add(btnSolicitarBorrar);
        panelHerramientas.add(btnCrearCarpeta);
        panelHerramientas.add(new JLabel("Política:"));
panelHerramientas.add(comboPolitica);
panelHerramientas.add(Box.createHorizontalStrut(10));
        panelHerramientas.add(Box.createHorizontalStrut(20)); // Separador
        panelHerramientas.add(btnEjecutarPaso);
        
        add(panelHerramientas, BorderLayout.NORTH);
    }

    // ==========================================================
    // LÓGICA DE SIMULACIÓN (Procesamiento de la Cola)
    // ==========================================================

    private void procesarSiguienteEnCola() {
        // 1. Pedir el siguiente proceso al planificador
        Proceso p = sistema.getPlanificador().siguienteProceso();
        
        if (p == null) {
            JOptionPane.showMessageDialog(this, "La cola de procesos está vacía.");
            return;
        }

        // 2. Preparar datos (Ruta y Color)
        // Usamos validaciones por si acaso vienen nulos
        String ruta = (p.getRutaDestino() != null) ? p.getRutaDestino() : "root";
        String color = (p.getColorAsociado() != null) ? p.getColorAsociado() : "Gris";
        String resultado = "";

        // 3. Ejecutar acción real
        switch (p.getTipoOperacion()) {
            case "CREAR":
                // Llamamos al sistema pasando ruta, nombre, tamaño y COLOR
                resultado = sistema.crearArchivo(ruta, p.getNombreArchivo(), p.getTamano(), color); 
                break;
                
            case "ELIMINAR":
                resultado = sistema.eliminarArchivo(ruta, p.getNombreArchivo());
                break;
                
            case "CREAR_DIR":
                 // Buscar la carpeta padre para meter la nueva carpeta
                 Directorio dirPadre = sistema.buscarDirectorioPorRuta(ruta);
                 if(dirPadre != null) {
                     // Validar duplicados
                     if (dirPadre.getSubdirectorios().buscar(p.getNombreArchivo()) == null) {
                         Directorio nuevo = new Directorio(p.getNombreArchivo());
                         dirPadre.agregarSubdirectorio(nuevo);
                         resultado = "Carpeta '" + p.getNombreArchivo() + "' creada exitosamente en " + ruta;
                     } else {
                         resultado = "Error: Ya existe esa carpeta.";
                     }
                 } else {
                     resultado = "Error: Ruta padre no encontrada.";
                 }
                break;
        }

        // 4. Finalizar Proceso y Actualizar GUI
        p.aTerminado();
        actualizarTodo(); // Refrescar visuales
        
        JOptionPane.showMessageDialog(this, 
            "Proceso " + p.getId() + " (" + p.getTipoOperacion() + ") Finalizado.\nResultado: " + resultado);
    }

    // ==========================================================
    // MÉTODOS DE ACTUALIZACIÓN (Backend -> Frontend)
    // ==========================================================

    public void actualizarTodo() {
        actualizarArbol();
        actualizarDiscoVisual();
        actualizarTablaAsignacion();
        actualizarColaProcesos();
    }

    private void actualizarColaProcesos() {
        modeloTablaProcesos.setRowCount(0); // Limpiar tabla
        ColaProcesos cola = sistema.getPlanificador().getCola();
        
        // Recorremos sin borrar usando el nodo frente
        NodoProceso actual = cola.getFrente();
        while (actual != null) {
            Proceso p = actual.proceso; // Asumiendo que el nodo tiene acceso público a proceso
            // O si el atributo es package-private en NodoProceso, esto funciona si estamos en mismo paquete
            Object[] fila = {
                p.getId(),
                p.getTipoOperacion(),
                p.getNombreArchivo(),
                p.getEstado(),
                p.getPropietario().getNombre()
            };
            modeloTablaProcesos.addRow(fila);
            actual = actual.siguiente;
        }
    }

    private void actualizarArbol() {
        Directorio raizLogica = sistema.getRaiz();
        DefaultMutableTreeNode raizVisual = new DefaultMutableTreeNode(raizLogica.getNombre());
        construirNodosRecursivo(raizVisual, raizLogica);
        modeloArbol.setRoot(raizVisual);
        modeloArbol.reload();
    }

    private void construirNodosRecursivo(DefaultMutableTreeNode nodoVisual, Directorio dirLogico) {
        // Archivos
        NodoArchivo auxArch = dirLogico.getArchivos().getCabeza();
        while (auxArch != null) {
            nodoVisual.add(new DefaultMutableTreeNode(auxArch.archivo.getNombre()));
            auxArch = auxArch.siguiente;
        }
        // Subdirectorios
        NodoDirectorio auxDir = dirLogico.getSubdirectorios().getCabeza();
        while (auxDir != null) {
            DefaultMutableTreeNode nodoSubDir = new DefaultMutableTreeNode(auxDir.getDirectorio().getNombre());
            nodoVisual.add(nodoSubDir);
            construirNodosRecursivo(nodoSubDir, auxDir.getDirectorio()); // Recursión
            auxDir = auxDir.getSiguiente();
        }
    }

    private void actualizarDiscoVisual() {
        Bloque[] bloquesFisicos = sistema.getDisco().getBloques();
        for (int i = 0; i < bloquesFisicos.length; i++) {
            if (bloquesFisicos[i].isOcupado()) {
                String nombre = bloquesFisicos[i].getArchivoAsociado();
                botonesBloques[i].setBackground(obtenerColorDeArchivo(nombre));
                botonesBloques[i].setToolTipText("Ocupado por: " + nombre);
            } else {
                botonesBloques[i].setBackground(COLOR_LIBRE);
                botonesBloques[i].setToolTipText("Libre");
            }
        }
    }

    private void actualizarTablaAsignacion() {
        modeloTablaAsignacion.setRowCount(0);
        NodoAsignacion aux = sistema.getTablaAsignacion().getCabeza();
        while (aux != null) {
            modeloTablaAsignacion.addRow(new Object[]{
                aux.getNombreArchivo(), aux.getCantidadBloques(), 
                aux.getPrimerBloque(), aux.getPropietario(), aux.getColor()
            });
            aux = aux.siguiente;
        }
    }

    private Color obtenerColorDeArchivo(String nombre) {
        NodoAsignacion nodo = sistema.getTablaAsignacion().buscar(nombre);
        if (nodo == null) return COLOR_OCUPADO_DEFECTO;
        String c = nodo.getColor().toLowerCase();
        
        switch(c) {
            case "rojo": return Color.RED;
            case "azul": return Color.BLUE;
            case "verde": return Color.GREEN;
            case "amarillo": return Color.YELLOW;
            case "naranja": return Color.ORANGE;
            case "magenta": return Color.MAGENTA;
            case "cyan": return Color.CYAN;
            default: return Color.GRAY;
        }
    }

    // ==========================================================
    // DIÁLOGOS Y EVENTOS
    // ==========================================================

    // Método Auxiliar: Llena el ComboBox con todas las carpetas disponibles
    private void llenarComboRutas(JComboBox<String> combo, Directorio dirActual) {
        combo.addItem(dirActual.obtenerRutaCompleta());
        NodoDirectorio aux = dirActual.getSubdirectorios().getCabeza();
        while (aux != null) {
            llenarComboRutas(combo, aux.getDirectorio());
            aux = aux.getSiguiente();
        }
    }

    private void abrirDialogoCrearArchivo() {
        JTextField campoNombre = new JTextField();
        JTextField campoTamano = new JTextField();
        
        // Selector de Ruta (Arreglo del problema de carpetas)
        JComboBox<String> comboRutas = new JComboBox<>();
        llenarComboRutas(comboRutas, sistema.getRaiz());
        
        String[] colores = {"Rojo", "Azul", "Verde", "Amarillo", "Naranja", "Magenta", "Cyan"};
        JComboBox<String> comboColor = new JComboBox<>(colores);

        Object[] mensaje = {
            "Seleccione Carpeta:", comboRutas,
            "Nombre Archivo (con extensión):", campoNombre,
            "Tamaño (bloques):", campoTamano,
            "Color:", comboColor
        };

        int op = JOptionPane.showConfirmDialog(this, mensaje, "Nuevo Proceso: Crear", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            try {
                String nombre = campoNombre.getText();
                int tamano = Integer.parseInt(campoTamano.getText());
                String ruta = (String) comboRutas.getSelectedItem();
                String color = (String) comboColor.getSelectedItem();
                
                int nuevoId = (int)(Math.random() * 1000);
                
                // Crear proceso y asignarle color y ruta
                Proceso p = new Proceso(nuevoId, "CREAR", nombre, sistema.getUsuarioActual(), tamano);
                p.setRutaDestino(ruta);
                p.setColorAsociado(color);
                
                sistema.getPlanificador().agregarProceso(p);
                
                JOptionPane.showMessageDialog(this, "Solicitud enviada a la cola (ID: " + nuevoId + ")");
                actualizarTodo();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El tamaño debe ser un número entero.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    
    private void abrirDialogoCrearCarpeta() {
        JTextField campoNombre = new JTextField();
        
        // Selector de Ruta Padre
        JComboBox<String> comboRutas = new JComboBox<>();
        llenarComboRutas(comboRutas, sistema.getRaiz());
        
        Object[] mensaje = {
            "Carpeta Padre:", comboRutas,
            "Nombre Nueva Carpeta:", campoNombre
        };

        int op = JOptionPane.showConfirmDialog(this, mensaje, "Nuevo Proceso: Crear Directorio", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            try {
                String nombre = campoNombre.getText();
                String ruta = (String) comboRutas.getSelectedItem();
                
                int nuevoId = (int)(Math.random() * 1000);
                
                Proceso p = new Proceso(nuevoId, "CREAR_DIR", nombre, sistema.getUsuarioActual(), 0);
                p.setRutaDestino(ruta);
                p.setColorAsociado("Gris"); // Color irrelevante para carpetas
                
                sistema.getPlanificador().agregarProceso(p);
                
                JOptionPane.showMessageDialog(this, "Solicitud enviada a la cola (ID: " + nuevoId + ")");
                actualizarTodo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear solicitud.");
            }
        }
    }
    
    private void abrirDialogoEliminar() {
    JTextField campoNombre = new JTextField();
    JComboBox<String> comboRutas = new JComboBox<>();
    llenarComboRutas(comboRutas, sistema.getRaiz());

    Object[] mensaje = { 
        "Ubicación del archivo:", comboRutas, 
        "Nombre a eliminar:", campoNombre 
    };

    int op = JOptionPane.showConfirmDialog(this, mensaje, "Eliminar Archivo", JOptionPane.OK_CANCEL_OPTION);
    if (op == JOptionPane.OK_OPTION) {
        String nombre = campoNombre.getText();
        String ruta = (String) comboRutas.getSelectedItem();

        if (nombre != null && !nombre.isEmpty()) {
            int nuevoId = (int)(Math.random() * 1000);

            // 🔹 Buscar primer bloque del archivo
            NodoAsignacion nodo = sistema.getTablaAsignacion().buscar(nombre);
            if (nodo != null) {
                Proceso p = new Proceso(nuevoId, "ELIMINAR", nombre, sistema.getUsuarioActual());
                p.setRutaDestino(ruta);
                p.setCilindro(nodo.getPrimerBloque()); // 🔑 clave para SSTF
                sistema.getPlanificador().agregarProceso(p);
                actualizarTodo();
                JOptionPane.showMessageDialog(this, "Solicitud enviada a la cola (ID: " + nuevoId + ")");
            } else {
                JOptionPane.showMessageDialog(this, "Archivo no encontrado en disco.");
            }
        }
    }
}

    
    private void cambiarUsuario() {
        String[] opciones = {"Administrador", "Usuario1", "Usuario2"};
        String seleccion = (String) JOptionPane.showInputDialog(this, "Seleccione Usuario:", 
                "Login", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                
        if (seleccion != null) {
            boolean esAdmin = seleccion.equals("Administrador");
            Usuario nuevoUsuario = new Usuario(seleccion, esAdmin);
            sistema.iniciarSesion(nuevoUsuario);
            JOptionPane.showMessageDialog(this, "Sesión iniciada como: " + seleccion);
        }
    }

    public static void main(String[] args) {
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}