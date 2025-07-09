package com.corruptopolis.view.panels;

import com.corruptopolis.controller.GameController;
import com.corruptopolis.model.Game;
import com.corruptopolis.model.entities.PoliticalNode;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GamePanel extends JPanel {
    private GameController controller;
    private JPanel contactsPanel;
    private JScrollPane scrollPane;

    public GamePanel(GameController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Contactos Disponibles"));

        contactsPanel = new JPanel();
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(contactsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateGameArea(Game game) {
        contactsPanel.removeAll();

        if (game != null) {
            List<PoliticalNode> contacts = game.getConnectionGraph().getAllAvailableContacts();

            if (contacts.isEmpty()) {
                JLabel noContactsLabel = new JLabel("No hay contactos disponibles");
                noContactsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                contactsPanel.add(noContactsLabel);
            } else {
                for (PoliticalNode contact : contacts) {
                    contactsPanel.add(createContactPanel(contact));
                    contactsPanel.add(Box.createVerticalStrut(5));
                }
            }
        }

        contactsPanel.revalidate();
        contactsPanel.repaint();
    }

    private JPanel createContactPanel(PoliticalNode contact) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Información del contacto
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Nombre: " + contact.getName()));
        infoPanel.add(new JLabel("Cargo: " + contact.getNodeType().getDisplayName()));
        infoPanel.add(new JLabel("Costo: $" + contact.calculateCurrentBribeCost()));

        // Botón de soborno
        JButton bribeButton = new JButton("Sobornar");
        bribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleBribeAttempt(contact.getId());
            }
        });

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(bribeButton, BorderLayout.EAST);

        return panel;
    }
}
