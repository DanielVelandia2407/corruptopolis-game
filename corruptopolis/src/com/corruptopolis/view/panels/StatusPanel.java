package com.corruptopolis.view.panels;

import com.corruptopolis.controller.GameController;
import com.corruptopolis.model.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusPanel extends JPanel {
    private GameController controller;
    private JLabel turnLabel;
    private JLabel statusLabel;
    private JLabel actionsLabel;
    private JLabel timeLabel;
    private Timer updateTimer;

    public StatusPanel(GameController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBorder(BorderFactory.createTitledBorder("Estado"));

        turnLabel = new JLabel("Turno: 1");
        statusLabel = new JLabel("Iniciando...");
        actionsLabel = new JLabel("Acciones: 0/4");
        timeLabel = new JLabel("Tiempo: 10s");

        add(turnLabel);
        add(Box.createHorizontalStrut(10));
        add(actionsLabel);
        add(Box.createHorizontalStrut(10));
        add(timeLabel);
        add(Box.createHorizontalStrut(20));
        add(statusLabel);
        
        // Timer para actualizar tiempo restante
        updateTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimeDisplay();
            }
        });
        updateTimer.start();
    }

    public void updateStatus(Game game) {
        if (game != null) {
            turnLabel.setText("Turno: " + game.getCurrentTurn());
            statusLabel.setText(game.getGameStatus());
            actionsLabel.setText("Acciones: " + game.getActionsThisTurn() + "/" + game.getMaxActionsPerTurn());
            
            // Cambiar color según acciones restantes
            if (game.getActionsThisTurn() >= game.getMaxActionsPerTurn()) {
                actionsLabel.setForeground(Color.RED);
            } else if (game.getActionsThisTurn() >= 3) {
                actionsLabel.setForeground(Color.ORANGE);
            } else {
                actionsLabel.setForeground(Color.BLACK);
            }
        }
    }
    
    private void updateTimeDisplay() {
        if (controller.hasActiveGame()) {
            int remainingTime = controller.getGameModel().getRemainingTurnTime();
            timeLabel.setText("Tiempo: " + remainingTime + "s");
        } else {
            timeLabel.setText("Tiempo: --s");
        }
    }
}
