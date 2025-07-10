package com.corruptopolis.view.panels;

import com.corruptopolis.controller.GameController;
import com.corruptopolis.model.Game;
import com.corruptopolis.model.entities.PoliticalNode;
import com.corruptopolis.model.structures.DualWeightGraph;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

public class NetworkGraphPanel extends JPanel {
    private GameController controller;
    private DualWeightGraph dualWeightGraph;
    private Map<String, Point> nodePositions;

    public NetworkGraphPanel(GameController controller) {
        this.controller = controller;
        this.nodePositions = new HashMap<>();
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Red de Contactos Disponibles"));
        setBackground(Color.WHITE);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String nodeId = findNodeAtPosition(e.getPoint());
                if (nodeId != null) {
                    controller.requestNodeInfo(nodeId);
                }
            }
        });
    }

    public void updateGraph(Game game) {
        if (game != null) {
            // Crear grafo temporal con contactos disponibles
            this.dualWeightGraph = new DualWeightGraph();
            List<PoliticalNode> availableContacts = game.getConnectionGraph().getAllAvailableContacts();
            
            // Añadir solo contactos disponibles al grafo
            for (PoliticalNode contact : availableContacts) {
                dualWeightGraph.addNode(contact);
            }
            
            // Generar conexiones entre contactos disponibles
            dualWeightGraph.generateConnections();
            
            calculateNodePositions();
            repaint();
        }
    }

    private void calculateNodePositions() {
        nodePositions.clear();
        if (dualWeightGraph == null || dualWeightGraph.getAllNodes().isEmpty()) return;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3;
        
        List<PoliticalNode> nodes = new ArrayList<>(dualWeightGraph.getAllNodes());
        for (int i = 0; i < nodes.size(); i++) {
            double angle = 2 * Math.PI * i / nodes.size();
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            nodePositions.put(nodes.get(i).getId(), new Point(x, y));
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (dualWeightGraph == null || dualWeightGraph.getAllNodes().isEmpty()) {
            drawEmptyMessage(g2d);
            return;
        }

        drawConnections(g2d);
        drawNodes(g2d);
        drawLegend(g2d);
    }

    private void drawConnections(Graphics2D g2d) {
        for (PoliticalNode node : dualWeightGraph.getAllNodes()) {
            Point fromPos = nodePositions.get(node.getId());
            if (fromPos == null) continue;
            
            for (Map.Entry<String, DualWeightGraph.EdgeWeight> connection : dualWeightGraph.getConnections(node.getId()).entrySet()) {
                Point toPos = nodePositions.get(connection.getKey());
                if (toPos == null) continue;
                
                Color connectionColor = getReputationColor(connection.getValue().reputationGain);
                g2d.setColor(connectionColor);
                g2d.setStroke(new BasicStroke(2.0f));
                g2d.drawLine(fromPos.x, fromPos.y, toPos.x, toPos.y);
            }
        }
    }

    private void drawNodes(Graphics2D g2d) {
        for (PoliticalNode node : dualWeightGraph.getAllNodes()) {
            Point pos = nodePositions.get(node.getId());
            if (pos == null) continue;
            
            int suspicionProb = dualWeightGraph.getNodeSuspicionProbability(node.getId());
            Color nodeColor = getSuspicionColor(suspicionProb);
            
            g2d.setColor(nodeColor);
            g2d.fillOval(pos.x - 25, pos.y - 25, 50, 50);
            
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawOval(pos.x - 25, pos.y - 25, 50, 50);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            FontMetrics fm = g2d.getFontMetrics();
            String shortName = node.getName().length() > 8 ? 
                node.getName().substring(0, 8) : node.getName();
            int textX = pos.x - fm.stringWidth(shortName) / 2;
            g2d.drawString(shortName, textX, pos.y + 3);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 8));
            g2d.drawString("S:" + suspicionProb + "%", pos.x - 15, pos.y + 35);
            g2d.drawString("R:" + dualWeightGraph.getNodeReputationGain(node.getId()), pos.x - 15, pos.y + 45);
        }
    }

    private Color getSuspicionColor(int suspicionProbability) {
        if (suspicionProbability > 75) return new Color(177, 7, 7);
        if (suspicionProbability > 50) return new Color(214, 92, 27);
        if (suspicionProbability > 30) return new Color(166, 155, 37);
        return new Color(85, 145, 172);
    }

    private Color getReputationColor(int reputationGain) {
        if (reputationGain > 70) return new Color(47, 129, 47);
        if (reputationGain > 50) return new Color(98, 177, 98);
        if (reputationGain > 30) return new Color(128, 128, 103);
        return new Color(57, 56, 56);
    }

    private void drawLegend(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("LEYENDA:", 10, 40);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        
        g2d.drawString("Nodos (Probabilidad Sospecha):", 10, 60);
        drawLegendItem(g2d, new Color(177, 7, 7), ">75% - Alto Riesgo", 10, 75);
        drawLegendItem(g2d, new Color(214, 92, 27), "50-75% - Riesgo Medio", 10, 90);
        drawLegendItem(g2d, new Color(166, 155, 37), "30-50% - Riesgo Bajo", 10, 110);
        drawLegendItem(g2d, new Color(85, 145, 172), "<30% - Muy Seguro", 10, 130);
        
        g2d.drawString("Conexiones (Ganancia Reputación):", 10, 145);
        drawLegendItem(g2d, new Color(47, 129, 47), ">70 - Excelente", 10, 160);
        drawLegendItem(g2d, new Color(98, 177, 98), "50-70 - Buena", 10, 175);
        drawLegendItem(g2d, new Color(128, 128, 103), "30-50 - Regular", 10, 190);
        drawLegendItem(g2d, new Color(57, 56, 56), "<30 - Baja", 10, 205);
    }

    private void drawLegendItem(Graphics2D g2d, Color color, String text, int x, int y) {
        g2d.setColor(color);
        g2d.fillRect(x, y - 8, 15, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y - 8, 15, 10);
        g2d.drawString(text, x + 20, y);
    }

    private void drawEmptyMessage(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.ITALIC, 14));
        FontMetrics fm = g2d.getFontMetrics();
        String message = "No hay contactos disponibles para sobornar";
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g2d.drawString(message, x, y);
    }

    private String findNodeAtPosition(Point clickPoint) {
        for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
            Point nodePos = entry.getValue();
            if (clickPoint.distance(nodePos) <= 25) {
                return entry.getKey();
            }
        }
        return null;
    }
}