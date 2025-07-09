package com.corruptopolis.model;

/**
 * Representa al jugador y gestiona sus recursos y estado
 */
public class Player {
    private String name;
    private int totalPoliticalCapital;
    private int dirtyMoney;
    private int accumulatedInfluence;
    private int generalSuspicionLevel;
    private int publicReputation;
    private int cleanMoney;

    private int turnsPlayed;
    private int totalBribes;
    private int successfulOperations;
    private int failedOperations;

    /**
     * Constructor para crear un nuevo jugador
     */
    public Player(String name) {
        this.name = name;
        this.dirtyMoney = 500000;
        this.accumulatedInfluence = 10;
        this.generalSuspicionLevel = 0;
        this.publicReputation = 50;
        this.cleanMoney = 10000;
        this.turnsPlayed = 0;
        this.totalBribes = 0;
        this.successfulOperations = 0;
        this.failedOperations = 0;

        calculateTotalPoliticalCapital();
    }

    /**
     * Calcula el capital político total basado en influencia y control
     */
    private void calculateTotalPoliticalCapital() {
        this.totalPoliticalCapital = accumulatedInfluence + (dirtyMoney / 1000);
    }

    /**
     * Intenta realizar un soborno
     */
    public boolean attemptBribe(int cost) {
        if (dirtyMoney >= cost) {
            dirtyMoney -= cost;
            totalBribes++;
            increaseSuspicion(2);
            calculateTotalPoliticalCapital();
            return true;
        }
        return false;
    }

    /**
     * Añade dinero sucio al jugador
     */
    public void addDirtyMoney(int amount) {
        dirtyMoney += amount;
        calculateTotalPoliticalCapital();
    }

    /**
     * Añade influencia al jugador
     */
    public void addInfluence(int amount) {
        accumulatedInfluence += amount;
        calculateTotalPoliticalCapital();
    }

    /**
     * Incrementa el nivel de sospecha
     */
    public void increaseSuspicion(int amount) {
        generalSuspicionLevel = Math.min(100, generalSuspicionLevel + amount);
    }

    /**
     * Reduce el nivel de sospecha
     */
    public void reduceSuspicion(int amount) {
        generalSuspicionLevel = Math.max(0, generalSuspicionLevel - amount);
    }

    /**
     * Modifica la reputación pública
     */
    public void modifyReputation(int change) {
        publicReputation = Math.max(0, Math.min(100, publicReputation + change));
    }

    /**
     * Realiza operación de lavado de dinero
     */
    public boolean launderMoney(int amount) {
        if (dirtyMoney >= amount) {
            dirtyMoney -= amount;
            int cleanAmount = (int) (amount * 0.7);
            cleanMoney += cleanAmount;
            increaseSuspicion(1);
            calculateTotalPoliticalCapital();
            return true;
        }
        return false;
    }

    /**
     * Gasta dinero limpio
     */
    public boolean spendCleanMoney(int amount) {
        if (cleanMoney >= amount) {
            cleanMoney -= amount;
            return true;
        }
        return false;
    }

    /**
     * Realiza operación de encubrimiento
     */
    public boolean performCoverUp(int cost) {
        if (dirtyMoney >= cost) {
            dirtyMoney -= cost;
            reduceSuspicion(15);
            modifyReputation(5);
            return true;
        }
        return false;
    }

    /**
     * Registra una operación exitosa
     */
    public void recordSuccessfulOperation() {
        successfulOperations++;
    }

    /**
     * Registra una operación fallida
     */
    public void recordFailedOperation() {
        failedOperations++;
        increaseSuspicion(5);
    }

    /**
     * Avanza un turno
     */
    public void nextTurn() {
        turnsPlayed++;

        if (generalSuspicionLevel > 0) {
            reduceSuspicion(1);
        }

        if (publicReputation > 50) {
            reduceSuspicion(1);
        }

        calculateTotalPoliticalCapital();
    }

    /**
     * Verifica si el jugador está en peligro crítico
     */
    public boolean isInCriticalDanger() {
        return generalSuspicionLevel >= 95;
    }

    /**
     * Verifica si el jugador ha perdido
     */
    public boolean hasLost() {
        return generalSuspicionLevel >= 100;
    }

    /**
     * Calcula el puntaje total del jugador
     */
    public int calculateScore() {
        return totalPoliticalCapital + (successfulOperations * 50) - (failedOperations * 25);
    }

    /**
     * Obtiene estadísticas del jugador
     */
    public String getStats() {
        return String.format(
                "Turnos: %d | Sobornos: %d | Éxitos: %d | Fallos: %d | Puntaje: %d",
                turnsPlayed, totalBribes, successfulOperations, failedOperations, calculateScore()
        );
    }

    // Getters
    public String getName() { return name; }
    public int getTotalPoliticalCapital() { return totalPoliticalCapital; }
    public int getDirtyMoney() { return dirtyMoney; }
    public int getAccumulatedInfluence() { return accumulatedInfluence; }
    public int getGeneralSuspicionLevel() { return generalSuspicionLevel; }
    public int getPublicReputation() { return publicReputation; }
    public int getCleanMoney() { return cleanMoney; }
    public int getTurnsPlayed() { return turnsPlayed; }
    public int getTotalBribes() { return totalBribes; }
    public int getSuccessfulOperations() { return successfulOperations; }
    public int getFailedOperations() { return failedOperations; }

    @Override
    public String toString() {
        return String.format(
                "%s - Capital: %d | Dinero Sucio: %d | Influencia: %d | Sospecha: %d%% | Reputación: %d%%",
                name, totalPoliticalCapital, dirtyMoney, accumulatedInfluence,
                generalSuspicionLevel, publicReputation
        );
    }
}
