package com.corruptopolis.app;

import com.corruptopolis.model.PlayerResources;
import com.corruptopolis.controller.ResourcesController;
import com.corruptopolis.view.ResourcesPanel;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // 1) Inicializar modelo y controlador
        PlayerResources player = new PlayerResources(1000.0, 50.0);
        ResourcesController controller = new ResourcesController();

        // 2) Crear la vista y pasarle referencias
        ResourcesPanel panel = new ResourcesPanel();
        panel.updateResources(
            player.getDirtyMoney(),
            player.getInfluence(),
            player.getSuspicionLevel()
        );

        // 3) Registrar listeners en los botones
        panel.getBtnBribe().addActionListener(e -> {
            // Aquí llamar a controller.bribe(...) con un nodo de prueba
            // Por ahora, usaremos un nodo simulado:
            // node debería venir luego de tu árbol; por ahora null a modo de ejemplo
            //dummynode es un nombre temporal, reemplazar por un nodo real
            boolean success = controller.bribe(dummyNode, player);
            if (!success) {
                JOptionPane.showMessageDialog(panel,
                    "Insufficient funds to bribe!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            }
            panel.updateResources(
                player.getDirtyMoney(),
                player.getInfluence(),
                player.getSuspicionLevel()
            );
        });

        panel.getBtnExtract().addActionListener(e -> {
            controller.extractResources(dummyNode, player);
            panel.updateResources(
                player.getDirtyMoney(),
                player.getInfluence(),
                player.getSuspicionLevel()
            );
        });

        // 4) Montar el frame
        JFrame frame = new JFrame("Corruptópolis - Demo Recursos");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // TODO: Reemplazar dummyNode por un Node real de tu árbol cuando esté listo
    private static final com.corruptopolis.model.Node dummyNode =
        new com.corruptopolis.model.Node("n1", "Test Node", "Mayor", 100.0);
}
