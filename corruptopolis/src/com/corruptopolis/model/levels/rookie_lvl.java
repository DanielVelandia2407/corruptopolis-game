package com.corruptopolis.model.levels;

import com.corruptopolis.model.entities.*;

public class rookie_lvl {
    public static final int TARGET_SCORE = 1000;
    public static final int TARGET_WEALTH = 50000;
    public static final int TARGET_NODES = 5;
    public static final int MAX_SUSPICION = 80;
    
    // Modificadores de dificultad
    public static final double SUSPICION_INCREASE_RATE = 1.0;
    public static final double SUSPICION_DECREASE_RATE = 1.2;
    public static final double REPUTATION_GAIN_RATE = 1.1;
    public static final double INFLUENCE_GAIN_RATE = 1.0;
    
    // Red de contactos inicial
    public static PoliticalNode[] getInitialContacts() {
        return new PoliticalNode[] {
            new PoliticalNode("mayor_1", "Carlos Mendoza", NodeType.MAYOR, 15000),
            new PoliticalNode("contractor_1", "Constructora Elite", NodeType.CONTRACTOR, 12000),
            new PoliticalNode("businessman_1", "Roberto Silva", NodeType.BUSINESSMAN, 18000)
        };
    }
    
    public static String getLevelDescription() {
        return "Nivel inicial para aprender las mecánicas básicas del juego. " +
               "Construye una pequeña red de corrupción local.";
    }
}