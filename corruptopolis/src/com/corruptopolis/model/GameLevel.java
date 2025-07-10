package com.corruptopolis.model;

public enum GameLevel {
    ROOKIE("Novato", 1, false),
    OPERATOR("Operador", 2, false),
    BOSS("Jefe", 3, false),
    KINGPIN("Capo", 4, false),
    GODFATHER("Padrino", 5, false);
    
    private final String displayName;
    private final int levelNumber;
    private boolean completed;
    
    GameLevel(String displayName, int levelNumber, boolean completed) {
        this.displayName = displayName;
        this.levelNumber = levelNumber;
        this.completed = completed;
    }
    
    public String getDisplayName() { return displayName; }
    public int getLevelNumber() { return levelNumber; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public boolean isUnlocked() {
        return true; // Todos los niveles son accesibles
    }
}