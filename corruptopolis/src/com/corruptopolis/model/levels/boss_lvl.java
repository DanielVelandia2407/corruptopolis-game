package com.corruptopolis.model.levels;

import com.corruptopolis.model.entities.*;

public class boss_lvl {
    public static final int TARGET_SCORE = 7000;
    public static final int TARGET_WEALTH = 350000;
    public static final int TARGET_NODES = 12;
    public static final int MAX_SUSPICION = 60;
    
    // Modificadores de dificultad
    public static final double SUSPICION_INCREASE_RATE = 1.4;
    public static final double SUSPICION_DECREASE_RATE = 0.8;
    public static final double REPUTATION_GAIN_RATE = 0.9;
    public static final double INFLUENCE_GAIN_RATE = 0.8;
    
    // Red de contactos inicial
    public static PoliticalNode[] getInitialContacts() {
        return new PoliticalNode[] {
            new PoliticalNode("governor_1", "Gobernador Ramírez", NodeType.GOVERNOR, 65000),
            new PoliticalNode("congressman_1", "Senador Vega", NodeType.CONGRESSMAN, 55000),
            new PoliticalNode("judge_2", "Juez Supremo Torres", NodeType.JUDGE, 70000),
            new PoliticalNode("businessman_2", "Consorcio Internacional", NodeType.BUSINESSMAN, 40000),
            new PoliticalNode("journalist_2", "Director de Medios", NodeType.JOURNALIST, 35000)
        };
    }
    
    public static String getLevelDescription() {
        return "Alcanza el poder regional. Controla gobernadores y congresistas. " +
               "La presión de las investigaciones aumenta significativamente.";
    }
}