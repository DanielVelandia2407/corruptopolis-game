package com.corruptopolis.model;

public class PlayerResources {
    private double dirtyMoney;
    private double influence;
    private int suspicionLevel;   // 0–100

    public PlayerResources(double initialDirtyMoney, double initialInfluence) {
        this.dirtyMoney = initialDirtyMoney;
        this.influence = initialInfluence;
        this.suspicionLevel = 0;
    }

    // ----- Getters -----
    public double getDirtyMoney() {
        return dirtyMoney;
    }

    public double getInfluence() {
        return influence;
    }

    public int getSuspicionLevel() {
        return suspicionLevel;
    }

    // ----- Setters -----
    public void setDirtyMoney(double dirtyMoney) {
        this.dirtyMoney = Math.max(0, dirtyMoney);
    }

    public void setInfluence(double influence) {
        this.influence = Math.max(0, influence);
    }

    public void setSuspicionLevel(int suspicionLevel) {
        this.suspicionLevel = Math.min(100, Math.max(0, suspicionLevel));
    }

    @Override
    public String toString() {
        return String.format(
            "PlayerResources{dirtyMoney=%.2f, influence=%.2f, suspicionLevel=%d}",
            dirtyMoney, influence, suspicionLevel
        );
    }
}
