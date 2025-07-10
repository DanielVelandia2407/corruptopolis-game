package com.corruptopolis.model;

import com.corruptopolis.model.entities.*;
import com.corruptopolis.model.structures.*;
import com.corruptopolis.model.structures.DualWeightGraph;
import com.corruptopolis.model.events.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.Timer;

/**
 * Modelo principal del juego que contiene toda la lógica de negocio
 * Implementa el patrón Observer para notificar cambios a la vista
 */
public class Game {
    private static final AtomicInteger NODE_COUNTER = new AtomicInteger(1);

    private Player player;
    private CorruptionTree corruptionTree;
    private ConnectionGraph connectionGraph;
    private DualWeightGraph dualWeightGraph;
    private EventQueue eventQueue;
    private EventManager eventManager;

    private int currentTurn;
    private boolean gameActive;
    private String gameStatus;
    private List<GameObserver> observers;
    
    // Sistema de restricción de acciones
    private int actionsThisTurn;
    private static final int MAX_ACTIONS_PER_TURN = 4;
    private Timer turnTimer;
    private static final int TURN_DURATION_MS = 10000; // 10 segundos
    private long turnStartTime;

    /**
     * Constructor que inicializa el juego
     */
    public Game(String playerName) {
        this.player = new Player(playerName);
        this.corruptionTree = new CorruptionTree(playerName);
        this.connectionGraph = new ConnectionGraph();
        this.dualWeightGraph = new DualWeightGraph();
        this.eventQueue = new EventQueue();
        this.eventManager = new EventManager();

        this.currentTurn = 1;
        this.gameActive = true;
        this.gameStatus = "Iniciando imperio de corrupción...";
        this.observers = new ArrayList<>();
        this.actionsThisTurn = 0;

        initializeGame();
        startTurnTimer();
    }

    /**
     * Inicializa los elementos básicos del juego
     */
    private void initializeGame() {
        generateInitialContacts();
        scheduleInitialEvents();
        notifyObservers("game_initialized");
    }

    /**
     * Genera contactos iniciales disponibles para sobornar
     */
    private void generateInitialContacts() {
        String[] mayorNames = {"Carlos Mendoza", "Ana Gutiérrez", "Luis Rodríguez"};
        String[] contractorNames = {"Constructora Elite", "Obras y Proyectos SAS", "Infraestructura Total"};

        for (String name : mayorNames) {
            PoliticalNode mayor = new PoliticalNode(
                    "mayor_" + NODE_COUNTER.getAndIncrement(),
                    name,
                    NodeType.MAYOR,
                    20000 + new Random().nextInt(15000)
            );
            connectionGraph.addAvailableContact(mayor);
            dualWeightGraph.addNode(mayor);
        }

        for (String name : contractorNames) {
            PoliticalNode contractor = new PoliticalNode(
                    "contractor_" + NODE_COUNTER.getAndIncrement(),
                    name,
                    NodeType.CONTRACTOR,
                    15000 + new Random().nextInt(10000)
            );
            connectionGraph.addAvailableContact(contractor);
            dualWeightGraph.addNode(contractor);
        }
    }

    /**
     * Programa eventos iniciales
     */
    private void scheduleInitialEvents() {
        eventQueue.addEvent(new GameEvent(EventType.ELECTION_OPPORTUNITY, 3, "Elecciones regionales"));
        eventQueue.addEvent(new GameEvent(EventType.MEDIA_ATTENTION, 5, "Investigación periodística"));
        
        // Generar conexiones del grafo
        dualWeightGraph.generateConnections();
    }

    /**
     * Intenta sobornar un contacto disponible
     */
    public BribeResult attemptBribe(String contactId) {
        if (!gameActive) {
            return new BribeResult(false, "El juego ha terminado");
        }
        
        if (actionsThisTurn >= MAX_ACTIONS_PER_TURN) {
            return new BribeResult(false, "Máximo de acciones por turno alcanzado");
        }

        PoliticalNode contact = connectionGraph.getAvailableContact(contactId);
        if (contact == null) {
            return new BribeResult(false, "Contacto no disponible");
        }

        int bribeCost = contact.calculateCurrentBribeCost();
        if (!player.attemptBribe(bribeCost)) {
            return new BribeResult(false, "Fondos insuficientes");
        }

        boolean success = calculateBribeSuccess(contact);
        if (success) {
            corruptionTree.addNode("player", contact);
            corruptionTree.activateNode(contact.getId());
            connectionGraph.removeAvailableContact(contactId);

            generateNewContacts(contact);
            player.recordSuccessfulOperation();
            actionsThisTurn++;

            notifyObservers("bribe_success");
            return new BribeResult(true, contact.getName() + " se ha unido a tu red");
        } else {
            player.recordFailedOperation();
            player.increaseSuspicion(10);
            actionsThisTurn++;

            notifyObservers("bribe_failed");
            return new BribeResult(false, "El soborno falló. Nivel de sospecha aumentado");
        }
    }

    /**
     * Calcula si un soborno es exitoso
     */
    private boolean calculateBribeSuccess(PoliticalNode contact) {
        double baseSuccessRate = 0.7;

        if (player.getGeneralSuspicionLevel() > 50) {
            baseSuccessRate -= 0.2;
        }

        if (player.getPublicReputation() > 70) {
            baseSuccessRate += 0.1;
        }

        if (contact.getNodeType().getHierarchyLevel() > 2) {
            baseSuccessRate -= 0.15;
        }

        return Math.random() < baseSuccessRate;
    }

    /**
     * Genera nuevos contactos basados en el nodo reclutado
     */
    private void generateNewContacts(PoliticalNode recruitedNode) {
        Random random = new Random();
        int newContacts = 1 + random.nextInt(2);

        for (int i = 0; i < newContacts; i++) {
            NodeType newType = generateRelatedNodeType(recruitedNode.getNodeType());
            String newName = generateNodeName(newType);

            PoliticalNode newContact = new PoliticalNode(
                    newType.name().toLowerCase() + "_" + NODE_COUNTER.getAndIncrement(),
                    newName,
                    newType,
                    calculateBribeCost(newType)
            );

            connectionGraph.addAvailableContact(newContact);
            dualWeightGraph.addNode(newContact);
        }
        
        // Regenerar conexiones del grafo
        dualWeightGraph.generateConnections();
        
    }

    /**
     * Extrae recursos de la red de corrupción
     */
    public void extractResources() {
        if (!gameActive) return;
        if (actionsThisTurn >= MAX_ACTIONS_PER_TURN) return;

        int totalInfluence = corruptionTree.calculateTotalInfluence();
        int totalWealth = corruptionTree.calculateTotalWealth();

        player.addInfluence(totalInfluence);
        player.addDirtyMoney(totalWealth);
        actionsThisTurn++;

        double networkRisk = corruptionTree.calculateNetworkRisk();
        if (networkRisk > 70) {
            player.increaseSuspicion(3);
        }

        notifyObservers("resources_extracted");
    }

    /**
     * Realiza una operación de encubrimiento
     */
    public boolean performCoverUp(int investment) {
        if (!gameActive) return false;
        if (actionsThisTurn >= MAX_ACTIONS_PER_TURN) return false;

        boolean success = player.performCoverUp(investment);
        if (success) {
            player.recordSuccessfulOperation();
            actionsThisTurn++;
            notifyObservers("coverup_success");
        }
        return success;
    }

    /**
     * Avanza al siguiente turno
     */
    public void nextTurn() {
        if (!gameActive) return;

        currentTurn++;
        player.nextTurn();
        actionsThisTurn = 0; // Resetear contador de acciones

        processEvents();
        checkGameConditions();
        
        // Reiniciar timer del turno
        startTurnTimer();

        notifyObservers("turn_advanced");
    }
    
    /**
     * Inicia el timer del turno
     */
    private void startTurnTimer() {
        if (turnTimer != null) {
            turnTimer.stop();
        }
        
        turnStartTime = System.currentTimeMillis();
        turnTimer = new Timer(TURN_DURATION_MS, e -> {
            if (gameActive) {
                nextTurn();
            }
        });
        turnTimer.setRepeats(false);
        turnTimer.start();
    }

    /**
     * Procesa eventos del turno actual
     */
    private void processEvents() {
        List<GameEvent> currentEvents = eventQueue.getEventsForTurn(currentTurn);

        for (GameEvent event : currentEvents) {
            processEvent(event);
        }

        if (currentTurn % 3 == 0) {
            scheduleRandomEvent();
        }
    }

    /**
     * Procesa un evento específico
     */
    private void processEvent(GameEvent event) {
        switch (event.getType()) {
            case BETRAYAL:
                handleBetrayal();
                break;
            case INVESTIGATION:
                handleInvestigation();
                break;
            case MEDIA_LEAK:
                handleMediaLeak();
                break;
            case ELECTION_OPPORTUNITY:
                handleElectionOpportunity();
                break;
            default:
                break;
        }

        notifyObservers("event_processed");
    }

    /**
     * Maneja eventos de traición
     */
    private void handleBetrayal() {
        List<PoliticalNode> activeNodes = corruptionTree.getActiveNodes();
        if (activeNodes.isEmpty()) return;

        for (PoliticalNode node : activeNodes) {
            if (Math.random() < node.calculateBetrayalProbability()) {
                node.setCurrentState(NodeState.PROTECTED_WITNESS);
                player.increaseSuspicion(20);
                gameStatus = node.getName() + " ha traicionado a la organización!";
                return;
            }
        }
    }

    /**
     * Maneja investigaciones
     */
    private void handleInvestigation() {
        player.increaseSuspicion(15);
        gameStatus = "Una investigación federal está en curso...";
    }

    /**
     * Maneja filtraciones a medios
     */
    private void handleMediaLeak() {
        player.modifyReputation(-10);
        player.increaseSuspicion(10);
        gameStatus = "Los medios han filtrado información comprometedora!";
    }

    /**
     * Maneja oportunidades electorales
     */
    private void handleElectionOpportunity() {
        gameStatus = "Oportunidad electoral: puedes expandir tu influencia!";
    }

    /**
     * Programa un evento aleatorio
     */
    private void scheduleRandomEvent() {
        EventType[] eventTypes = EventType.values();
        EventType randomType = eventTypes[new Random().nextInt(eventTypes.length)];

        int turnsAhead = 2 + new Random().nextInt(3);
        GameEvent randomEvent = new GameEvent(randomType, currentTurn + turnsAhead, "Evento aleatorio");

        eventQueue.addEvent(randomEvent);
    }

    /**
     * Verifica condiciones de fin de juego
     */
    private void checkGameConditions() {
        if (player.hasLost()) {
            gameActive = false;
            gameStatus = "¡Has sido capturado! La justicia ha prevalecido.";
            notifyObservers("game_over");
        } else if (player.isInCriticalDanger()) {
            gameStatus = "¡PELIGRO CRÍTICO! La justicia se acerca...";
        }
    }

    // Métodos de utilidad
    private NodeType generateRelatedNodeType(NodeType baseType) {
        switch (baseType) {
            case MAYOR:
                return Math.random() < 0.5 ? NodeType.CONTRACTOR : NodeType.BUSINESSMAN;
            case CONTRACTOR:
                return Math.random() < 0.6 ? NodeType.MAYOR : NodeType.JUDGE;
            default:
                return NodeType.CONTRACTOR;
        }
    }

    private String generateNodeName(NodeType type) {
        String[] firstNames = {"María", "Carlos", "Ana", "Luis", "Patricia", "Roberto"};
        String[] lastNames = {"González", "Rodríguez", "Martínez", "López", "García", "Hernández"};

        Random random = new Random();
        return firstNames[random.nextInt(firstNames.length)] + " " +
                lastNames[random.nextInt(lastNames.length)];
    }

    private int calculateBribeCost(NodeType type) {
        int baseCost = 10000;
        return baseCost * type.getHierarchyLevel() + new Random().nextInt(15000);
    }

    // Patrón Observer
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String eventType) {
        for (GameObserver observer : observers) {
            observer.onGameStateChanged(eventType);
        }
    }

    // Getters para la vista
    public Player getPlayer() { return player; }
    public CorruptionTree getCorruptionTree() { return corruptionTree; }
    public ConnectionGraph getConnectionGraph() { return connectionGraph; }
    public DualWeightGraph getDualWeightGraph() { return dualWeightGraph; }
    public int getCurrentTurn() { return currentTurn; }
    public boolean isGameActive() { return gameActive; }
    public String getGameStatus() { return gameStatus; }
    public int getActionsThisTurn() { return actionsThisTurn; }
    public int getMaxActionsPerTurn() { return MAX_ACTIONS_PER_TURN; }
    public int getRemainingTurnTime() {
        if (turnTimer != null && turnTimer.isRunning()) {
            long elapsed = System.currentTimeMillis() - turnStartTime;
            int remaining = (int)((TURN_DURATION_MS - elapsed) / 1000);
            return Math.max(0, remaining);
        }
        return 0;
    }

    /**
     * Interface para el patrón Observer
     */
    public interface GameObserver {
        void onGameStateChanged(String eventType);
    }

    /**
     * Clase para encapsular resultados de sobornos
     */
    public static class BribeResult {
        private final boolean success;
        private final String message;

        public BribeResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}