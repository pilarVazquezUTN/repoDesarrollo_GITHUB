package Main;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// La clase App ahora es completamente orientada a la GUI
public class App {
    
    // El Scanner ya no es necesario, se elimina.
    // La función limpiarPantalla ya no es necesaria, se elimina.

    public static void main(String[] args) {
        // Ejecutar la GUI en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> mostrarMenuPrincipal());
    }

    // El antiguo método Menu() se convierte en la ventana principal
    public static void mostrarMenuPrincipal() {
        JFrame frameMenu = new JFrame("Menú Principal - Gestión de Húespedes");
        frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Al cerrar esta, la app termina
        frameMenu.setSize(400, 300);
        frameMenu.setLocationRelativeTo(null);
        
        // Panel principal con layout de columna (Grid con 6 filas)
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Margen
        
        // --- Creación de Botones ---
        JButton btnAutenticar = new JButton("1. Autenticar Usuario");
        JButton btnBuscar = new JButton("2. Buscar Huésped");
        JButton btnAlta = new JButton("3. Dar De Alta Huésped");
        JButton btnModificar = new JButton("4. Modificar Huésped");
        JButton btnBaja = new JButton("5. Dar de Baja Huésped");
        JButton btnSalir = new JButton("6. Salir");
        
        // --- Adición al Panel ---
        panel.add(btnAutenticar);
        panel.add(btnBuscar);
        panel.add(btnAlta);
        panel.add(btnModificar);
        panel.add(btnBaja);
        panel.add(btnSalir);

        // --- Configuración de Acciones (ActionListeners) ---
        
        // 1. Autenticar Usuario (Llama a la ventana de Login)
        btnAutenticar.addActionListener(e -> {
            mostrarLogin(frameMenu); // Pasamos el frameMenu para ocultarlo/mostrarlo
        });
        
        // 2. Buscar Húesped (Ejemplo de función simple)
        btnBuscar.addActionListener(e -> {
            JOptionPane.showMessageDialog(frameMenu, "Función: Buscar Húesped (Pendiente)", "Opción 2", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // 3. Dar De Alta Húesped (Ejemplo de función simple)
        btnAlta.addActionListener(e -> {
            JOptionPane.showMessageDialog(frameMenu, "Función: Dar De Alta Húesped (Pendiente)", "Opción 3", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // 4. Modificar Húesped (Ejemplo de función simple)
        btnModificar.addActionListener(e -> {
            JOptionPane.showMessageDialog(frameMenu, "Función: Modificar Húesped (Pendiente)", "Opción 4", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // 5. Dar de Baja Húesped (Ejemplo de función simple)
        btnBaja.addActionListener(e -> {
            JOptionPane.showMessageDialog(frameMenu, "Función: Dar de Baja Huésped (Pendiente)", "Opción 5", JOptionPane.INFORMATION_MESSAGE);
        });

        // 6. Salir
        btnSalir.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(frameMenu, 
                "¿Está seguro que desea salir de la aplicación?", "Confirmar Salida", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        frameMenu.add(panel);
        frameMenu.setVisible(true);
    }
    
    // Método que recibe el frame principal para saber a dónde volver
    public static void mostrarLogin(JFrame frameMenuPrincipal) {
        // Ocultar el menú principal mientras se muestra el login
        frameMenuPrincipal.setVisible(false);
        
        JFrame frameLogin = new JFrame("Autenticación de Usuario");
        frameLogin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frameLogin.setSize(350, 200);
        frameLogin.setLocationRelativeTo(null);

        JLabel labelNombre = new JLabel("Nombre:");
        JTextField campoNombre = new JTextField(15);
        JLabel labelContrasena = new JLabel("Contraseña:");
        JPasswordField campoContrasena = new JPasswordField(15);
        JButton botonAceptar = new JButton("Ingresar");
        JButton botonCancelar = new JButton("Cancelar");

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(labelNombre);
        panel.add(campoNombre);
        panel.add(labelContrasena);
        panel.add(campoContrasena);
        panel.add(botonAceptar);
        panel.add(botonCancelar);

        frameLogin.add(panel);
        frameLogin.setVisible(true);

        // Acción del botón Aceptar
        botonAceptar.addActionListener(e -> {
            String nombre = campoNombre.getText();
            String contrasena = new String(campoContrasena.getPassword()); 
            
            if (nombre.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(frameLogin, "Complete ambos campos.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return; 
            } 

            if (buscarNombre(nombre, contrasena)) {
                // Éxito:
                JOptionPane.showMessageDialog(frameLogin, "✅ Autenticación exitosa!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Cerrar la ventana de login y mostrar el menú principal
                frameLogin.dispose(); 
                frameMenuPrincipal.setVisible(true); 

            } else {
                // Fallo:
                JOptionPane.showMessageDialog(frameLogin, "❌ Usuario o contraseña incorrectos. Intente de nuevo.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                campoContrasena.setText(""); 
            }
        });

        // Acción del botón Cancelar
        botonCancelar.addActionListener(e -> {
            frameLogin.dispose();
            frameMenuPrincipal.setVisible(true); // Vuelve al menú principal al cerrar la ventana
        });
        
        // Manejar el cierre de la ventana con la "X"
        frameLogin.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                frameMenuPrincipal.setVisible(true);
            }
        });
    }

    // La función de autenticación se mantiene igual, pero ahora usa JOptionPane para errores
    public static boolean buscarNombre(String nombreUsuario, String contraseñaUsuario) {
        // **IMPORTANTE:** Reemplaza esta ruta por una ruta válida en tu sistema.
        String rutaArchivo = "D:/UTN/2025/DesarrolloSoftware/trabajoPractico/infoAutenticarHuesped.txt";
        boolean autenticado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    String usuario = partes[0].trim();
                    String contrasena = partes[1].trim(); 

                    if (usuario.equalsIgnoreCase(nombreUsuario) &&
                        contrasena.equals(contraseñaUsuario)) {
                        autenticado = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de autenticación: " + e.getMessage(), 
                                         "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }

        return autenticado;
    }
}