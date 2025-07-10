package com.corruptopolis.model.levels;

import com.corruptopolis.model.entities.*;

public class godfather_lvl {
    public static final int TARGET_SCORE = 30000;
    public static final int TARGET_WEALTH = 1500000;
    public static final int TARGET_NODES = 25;
    public static final int MAX_SUSPICION = 40;
    
    // Modificadores de dificultad
    public static final double SUSPICION_INCREASE_RATE = 2.0;
    public static final double SUSPICION_DECREASE_RATE = 0.4;
    public static final double REPUTATION_GAIN_RATE = 0.7;
    public static final double INFLUENCE_GAIN_RATE = 0.6;
    
    // Red de contactos inicial
    public static PoliticalNode[] getInitialContacts() {
        return new PoliticalNode[] {
            new PoliticalNode("minister_2", "Primer Ministro", NodeType.MINISTER, 200000),
            new PoliticalNode("minister_3", "Ministro Defensa", NodeType.MINISTER, 180000),
            new PoliticalNode("governor_3", "Gobernador Metropolitano", NodeType.GOVERNOR, 120000),
            new PoliticalNode("congressman_3", "Líder Mayoría", NodeType.CONGRESSMAN, 140000),
            new PoliticalNode("judge_4", "Presidente Corte Suprema", NodeType.JUDGE, 250000),
            new PoliticalNode("businessman_4", "Conglomerado Financiero", NodeType.BUSINESSMAN, 150000),
            new PoliticalNode("journalist_4", "Imperio Mediático", NodeType.JOURNALIST, 100000)
        };
    }
    
    public static String getLevelDescription() {
        return "El nivel supremo del poder. Controla el estado desde las sombras. " +
               "Supervivencia extrema: un error y todo se desmorona.";
    }
}