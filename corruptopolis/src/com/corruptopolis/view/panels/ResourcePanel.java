package com.corruptopolis.view.panels;

import com.corruptopolis.controller.GameController;
import com.corruptopolis.model.Player;
import javax.swing.*;
import java.awt.*;

public class ResourcePanel extends JPanel {
    private GameController controller;
    private JLabel dirtyMoneyLabel;
    private JLabel influenceLabel;
    private JLabel reputationLabel;

    public ResourcePanel(GameController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Recursos"));

        dirtyMoneyLabel = new JLabel("Dinero Sucio: $0");
        influenceLabel = new JLabel("Influencia: 0");
        reputationLabel = new JLabel("Reputación: 0%");

        add(dirtyMoneyLabel);
        add(Box.createHorizontalStrut(20));
        add(influenceLabel);
        add(Box.createHorizontalStrut(20));
        add(reputationLabel);
    }

    public void updateResources(Player player) {
        if (player != null) {
            dirtyMoneyLabel.setText("Dinero Sucio: $" + player.getDirtyMoney());
            influenceLabel.setText("Influencia: " + player.getAccumulatedInfluence());
            reputationLabel.setText("Reputación: " + player.getPublicReputation() + "%");
        }
    }
}
