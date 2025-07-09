package com.corruptopolis.model.entities;

/**
 * Habilidades especiales que pueden tener los nodos políticos
 */
public enum SpecialAbility {
    BLOCK_INVESTIGATION("Bloquear Investigación", "Puede detener una investigación en curso", 30),
    VOTE_BUYING("Compra de Votos", "Genera influencia adicional durante elecciones", 20),
    RIGGED_CONTRACTS("Contratación Amañada", "Aumenta significativamente la generación de dinero", 25),
    MEDIA_MANIPULATION("Manipulación Mediática", "Reduce el riesgo de exposición de la red", 35),
    HITMAN_CONTRACTS("Contrato de Sicarios", "Puede eliminar amenazas directas", 50),
    EVIDENCE_DESTRUCTION("Destrucción de Evidencia", "Reduce el nivel de sospecha", 40),
    MONEY_LAUNDERING("Lavado de Dinero", "Convierte dinero sucio en limpio más eficientemente", 30),
    JUDICIAL_IMMUNITY("Inmunidad Judicial", "Resistencia a investigaciones legales", 45),
    WHISTLEBLOWER_NETWORK("Red de Informantes", "Proporciona información sobre amenazas", 25),
    INTERNATIONAL_CONNECTIONS("Conexiones Internacionales", "Acceso a refugios y recursos externos", 35);

    private final String name;
    private final String description;
    private final int powerLevel;

    SpecialAbility(String name, String description, int powerLevel) {
        this.name = name;
        this.description = description;
        this.powerLevel = powerLevel;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPowerLevel() { return powerLevel; }

    /**
     * Calcula el costo de usar esta habilidad
     */
    public int getUsageCost() {
        return powerLevel * 1000;
    }

    /**
     * Verifica si la habilidad puede usarse en el estado actual
     */
    public boolean canUseInState(NodeState state) {
        switch (this) {
            case BLOCK_INVESTIGATION:
            case EVIDENCE_DESTRUCTION:
                return state == NodeState.ACTIVE || state == NodeState.UNDER_SUSPICION;
            case HITMAN_CONTRACTS:
                return state == NodeState.ACTIVE;
            case WHISTLEBLOWER_NETWORK:
                return state.isOperational();
            default:
                return state == NodeState.ACTIVE;
        }
    }

    /**
     * Obtiene el efecto de la habilidad
     */
    public String getEffectDescription() {
        switch (this) {
            case BLOCK_INVESTIGATION:
                return "Detiene investigaciones activas por 2-3 turnos";
            case VOTE_BUYING:
                return "+50% influencia durante eventos electorales";
            case RIGGED_CONTRACTS:
                return "+30% generación de dinero del nodo";
            case MEDIA_MANIPULATION:
                return "-20% riesgo de exposición de toda la red";
            case HITMAN_CONTRACTS:
                return "Elimina amenazas específicas permanentemente";
            case EVIDENCE_DESTRUCTION:
                return "Reduce nivel de sospecha en 15-25 puntos";
            case MONEY_LAUNDERING:
                return "+20% eficiencia en lavado de dinero";
            case JUDICIAL_IMMUNITY:
                return "50% resistencia a investigaciones legales";
            case WHISTLEBLOWER_NETWORK:
                return "Alerta temprana sobre amenazas inminentes";
            case INTERNATIONAL_CONNECTIONS:
                return "Opciones de escape y refugio en el extranjero";
            default:
                return "Efecto especial activado";
        }
    }
}
