package com.corruptopolis.model.entities;

import java.util.*;

/**
 * Representa un nodo en la red de corrupción política
 * Contiene todos los atributos necesarios para modelar un funcionario corrupto
 */
public class PoliticalNode {
    private String id;
    private String name;
    private NodeType nodeType;
    private NodeState currentState;
    private SpecialAbility specialAbility;

    private int bribeCost;
    private int influenceContribution;
    private int wealthContribution;
    private int loyaltyLevel;
    private int ambitionLevel;
    private int exposureRisk;

    private boolean acceptsBribes;
    private List<PoliticalNode> subordinates;
    private PoliticalNode superior;

    /**
     * Constructor para crear un nuevo nodo político
     */
    public PoliticalNode(String id, String name, NodeType nodeType, int bribeCost) {
        this.id = id;
        this.name = name;
        this.nodeType = nodeType;
        this.bribeCost = bribeCost;
        this.currentState = NodeState.INACTIVE;
        this.acceptsBribes = true;
        this.subordinates = new ArrayList<>();

        initializeDefaultValues();
    }

    /**
     * Inicializa valores por defecto basados en el tipo de nodo
     */
    private void initializeDefaultValues() {
        Random random = new Random();

        switch (nodeType) {
            case MAYOR:
                this.influenceContribution = random.nextInt(15) + 10;
                this.wealthContribution = random.nextInt(20) + 15;
                break;
            case GOVERNOR:
                this.influenceContribution = random.nextInt(25) + 20;
                this.wealthContribution = random.nextInt(30) + 25;
                break;
            case CONGRESSMAN:
                this.influenceContribution = random.nextInt(20) + 15;
                this.wealthContribution = random.nextInt(25) + 20;
                break;
            case JUDGE:
                this.influenceContribution = random.nextInt(30) + 25;
                this.wealthContribution = random.nextInt(15) + 10;
                break;
            case CONTRACTOR:
                this.influenceContribution = random.nextInt(10) + 5;
                this.wealthContribution = random.nextInt(35) + 30;
                break;
            case BUSINESSMAN:
                this.influenceContribution = random.nextInt(12) + 8;
                this.wealthContribution = random.nextInt(28) + 22;
                break;
            case JOURNALIST:
                this.influenceContribution = random.nextInt(18) + 12;
                this.wealthContribution = random.nextInt(12) + 8;
                break;
            default:
                this.influenceContribution = random.nextInt(10) + 5;
                this.wealthContribution = random.nextInt(15) + 10;
        }

        this.loyaltyLevel = random.nextInt(40) + 30;
        this.ambitionLevel = random.nextInt(50) + 25;
        this.exposureRisk = random.nextInt(30) + 20;

        // Asignar habilidad especial basada en el tipo
        assignSpecialAbility();
    }

    /**
     * Asigna una habilidad especial basada en el tipo de nodo
     */
    private void assignSpecialAbility() {
        Random random = new Random();

        switch (nodeType) {
            case JUDGE:
                this.specialAbility = random.nextBoolean() ?
                        SpecialAbility.BLOCK_INVESTIGATION : SpecialAbility.JUDICIAL_IMMUNITY;
                break;
            case MAYOR:
                this.specialAbility = random.nextBoolean() ?
                        SpecialAbility.RIGGED_CONTRACTS : SpecialAbility.VOTE_BUYING;
                break;
            case CONTRACTOR:
                this.specialAbility = SpecialAbility.RIGGED_CONTRACTS;
                break;
            case JOURNALIST:
                this.specialAbility = SpecialAbility.MEDIA_MANIPULATION;
                break;
            case BUSINESSMAN:
                this.specialAbility = random.nextBoolean() ?
                        SpecialAbility.MONEY_LAUNDERING : SpecialAbility.INTERNATIONAL_CONNECTIONS;
                break;
            default:
                this.specialAbility = null;
        }
    }

    /**
     * Activa el nodo después de un soborno exitoso
     */
    public void activate() {
        this.currentState = NodeState.ACTIVE;
        this.acceptsBribes = true;
    }

    /**
     * Agrega un subordinado a este nodo
     */
    public void addSubordinate(PoliticalNode subordinate) {
        if (!subordinates.contains(subordinate)) {
            subordinates.add(subordinate);
            subordinate.setSuperior(this);
        }
    }

    /**
     * Remueve un subordinado de este nodo
     */
    public void removeSubordinate(PoliticalNode subordinate) {
        if (subordinates.remove(subordinate)) {
            subordinate.setSuperior(null);
        }
    }

    /**
     * Calcula el costo actual de soborno considerando factores dinámicos
     */
    public int calculateCurrentBribeCost() {
        double multiplier = 1.0;

        if (currentState == NodeState.UNDER_SUSPICION) {
            multiplier += 0.5;
        } else if (currentState == NodeState.INVESTIGATED) {
            multiplier += 1.0;
        }

        multiplier += (ambitionLevel / 100.0) * 0.3;
        multiplier -= (loyaltyLevel / 100.0) * 0.2;

        return (int) (bribeCost * multiplier);
    }

    /**
     * Calcula la probabilidad de traición basada en lealtad y ambición
     */
    public double calculateBetrayalProbability() {
        double baseProbability = (100 - loyaltyLevel) / 100.0;
        double ambitionFactor = ambitionLevel / 100.0;

        return Math.min(baseProbability + (ambitionFactor * 0.3), 0.95);
    }

    /**
     * Calcula la efectividad del nodo basada en su estado y atributos
     */
    public double calculateEffectiveness() {
        double baseEffectiveness = 1.0;

        switch (currentState) {
            case ACTIVE:
                baseEffectiveness = 1.0;
                break;
            case UNDER_SUSPICION:
                baseEffectiveness = 0.7;
                break;
            case INVESTIGATED:
                baseEffectiveness = 0.3;
                break;
            case BURNED:
            case IMPRISONED:
                baseEffectiveness = 0.0;
                break;
            default:
                baseEffectiveness = 0.1;
        }

        // Modificar por lealtad
        baseEffectiveness *= (loyaltyLevel / 100.0);

        return baseEffectiveness;
    }

    /**
     * Ejecuta la habilidad especial del nodo
     */
    public boolean executeSpecialAbility() {
        if (specialAbility == null || !currentState.isOperational()) {
            return false;
        }

        // La lógica específica de cada habilidad se implementaría aquí
        // Por ahora, simplemente retorna true si el nodo puede ejecutarla
        return loyaltyLevel > 50 && exposureRisk < 80;
    }

    /**
     * Modifica el nivel de lealtad
     */
    public void modifyLoyalty(int change) {
        loyaltyLevel = Math.max(0, Math.min(100, loyaltyLevel + change));
    }

    /**
     * Modifica el nivel de ambición
     */
    public void modifyAmbition(int change) {
        ambitionLevel = Math.max(0, Math.min(100, ambitionLevel + change));
    }

    /**
     * Modifica el riesgo de exposición
     */
    public void modifyExposureRisk(int change) {
        exposureRisk = Math.max(0, Math.min(100, exposureRisk + change));
    }

    /**
     * Actualiza el estado del nodo
     */
    public void updateState(NodeState newState) {
        this.currentState = newState;

        // Ajustar comportamiento basado en el nuevo estado
        switch (newState) {
            case UNDER_SUSPICION:
                this.exposureRisk += 10;
                this.loyaltyLevel -= 5;
                break;
            case INVESTIGATED:
                this.exposureRisk += 20;
                this.loyaltyLevel -= 15;
                this.acceptsBribes = false;
                break;
            case BURNED:
            case IMPRISONED:
                this.acceptsBribes = false;
                this.influenceContribution = 0;
                this.wealthContribution = 0;
                break;
            case PROTECTED_WITNESS:
                this.acceptsBribes = false;
                this.loyaltyLevel = 0;
                break;
        }
    }

    /**
     * Verifica si el nodo puede ser sobornado
     */
    public boolean canBeBribed() {
        return acceptsBribes && currentState.isOperational() && loyaltyLevel < 90;
    }

    /**
     * Obtiene una descripción detallada del nodo
     */
    public String getDetailedDescription() {
        StringBuilder description = new StringBuilder();
        description.append(String.format("=== %s ===\n", name));
        description.append(String.format("Cargo: %s\n", nodeType.getDisplayName()));
        description.append(String.format("Estado: %s\n", currentState.getDisplayName()));
        description.append(String.format("Costo Soborno: $%d\n", calculateCurrentBribeCost()));
        description.append(String.format("Lealtad: %d%% | Ambición: %d%% | Riesgo: %d%%\n",
                loyaltyLevel, ambitionLevel, exposureRisk));
        description.append(String.format("Contribución: Influencia +%d, Riqueza +$%d\n",
                influenceContribution, wealthContribution));

        if (specialAbility != null) {
            description.append(String.format("Habilidad Especial: %s\n", specialAbility.getName()));
        }

        if (!subordinates.isEmpty()) {
            description.append(String.format("Subordinados: %d\n", subordinates.size()));
        }

        return description.toString();
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public NodeType getNodeType() { return nodeType; }
    public NodeState getCurrentState() { return currentState; }
    public void setCurrentState(NodeState state) {
        updateState(state);
    }
    public SpecialAbility getSpecialAbility() { return specialAbility; }
    public void setSpecialAbility(SpecialAbility ability) { this.specialAbility = ability; }

    public int getBribeCost() { return bribeCost; }
    public void setBribeCost(int bribeCost) { this.bribeCost = bribeCost; }
    public int getInfluenceContribution() { return (int)(influenceContribution * calculateEffectiveness()); }
    public int getWealthContribution() { return (int)(wealthContribution * calculateEffectiveness()); }
    public int getLoyaltyLevel() { return loyaltyLevel; }
    public void setLoyaltyLevel(int loyaltyLevel) { this.loyaltyLevel = Math.max(0, Math.min(100, loyaltyLevel)); }
    public int getAmbitionLevel() { return ambitionLevel; }
    public void setAmbitionLevel(int ambitionLevel) { this.ambitionLevel = Math.max(0, Math.min(100, ambitionLevel)); }
    public int getExposureRisk() { return exposureRisk; }
    public void setExposureRisk(int exposureRisk) { this.exposureRisk = Math.max(0, Math.min(100, exposureRisk)); }

    public boolean getAcceptsBribes() { return acceptsBribes; }
    public void setAcceptsBribes(boolean accepts) { this.acceptsBribes = accepts; }

    public List<PoliticalNode> getSubordinates() { return new ArrayList<>(subordinates); }
    public PoliticalNode getSuperior() { return superior; }
    public void setSuperior(PoliticalNode superior) { this.superior = superior; }

    @Override
    public String toString() {
        return String.format("%s (%s) - Estado: %s, Lealtad: %d%%, Ambición: %d%%",
                name, nodeType.getDisplayName(), currentState.getDisplayName(),
                loyaltyLevel, ambitionLevel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PoliticalNode that = (PoliticalNode) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
