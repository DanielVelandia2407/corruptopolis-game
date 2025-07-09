package com.corruptopolis.view.dialogs;

import com.corruptopolis.controller.GameController;
import javax.swing.*;
import java.awt.*;

public class NodeInfoDialog extends JDialog {
    private GameController controller;
    private String nodeId;

    public NodeInfoDialog(Frame parent, GameController controller, String nodeId) {
        super(parent, "Información del Nodo", true);
        this.controller = controller;
        this.nodeId = nodeId;
        initializeDialog();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(getParent());

        JTextArea infoArea = new JTextArea("Información detallada del nodo: " + nodeId);
        infoArea.setEditable(false);

        add(new JScrollPane(infoArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
