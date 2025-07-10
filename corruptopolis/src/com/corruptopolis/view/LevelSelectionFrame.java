package com.corruptopolis.view;

import com.corruptopolis.controller.GameController;
import com.corruptopolis.model.GameLevel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelSelectionFrame extends JFrame {
    private GameController controller;
    private JPanel levelPanel;
    
    public LevelSelectionFrame(GameController controller) {
        this.controller = controller;
        initializeFrame();
        createLevelSelection();
    }
    
    private void initializeFrame() {
        setTitle("Corruptópolis - Selección de Nivel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        getContentPane().setBackground(new Color(45, 45, 45));
    }
    
    private void createLevelSelection() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("SELECCIONA TU NIVEL DE CORRUPCIÓN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Panel de niveles
        levelPanel = new JPanel(new GridLayout(1, 5, 20, 0));
        levelPanel.setBackground(new Color(45, 45, 45));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        createLevelButtons();
        add(levelPanel, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(45, 45, 45));
        
        JButton exitButton = new JButton("Salir");
        exitButton.setPreferredSize(new Dimension(100, 40));
        exitButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(exitButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void createLevelButtons() {
        for (GameLevel level : GameLevel.values()) {
            JButton levelButton = createLevelButton(level);
            levelPanel.add(levelButton);
        }
    }
    
    private JButton createLevelButton(GameLevel level) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Color de fondo según estado
                Color bgColor;
                if (level.isCompleted()) {
                    bgColor = new Color(34, 139, 34, 100); // Verde oscuro con transparencia
                } else if (level.isUnlocked()) {
                    bgColor = new Color(70, 70, 70);
                } else {
                    bgColor = new Color(30, 30, 30);
                }
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Borde
                g2d.setColor(level.isUnlocked() ? Color.WHITE : Color.GRAY);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                
                // Texto
                g2d.setColor(level.isUnlocked() ? Color.WHITE : Color.GRAY);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                
                String text = level.getDisplayName();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(text, textX, textY);
                
                // Marca de completado
                if (level.isCompleted()) {
                    g2d.setColor(new Color(0, 255, 0, 150));
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    fm = g2d.getFontMetrics();
                    String completed = "COMPLETADO";
                    int compX = (getWidth() - fm.stringWidth(completed)) / 2;
                    g2d.drawString(completed, compX, getHeight() - 10);
                }
            }
        };
        
        button.setPreferredSize(new Dimension(140, 200));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        
        if (level.isUnlocked()) {
            button.addActionListener(new LevelSelectionListener(level));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        return button;
    }
    
    public void updateLevelStatus() {
        levelPanel.removeAll();
        createLevelButtons();
        levelPanel.revalidate();
        levelPanel.repaint();
    }
    
    private class LevelSelectionListener implements ActionListener {
        private GameLevel selectedLevel;
        
        public LevelSelectionListener(GameLevel level) {
            this.selectedLevel = level;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!selectedLevel.isUnlocked()) return;
            
            controller.startNewGameWithLevel(selectedLevel);
            dispose();
        }
    }
}