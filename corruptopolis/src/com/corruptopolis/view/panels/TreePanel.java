package com.corruptopolis.view.panels;

import com.corruptopolis.controller.GameController;
import com.corruptopolis.model.structures.CorruptionTree;
import com.corruptopolis.model.entities.PoliticalNode;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TreePanel extends JPanel {
    private GameController controller;
    private CorruptionTree tree;

    public TreePanel(GameController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Red de Corrupción"));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Doble click para mostrar información
                    controller.requestNodeInfo("player");
                }
            }
        });
    }

    public void updateTree(CorruptionTree tree) {
        this.tree = tree;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (tree != null) {
            drawTree(g2d);
        } else {
            drawEmptyMessage(g2d);
        }
    }

    private void drawTree(Graphics2D g2d) {
        // Dibujar el árbol de forma simple
        g2d.setColor(Color.BLUE);
        g2d.fillOval(getWidth()/2 - 20, 50, 40, 40);
        g2d.setColor(Color.WHITE);
        g2d.drawString("TÚ", getWidth()/2 - 10, 75);

        // Dibujar nodos subordinados si existen
        List<PoliticalNode> activeNodes = tree.getActiveNodes();
        int nodeCount = activeNodes.size();

        if (nodeCount > 1) { // Más que solo el jugador
            int spacing = Math.min(getWidth() / (nodeCount + 1), 100);
            int y = 150;

            for (int i = 1; i < nodeCount && i < 6; i++) { // Máximo 5 nodos visibles
                PoliticalNode node = activeNodes.get(i);
                int x = i * spacing;

                // Línea de conexión
                g2d.setColor(Color.GRAY);
                g2d.drawLine(getWidth()/2, 90, x + 20, y);

                // Nodo
                g2d.setColor(getNodeColor(node));
                g2d.fillOval(x, y, 40, 40);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 8));
                g2d.drawString(node.getName().substring(0, Math.min(6, node.getName().length())), x + 5, y + 25);
            }
        }

        // Estadísticas del árbol
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Nodos Activos: " + tree.getActiveNodes().size(), 10, getHeight() - 40);
        g2d.drawString("Influencia Total: " + tree.calculateTotalInfluence(), 10, getHeight() - 20);
    }

    private Color getNodeColor(PoliticalNode node) {
        switch (node.getCurrentState()) {
            case ACTIVE:
                return Color.GREEN;
            case UNDER_SUSPICION:
                return Color.ORANGE;
            case INVESTIGATED:
                return Color.RED;
            default:
                return Color.GRAY;
        }
    }

    private void drawEmptyMessage(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.ITALIC, 14));
        FontMetrics fm = g2d.getFontMetrics();
        String message = "Tu red de corrupción aparecerá aquí";
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g2d.drawString(message, x, y);
    }
}
