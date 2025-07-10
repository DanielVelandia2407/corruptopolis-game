package com.corruptopolis.view.panels;

import com.corruptopolis.controller.GameController;
import com.corruptopolis.model.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionPanel extends JPanel {
    private GameController controller;
    private JButton nextTurnButton;
    private JButton extractResourcesButton;
    private JButton coverUpButton;
    private JButton launderButton;

    public ActionPanel(GameController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Acciones"));
        setPreferredSize(new Dimension(200, 0));

        // Botón siguiente turno
        nextTurnButton = new JButton("Siguiente Turno");
        nextTurnButton.addActionListener(e -> controller.handleNextTurn());

        // Botón extraer recursos
        extractResourcesButton = new JButton("Extraer Recursos");
        extractResourcesButton.addActionListener(e -> controller.handleExtractResources());

        // Botón encubrimiento
        coverUpButton = new JButton("Encubrimiento");
        coverUpButton.addActionListener(e -> showCoverUpDialog());

        // Botón lavado
        launderButton = new JButton("Lavar Dinero");
        launderButton.addActionListener(e -> showLaunderDialog());

        // Agregar botones
        add(nextTurnButton);
        add(Box.createVerticalStrut(10));
        add(extractResourcesButton);
        add(Box.createVerticalStrut(10));
        add(coverUpButton);
        add(Box.createVerticalStrut(10));
        add(launderButton);
        add(Box.createVerticalGlue());
    }

    public void updateActions(Game game) {
        boolean gameActive = game != null && game.isGameActive();
        boolean canPerformActions = gameActive && (game.getActionsThisTurn() < game.getMaxActionsPerTurn());
        
        nextTurnButton.setEnabled(gameActive);
        extractResourcesButton.setEnabled(canPerformActions);
        coverUpButton.setEnabled(canPerformActions);
        launderButton.setEnabled(canPerformActions);
        
        // Cambiar texto del botón si no se pueden realizar más acciones
        if (!canPerformActions && gameActive) {
            extractResourcesButton.setText("Sin Acciones");
            coverUpButton.setText("Sin Acciones");
            launderButton.setText("Sin Acciones");
        } else {
            extractResourcesButton.setText("Extraer Recursos");
            coverUpButton.setText("Encubrimiento");
            launderButton.setText("Lavar Dinero");
        }
    }

    private void showCoverUpDialog() {
        String input = JOptionPane.showInputDialog(this, "Cantidad para encubrimiento:");
        try {
            int amount = Integer.parseInt(input);
            controller.handleCoverUpOperation(amount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
        }
    }

    private void showLaunderDialog() {
        String input = JOptionPane.showInputDialog(this, "Cantidad a lavar:");
        try {
            int amount = Integer.parseInt(input);
            controller.handleMoneyLaundering(amount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
        }
    }
}
