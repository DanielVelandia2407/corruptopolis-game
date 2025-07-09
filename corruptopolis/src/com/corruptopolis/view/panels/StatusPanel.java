package com.corruptopolis.view.panels;

import com.corruptopolis.controller.GameController;
import com.corruptopolis.model.Game;
import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private GameController controller;
    private JLabel turnLabel;
    private JLabel statusLabel;

    public StatusPanel(GameController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBorder(BorderFactory.createTitledBorder("Estado"));

        turnLabel = new JLabel("Turno: 1");
        statusLabel = new JLabel("Iniciando...");

        add(turnLabel);
        add(Box.createHorizontalStrut(20));
        add(statusLabel);
    }

    public void updateStatus(Game game) {
        if (game != null) {
            turnLabel.setText("Turno: " + game.getCurrentTurn());
            statusLabel.setText(game.getGameStatus());
        }
    }
}
