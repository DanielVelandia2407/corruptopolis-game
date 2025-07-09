package com.corruptopolis.view;
import javax.swing.*;
import java.awt.*;

public class ResourcesPanel extends JPanel {
    private JLabel lblDirtyMoney;
    private JLabel lblInfluence;
    private JLabel lblSuspicion;
    private JButton btnBribe;
    private JButton btnExtract;

    public ResourcesPanel() {
        setLayout(new GridLayout(2, 3, 10, 10));
        lblDirtyMoney = new JLabel("Dirty Money: 0.00");
        lblInfluence  = new JLabel("Influence: 0.00");
        lblSuspicion  = new JLabel("Suspicion: 0");
        btnBribe      = new JButton("Bribe");
        btnExtract    = new JButton("Extract");

        add(lblDirtyMoney);
        add(lblInfluence);
        add(lblSuspicion);
        add(btnBribe);
        add(btnExtract);
    }

    // For controller to register listeners:
    public JButton getBtnBribe() {
        return btnBribe;
    }

    public JButton getBtnExtract() {
        return btnExtract;
    }

    // Update display after actions:
    public void updateResources(double dirtyMoney, double influence, int suspicion) {
        lblDirtyMoney.setText(String.format("Dirty Money: %.2f", dirtyMoney));
        lblInfluence.setText(String.format("Influence: %.2f", influence));
        lblSuspicion.setText("Suspicion: " + suspicion);
    }
}

