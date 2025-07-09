package com.corruptopolis.model.entities;

/**
 * Tipos de nodos políticos disponibles en el juego
 */
public enum NodeType {
    MAYOR("Alcalde", 1),
    GOVERNOR("Gobernador", 2),
    CONGRESSMAN("Congresista", 2),
    MINISTER("Ministro", 3),
    JUDGE("Juez", 2),
    CONTRACTOR("Contratista", 1),
    BUSINESSMAN("Empresario", 1),
    JOURNALIST("Periodista", 1);

    private final String displayName;
    private final int hierarchyLevel;

    NodeType(String displayName, int hierarchyLevel) {
        this.displayName = displayName;
        this.hierarchyLevel = hierarchyLevel;
    }

    public String getDisplayName() { return displayName; }
    public int getHierarchyLevel() { return hierarchyLevel; }

    /**
     * Obtiene el costo base de soborno para este tipo
     */
    public int getBaseBribeCost() {
        switch (this) {
            case MAYOR:
                return 20000;
            case GOVERNOR:
                return 50000;
            case CONGRESSMAN:
                return 40000;
            case MINISTER:
                return 80000;
            case JUDGE:
                return 60000;
            case CONTRACTOR:
                return 15000;
            case BUSINESSMAN:
                return 25000;
            case JOURNALIST:
                return 10000;
            default:
                return 10000;
        }
    }

    /**
     * Obtiene la descripción del rol
     */
    public String getRoleDescription() {
        switch (this) {
            case MAYOR:
                return "Controla contratos municipales y permisos locales";
            case GOVERNOR:
                return "Influencia regional y acceso a presupuestos departamentales";
            case CONGRESSMAN:
                return "Poder legislativo y conexiones nacionales";
            case MINISTER:
                return "Control de políticas nacionales y grandes presupuestos";
            case JUDGE:
                return "Puede bloquear investigaciones y manipular procesos judiciales";
            case CONTRACTOR:
                return "Genera ingresos constantes a través de obras públicas";
            case BUSINESSMAN:
                return "Facilita lavado de dinero y conexiones internacionales";
            case JOURNALIST:
                return "Controla narrativa pública y puede encubrir escándalos";
            default:
                return "Funciones generales de apoyo";
        }
    }
}
