package com.corruptopolis;

import com.corruptopolis.controller.GameController;
import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase principal que inicia la aplicación Corruptópolis
 * Configura el entorno gráfico y lanza el controlador principal
 */
public class Main {

    /**
     * Método principal de entrada de la aplicación
     */
    public static void main(String[] args) {
        // Configurar propiedades del sistema para mejor rendimiento gráfico
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.acceleration", "true");
        System.setProperty("sun.java2d.trace", "timestamp,log,count,out:java2d.log");

        // Configurar Look and Feel antes de crear componentes
        SwingUtilities.invokeLater(() -> {
            try {
                configureLookAndFeel();
                initializeApplication();
            } catch (Exception e) {
                showErrorAndExit("Error al inicializar la aplicación", e);
            }
        });
    }

    /**
     * Configura el Look and Feel de la aplicación
     */
    private static void configureLookAndFeel() {
        try {
            // Intentar usar el Look and Feel del sistema
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            // Configuraciones adicionales de UI
            UIManager.put("ToolTip.background", new Color(255, 255, 225));
            UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Color.BLACK));
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));

        } catch (Exception e) {
            System.out.println("No se pudo configurar el Look and Feel del sistema, usando el predeterminado");
        }
    }

    /**
     * Inicializa la aplicación mostrando la pantalla de bienvenida
     */
    private static void initializeApplication() {
        // Mostrar splash screen
        showSplashScreen();

        // Mostrar diálogo de inicio
        String playerName = showWelcomeDialog();

        if (playerName != null && !playerName.trim().isEmpty()) {
            // Crear y iniciar el controlador del juego
            GameController controller = new GameController();
            controller.startNewGame(playerName.trim());
        } else {
            System.exit(0);
        }
    }

    /**
     * Muestra la pantalla de bienvenida temporal
     */
    private static void showSplashScreen() {
        JWindow splashWindow = new JWindow();

        // Panel principal del splash
        JPanel splashPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(45, 45, 45),
                        getWidth(), getHeight(), new Color(25, 25, 25)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Título principal
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 36));
                FontMetrics fm = g2d.getFontMetrics();
                String title = "CORRUPTÓPOLIS";
                int titleX = (getWidth() - fm.stringWidth(title)) / 2;
                g2d.drawString(title, titleX, 150);

                // Subtítulo
                g2d.setFont(new Font("Arial", Font.ITALIC, 16));
                fm = g2d.getFontMetrics();
                String subtitle = "El Ascenso y Caída en el Laberinto del Poder";
                int subtitleX = (getWidth() - fm.stringWidth(subtitle)) / 2;
                g2d.drawString(subtitle, subtitleX, 180);

                // Versión
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                g2d.setColor(Color.LIGHT_GRAY);
                fm = g2d.getFontMetrics();
                String version = "Versión 1.0 - Proyecto Académico";
                int versionX = (getWidth() - fm.stringWidth(version)) / 2;
                g2d.drawString(version, versionX, 220);

                // Barra de carga simulada
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(50, 280, getWidth() - 100, 20);
                g2d.setColor(new Color(0, 150, 0));
                g2d.fillRect(50, 280, (int)((getWidth() - 100) * 0.8), 20);

                // Texto de carga
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                fm = g2d.getFontMetrics();
                String loading = "Inicializando estructuras de datos...";
                int loadingX = (getWidth() - fm.stringWidth(loading)) / 2;
                g2d.drawString(loading, loadingX, 320);
            }
        };

        splashPanel.setPreferredSize(new Dimension(500, 350));
        splashWindow.add(splashPanel);
        splashWindow.pack();
        splashWindow.setLocationRelativeTo(null);
        splashWindow.setVisible(true);

        // Simular tiempo de carga
        Timer splashTimer = new Timer(2500, e -> {
            splashWindow.dispose();
            ((Timer) e.getSource()).stop();
        });
        splashTimer.setRepeats(false);
        splashTimer.start();

        // Esperar a que termine el splash
        try {
            Thread.sleep(2600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Muestra el diálogo de bienvenida para obtener el nombre del jugador
     */
    private static String showWelcomeDialog() {
        // Panel personalizado para el diálogo
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.setPreferredSize(new Dimension(500, 400));

        // Texto de bienvenida
        JTextArea welcomeText = new JTextArea(
                "¡Bienvenido a Corruptópolis!\n\n" +
                        "En este simulador estratégico, encarnarás a un político ambicioso " +
                        "que debe construir una red de corrupción mientras evade la justicia.\n\n" +
                        "Tu objetivo es acumular poder e influencia, gestionar sobornos, " +
                        "neutralizar amenazas y mantener tu imperio corrupto en las sombras.\n\n" +
                        "¿Podrás alcanzar la cima del poder sin ser descubierto?\n\n" +
                        "Ingresa el nombre de tu personaje para comenzar:"
        );
        welcomeText.setEditable(false);
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 12));
        welcomeText.setBackground(UIManager.getColor("Panel.background"));
        welcomeText.setLineWrap(true);
        welcomeText.setWrapStyleWord(true);

        JScrollPane textScrollPane = new JScrollPane(welcomeText);
        textScrollPane.setPreferredSize(new Dimension(450, 300));

        // Campo de entrada
        JTextField nameField = new JTextField("El Padrino");
        nameField.setFont(new Font("Arial", Font.BOLD, 14));
        nameField.selectAll();

        panel.add(textScrollPane, BorderLayout.CENTER);
        panel.add(nameField, BorderLayout.SOUTH);

        panel.add(welcomeText, BorderLayout.CENTER);
        panel.add(nameField, BorderLayout.SOUTH);

        // Mostrar diálogo
        int option = JOptionPane.showConfirmDialog(
                null, panel, "Bienvenido a Corruptópolis",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            return name.isEmpty() ? "El Padrino" : name;
        }

        return null;
    }

    /**
     * Muestra un error crítico y termina la aplicación
     */
    private static void showErrorAndExit(String message, Exception e) {
        String errorMessage = message + "\n\nDetalle del error:\n" + e.getMessage();

        JTextArea errorArea = new JTextArea(errorMessage);
        errorArea.setEditable(false);
        errorArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(errorArea);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        JOptionPane.showMessageDialog(
                null, scrollPane, "Error Crítico", JOptionPane.ERROR_MESSAGE
        );

        System.err.println("Error crítico en Corruptópolis:");
        e.printStackTrace();
        System.exit(1);
    }

    /**
     * Maneja errores no capturados en el Event Dispatch Thread
     */
    static {
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            showErrorAndExit("Error no manejado en el hilo: " + thread.getName(),
                    new RuntimeException(exception));
        });

        // Manejador específico para el EDT
        System.setProperty("sun.awt.exception.handler",
                "com.corruptopolis.Main$EDTExceptionHandler");
    }

    /**
     * Manejador de excepciones para el Event Dispatch Thread
     */
    public static class EDTExceptionHandler {
        public void handle(Throwable throwable) {
            showErrorAndExit("Error en el Event Dispatch Thread",
                    new RuntimeException(throwable));
        }
    }
}