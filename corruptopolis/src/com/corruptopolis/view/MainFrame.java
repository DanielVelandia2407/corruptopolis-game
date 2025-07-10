package com.corruptopolis.view;

import com.corruptopolis.controller.GameController;
import com.corruptopolis.model.Game;
import com.corruptopolis.view.panels.*;
import com.corruptopolis.view.panels.NetworkGraphPanel;
import com.corruptopolis.view.dialogs.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Ventana principal de la aplicación que contiene todos los paneles del juego
 * Implementa la vista en el patrón MVC
 */
public class MainFrame extends JFrame {
    private GameController controller;

    // Paneles principales
    private TreePanel treePanel;
    private ResourcePanel resourcePanel;
    private ActionPanel actionPanel;
    private StatusPanel statusPanel;
    private GamePanel gamePanel;

    // Componentes de la interfaz
    private JMenuBar menuBar;
    private JLabel statusLabel;
    private JProgressBar suspicionBar;
    private Timer animationTimer;

    /**
     * Constructor que inicializa la interfaz gráfica
     */
    public MainFrame(GameController controller) {
        this.controller = controller;
        initializeFrame();
        createMenuBar();
        createMainLayout();
        setupEventHandlers();
        applyTheme();
    }

    /**
     * Inicializa la configuración básica del frame
     */
    private void initializeFrame() {
        setTitle("Corruptópolis - El Ascenso y Caída en el Laberinto del Poder");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 800));

        // Icono de la aplicación
        try {
            // setIconImage(ImageIO.read(getClass().getResource("/images/icon.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono");
        }
    }

    /**
     * Crea la barra de menú
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();

        // Menú Juego
        JMenu gameMenu = new JMenu("Juego");
        gameMenu.add(createMenuItem("Nuevo Juego", "NEW_GAME"));
        gameMenu.add(createMenuItem("Selector de Niveles", "LEVEL_SELECTION"));
        gameMenu.addSeparator();
        gameMenu.add(createMenuItem("Guardar", "SAVE_GAME"));
        gameMenu.add(createMenuItem("Cargar", "LOAD_GAME"));
        gameMenu.addSeparator();
        gameMenu.add(createMenuItem("Reiniciar", "RESTART_GAME"));
        gameMenu.addSeparator();
        gameMenu.add(createMenuItem("Salir", "EXIT"));

        // Menú Acciones
        JMenu actionsMenu = new JMenu("Acciones");
        actionsMenu.add(createMenuItem("Siguiente Turno", "NEXT_TURN"));
        actionsMenu.add(createMenuItem("Extraer Recursos", "EXTRACT_RESOURCES"));
        actionsMenu.add(createMenuItem("Operación Encubrimiento", "COVER_UP"));
        actionsMenu.add(createMenuItem("Lavar Dinero", "LAUNDER_MONEY"));

        // Menú Vista
        JMenu viewMenu = new JMenu("Vista");
        viewMenu.add(createMenuItem("Mostrar Estadísticas", "SHOW_STATS"));
        viewMenu.add(createMenuItem("Mostrar Red Completa", "SHOW_NETWORK"));
        viewMenu.add(createMenuItem("Modo Pantalla Completa", "TOGGLE_FULLSCREEN"));

        // Menú Ayuda
        JMenu helpMenu = new JMenu("Ayuda");
        helpMenu.add(createMenuItem("Manual de Usuario", "HELP"));
        helpMenu.add(createMenuItem("Acerca de", "ABOUT"));

        menuBar.add(gameMenu);
        menuBar.add(actionsMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Crea un elemento de menú con action command
     */
    private JMenuItem createMenuItem(String text, String actionCommand) {
        JMenuItem item = new JMenuItem(text);
        item.setActionCommand(actionCommand);
        item.addActionListener(new MenuActionListener());
        return item;
    }

    /**
     * Crea el layout principal de la aplicación
     */
    private void createMainLayout() {
        setLayout(new BorderLayout());

        // Panel superior - Recursos y estado
        JPanel topPanel = new JPanel(new BorderLayout());
        resourcePanel = new ResourcePanel(controller);
        statusPanel = new StatusPanel(controller);

        topPanel.add(resourcePanel, BorderLayout.WEST);
        topPanel.add(statusPanel, BorderLayout.EAST);

        // Panel central - Pestañas con árbol, grafo y panel de juego
        JTabbedPane centerTabs = new JTabbedPane();
        
        treePanel = new TreePanel(controller);
        gamePanel = new GamePanel(controller);
        NetworkGraphPanel networkPanel = new NetworkGraphPanel(controller);
        
        centerTabs.addTab("Jerarquía", treePanel);
        centerTabs.addTab("Red de Contactos", networkPanel);
        
        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerTabs, gamePanel);
        centerSplitPane.setDividerLocation(600);
        centerSplitPane.setResizeWeight(0.6);

        // Panel derecho - Acciones
        actionPanel = new ActionPanel(controller);

        // Panel inferior - Barra de estado
        JPanel bottomPanel = createBottomPanel();

        // Ensamblar layout principal
        add(topPanel, BorderLayout.NORTH);
        add(centerSplitPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel inferior con barra de estado
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEtchedBorder());

        statusLabel = new JLabel("Iniciando Corruptópolis...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Barra de sospecha
        suspicionBar = new JProgressBar(0, 100);
        suspicionBar.setStringPainted(true);
        suspicionBar.setString("Nivel de Sospecha: 0%");
        suspicionBar.setForeground(Color.ORANGE);
        suspicionBar.setPreferredSize(new Dimension(200, 25));

        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(suspicionBar, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * Configura los manejadores de eventos
     */
    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });

        // Timer para animaciones
        animationTimer = new Timer(100, new AnimationListener());
    }

    /**
     * Aplica el tema visual del juego
     */
    private void applyTheme() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el tema del sistema");
        }

        // Colores personalizados
        getContentPane().setBackground(new Color(45, 45, 45));

        // Configurar colores de la barra de sospecha
        suspicionBar.setBackground(new Color(60, 60, 60));
        suspicionBar.setBorder(BorderFactory.createEtchedBorder());
    }

    /**
     * Actualiza la pantalla con el estado actual del juego
     */
    public void updateDisplay(Game gameModel) {
        if (gameModel == null) return;

        // Actualizar paneles
        resourcePanel.updateResources(gameModel.getPlayer());
        statusPanel.updateStatus(gameModel);
        treePanel.updateTree(gameModel.getCorruptionTree());
        gamePanel.updateGameArea(gameModel);
        actionPanel.updateActions(gameModel);
        
        // Actualizar panel de red
        Component selectedTab = ((JTabbedPane)((JSplitPane)getContentPane().getComponent(1)).getLeftComponent()).getSelectedComponent();
        if (selectedTab instanceof NetworkGraphPanel) {
            ((NetworkGraphPanel) selectedTab).updateGraph(gameModel);
        }

        // Actualizar barra de sospecha
        int suspicionLevel = gameModel.getPlayer().getGeneralSuspicionLevel();
        suspicionBar.setValue(suspicionLevel);
        suspicionBar.setString("Nivel de Sospecha: " + suspicionLevel + "%");

        // Cambiar color según el nivel de peligro
        if (suspicionLevel > 80) {
            suspicionBar.setForeground(Color.RED);
        } else if (suspicionLevel > 50) {
            suspicionBar.setForeground(Color.ORANGE);
        } else {
            suspicionBar.setForeground(Color.GREEN);
        }

        // Actualizar estado
        statusLabel.setText(gameModel.getGameStatus());

        // Actualizar título con información del turno
        setTitle(String.format("Corruptópolis - Turno %d - %s",
                gameModel.getCurrentTurn(),
                gameModel.getPlayer().getName()));
    }

    /**
     * Muestra un mensaje al usuario
     */
    public void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra información detallada de un nodo
     */
    public void showNodeInfo(String nodeId) {
        NodeInfoDialog dialog = new NodeInfoDialog(this, controller, nodeId);
        dialog.setVisible(true);
    }

    /**
     * Muestra la pantalla de fin de juego
     */
    public void showGameOver(int finalScore) {
        String message = String.format(
                "¡Juego Terminado!\n\nPuntaje Final: %d\n\n¿Deseas jugar de nuevo?",
                finalScore
        );

        int option = JOptionPane.showConfirmDialog(
                this, message, "Fin del Juego",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            showNewGameDialog();
        }
    }

    /**
     * Muestra el diálogo de nuevo juego
     */
    private void showNewGameDialog() {
        controller.startNewGame();
    }

    /**
     * Reproduce animación de éxito
     */
    public void playSuccessAnimation() {
        // Efecto visual de éxito
        Timer successTimer = new Timer(50, new ActionListener() {
            private int counter = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (counter < 6) {
                    getContentPane().setBackground(counter % 2 == 0 ? Color.GREEN : new Color(45, 45, 45));
                    repaint();
                    counter++;
                } else {
                    ((Timer) e.getSource()).stop();
                    getContentPane().setBackground(new Color(45, 45, 45));
                    repaint();
                }
            }
        });
        successTimer.start();
    }

    /**
     * Reproduce animación de fallo
     */
    public void playFailureAnimation() {
        // Efecto visual de fallo
        Timer failureTimer = new Timer(50, new ActionListener() {
            private int counter = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (counter < 6) {
                    getContentPane().setBackground(counter % 2 == 0 ? Color.RED : new Color(45, 45, 45));
                    repaint();
                    counter++;
                } else {
                    ((Timer) e.getSource()).stop();
                    getContentPane().setBackground(new Color(45, 45, 45));
                    repaint();
                }
            }
        });
        failureTimer.start();
    }

    /**
     * Resalta notificaciones de eventos
     */
    public void highlightEventNotification() {
        statusLabel.setForeground(Color.YELLOW);
        Timer highlightTimer = new Timer(2000, e -> {
            statusLabel.setForeground(Color.BLACK);
            ((Timer) e.getSource()).stop();
        });
        highlightTimer.start();
    }

    /**
     * Actualiza el contador de turnos
     */
    public void updateTurnCounter() {
        // Efecto visual para nuevo turno
        Timer turnTimer = new Timer(100, new ActionListener() {
            private int alpha = 255;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (alpha > 100) {
                    // Efecto de fade
                    alpha -= 15;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        turnTimer.start();
    }

    /**
     * Maneja el cierre de la ventana
     */
    private void handleWindowClosing() {
        int option = JOptionPane.showConfirmDialog(
                this, "¿Estás seguro de que deseas salir?",
                "Confirmar Salida", JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            controller.handleApplicationExit();
        }
    }

    /**
     * Manejador de acciones del menú
     */
    private class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "NEW_GAME":
                    showNewGameDialog();
                    break;
                case "LEVEL_SELECTION":
                    controller.showLevelSelection();
                    break;
                case "SAVE_GAME":
                    controller.saveGame();
                    break;
                case "LOAD_GAME":
                    controller.loadGame();
                    break;
                case "RESTART_GAME":
                    controller.restartGame();
                    break;
                case "EXIT":
                    handleWindowClosing();
                    break;
                case "NEXT_TURN":
                    controller.handleNextTurn();
                    break;
                case "EXTRACT_RESOURCES":
                    controller.handleExtractResources();
                    break;
                case "COVER_UP":
                    showCoverUpDialog();
                    break;
                case "LAUNDER_MONEY":
                    showLaunderDialog();
                    break;
                case "SHOW_STATS":
                    showStatsDialog();
                    break;
                case "HELP":
                    showHelpDialog();
                    break;
                case "ABOUT":
                    showAboutDialog();
                    break;
            }
        }
    }

    /**
     * Muestra diálogo de operación de encubrimiento
     */
    private void showCoverUpDialog() {
        String input = JOptionPane.showInputDialog(
                this, "Cantidad a invertir en encubrimiento:",
                "Operación de Encubrimiento", JOptionPane.QUESTION_MESSAGE
        );

        try {
            int amount = Integer.parseInt(input);
            controller.handleCoverUpOperation(amount);
        } catch (NumberFormatException e) {
            showMessage("Cantidad inválida", "Error");
        }
    }

    /**
     * Muestra diálogo de lavado de dinero
     */
    private void showLaunderDialog() {
        String input = JOptionPane.showInputDialog(
                this, "Cantidad de dinero a lavar:",
                "Lavado de Dinero", JOptionPane.QUESTION_MESSAGE
        );

        try {
            int amount = Integer.parseInt(input);
            controller.handleMoneyLaundering(amount);
        } catch (NumberFormatException e) {
            showMessage("Cantidad inválida", "Error");
        }
    }

    /**
     * Muestra diálogo de estadísticas
     */
    private void showStatsDialog() {
        String stats = controller.getGameStats();
        JOptionPane.showMessageDialog(this, stats, "Estadísticas del Juego", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra diálogo de ayuda
     */
    private void showHelpDialog() {
        String helpText = "CORRUPTÓPOLIS - MANUAL BÁSICO\n\n" +
                "OBJETIVO: Construir una red de corrupción sin ser capturado\n\n" +
                "ACCIONES PRINCIPALES:\n" +
                "• Sobornar contactos para expandir tu red\n" +
                "• Extraer recursos de tus subordinados\n" +
                "• Realizar operaciones de encubrimiento\n" +
                "• Lavar dinero para convertirlo en limpio\n\n" +
                "PELIGROS:\n" +
                "• Nivel de sospecha: Si llega a 100%, pierdes\n" +
                "• Traiciones de subordinados desleales\n" +
                "• Investigaciones federales\n" +
                "• Filtraciones a los medios\n\n" +
                "RECURSOS:\n" +
                "• Dinero Sucio: Para sobornos y operaciones\n" +
                "• Influencia: Para acceder a contactos de alto nivel\n" +
                "• Reputación: Protege contra investigaciones";

        JTextArea textArea = new JTextArea(helpText);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Ayuda", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra diálogo acerca de
     */
    private void showAboutDialog() {
        String aboutText = "CORRUPTÓPOLIS\n" +
                "El Ascenso y Caída en el Laberinto del Poder\n\n" +
                "Versión 1.0\n\n" +
                "Un simulador estratégico avanzado de corrupción política\n" +
                "que implementa estructuras de datos complejas para\n" +
                "ilustrar las mecánicas de redes de poder.\n\n" +
                "Desarrollado como proyecto académico\n" +
                "Propósito educativo y de entretenimiento\n\n" +
                "© 2025 - Todos los derechos reservados";

        JOptionPane.showMessageDialog(this, aboutText, "Acerca de Corruptópolis", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Manejador de animaciones
     */
    private class AnimationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica de animaciones continuas
            repaint();
        }
    }
}
