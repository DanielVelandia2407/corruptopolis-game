package com.corruptopolis.model.levels;

import com.corruptopolis.model.entities.*;

public class kingpin_lvl {
    public static final int TARGET_SCORE = 15000;
    public static final int TARGET_WEALTH = 750000;
    public static final int TARGET_NODES = 18;
    public static final int MAX_SUSPICION = 50;
    
    // Modificadores de dificultad
    public static final double SUSPICION_INCREASE_RATE = 1.6;
    public static final double SUSPICION_DECREASE_RATE = 0.6;
    public static final double REPUTATION_GAIN_RATE = 0.8;
    public static final double INFLUENCE_GAIN_RATE = 0.7;
    
    // Red de contactos inicial
    public static PoliticalNode[] getInitialContacts() {
        return new PoliticalNode[] {
            new PoliticalNode("minister_1", "Ministro de Hacienda", NodeType.MINISTER, 120000),
            new PoliticalNode("governor_2", "Gobernador Capital", NodeType.GOVERNOR, 85000),
            new PoliticalNode("congressman_2", "Presidente Congreso", NodeType.CONGRESSMAN, 95000),
            new PoliticalNode("judge_3", "Corte Constitucional", NodeType.JUDGE, 110000),
            new PoliticalNode("businessman_3", "Monopolio Energético", NodeType.BUSINESSMAN, 75000),
            new PoliticalNode("journalist_3", "Cadena Nacional TV", NodeType.JOURNALIST, 60000)
        };
    }
    
    public static String getLevelDescription() {
        return "Domina el poder nacional. Controla ministros y altas cortes. " +
               "Máximo nivel de vigilancia y riesgo de exposición.";
    }
}