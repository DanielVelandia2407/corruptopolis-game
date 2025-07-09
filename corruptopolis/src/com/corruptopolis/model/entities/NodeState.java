package com.corruptopolis.model.entities;

/**
 * Estados posibles de un nodo político
 */
public enum NodeState {
    INACTIVE("Inactivo"),
    ACTIVE("Activo"),
    UNDER_SUSPICION("Bajo Sospecha"),
    INVESTIGATED("Investigado"),
    BURNED("Quemado"),
    IMPRISONED("Encarcelado"),
    PROTECTED_WITNESS("Testigo Protegido"),
    INTENSIVE_CARE("En UCI");

    private final String displayName;

    NodeState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    /**
     * Verifica si el nodo puede operar en este estado
     */
    public boolean isOperational() {
        return this == ACTIVE || this == UNDER_SUSPICION;
    }

    /**
     * Verifica si el nodo está comprometido
     */
    public boolean isCompromised() {
        return this == BURNED || this == IMPRISONED || this == PROTECTED_WITNESS;
    }

    /**
     * Verifica si el nodo puede generar recursos
     */
    public boolean canGenerateResources() {
        return this == ACTIVE;
    }

    /**
     * Verifica si el nodo está en peligro
     */
    public boolean isInDanger() {
        return this == UNDER_SUSPICION || this == INVESTIGATED;
    }
}
