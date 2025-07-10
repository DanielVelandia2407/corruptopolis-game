package com.corruptopolis.model.levels;

import com.corruptopolis.model.entities.*;

public class operator_lvl {
    public static final int TARGET_SCORE = 3000;
    public static final int TARGET_WEALTH = 150000;
    public static final int TARGET_NODES = 8;
    public static final int MAX_SUSPICION = 70;
    
    // Modificadores de dificultad
    public static final double SUSPICION_INCREASE_RATE = 1.2;
    public static final double SUSPICION_DECREASE_RATE = 1.0;
    public static final double REPUTATION_GAIN_RATE = 1.0;
    public static final double INFLUENCE_GAIN_RATE = 0.9;
    
    // Red de contactos inicial
    public static PoliticalNode[] getInitialContacts() {
        return new PoliticalNode[] {
            new PoliticalNode("mayor_2", "Ana Gutiérrez", NodeType.MAYOR, 22000),
            new PoliticalNode("judge_1", "Magistrado López", NodeType.JUDGE, 45000),
            new PoliticalNode("contractor_2", "Obras y Proyectos SAS", NodeType.CONTRACTOR, 18000),
            new PoliticalNode("journalist_1", "Patricia Morales", NodeType.JOURNALIST, 25000)
        };
    }
    
    public static String getLevelDescription() {
        return "Expande tu influencia a nivel regional. " +
               "Incluye jueces y periodistas en tu red.";
    }
}