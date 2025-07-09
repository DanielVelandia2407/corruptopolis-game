package com.corruptopolis.model.structures;

import com.corruptopolis.model.entities.*;
import java.util.*;

/**
 * Árbol n-ario que representa la jerarquía de corrupción
 * Implementa operaciones para gestionar la red de nodos políticos
 */
public class CorruptionTree {
    private PoliticalNode root;
    private Map<String, PoliticalNode> nodeRegistry;
    private int totalNodes;
    private int activeNodes;

    /**
     * Constructor que inicializa el árbol con el jugador como raíz
     */
    public CorruptionTree(String playerName) {
        this.root = new PoliticalNode("player", playerName, NodeType.MAYOR, 0);
        this.root.activate();
        this.nodeRegistry = new HashMap<>();
        this.nodeRegistry.put("player", root);
        this.totalNodes = 1;
        this.activeNodes = 1;
    }

    /**
     * Añade un nuevo nodo como subordinado de un nodo padre
     */
    public boolean addNode(String parentId, PoliticalNode newNode) {
        PoliticalNode parent = nodeRegistry.get(parentId);
        if (parent == null || !parent.getCurrentState().isOperational()) {
            return false;
        }

        parent.addSubordinate(newNode);
        nodeRegistry.put(newNode.getId(), newNode);
        totalNodes++;

        if (newNode.getCurrentState().isOperational()) {
            activeNodes++;
        }

        return true;
    }

    /**
     * Activa un nodo después de un soborno exitoso
     */
    public boolean activateNode(String nodeId) {
        PoliticalNode node = nodeRegistry.get(nodeId);
        if (node == null || node.getCurrentState().isCompromised()) {
            return false;
        }

        boolean wasInactive = !node.getCurrentState().isOperational();
        node.activate();

        if (wasInactive) {
            activeNodes++;
        }

        return true;
    }

    /**
     * Elimina un nodo y todo su subárbol
     */
    public boolean removeNode(String nodeId) {
        if (nodeId.equals("player")) {
            return false;
        }

        PoliticalNode node = nodeRegistry.get(nodeId);
        if (node == null) {
            return false;
        }

        int removedNodes = removeSubtree(node);
        totalNodes -= removedNodes;

        PoliticalNode parent = node.getSuperior();
        if (parent != null) {
            parent.getSubordinates().remove(node);
        }

        return true;
    }

    /**
     * Elimina recursivamente un subárbol
     */
    private int removeSubtree(PoliticalNode node) {
        int removedCount = 0;

        List<PoliticalNode> subordinates = new ArrayList<>(node.getSubordinates());
        for (PoliticalNode subordinate : subordinates) {
            removedCount += removeSubtree(subordinate);
        }

        if (node.getCurrentState().isOperational()) {
            activeNodes--;
        }

        nodeRegistry.remove(node.getId());
        removedCount++;

        return removedCount;
    }

    /**
     * Calcula la influencia total generada por la red
     */
    public int calculateTotalInfluence() {
        return calculateInfluenceRecursive(root);
    }

    private int calculateInfluenceRecursive(PoliticalNode node) {
        int totalInfluence = 0;

        if (node.getCurrentState().isOperational()) {
            totalInfluence += node.getInfluenceContribution();
        }

        for (PoliticalNode subordinate : node.getSubordinates()) {
            totalInfluence += calculateInfluenceRecursive(subordinate);
        }

        return totalInfluence;
    }

    /**
     * Calcula la riqueza total generada por la red
     */
    public int calculateTotalWealth() {
        return calculateWealthRecursive(root);
    }

    private int calculateWealthRecursive(PoliticalNode node) {
        int totalWealth = 0;

        if (node.getCurrentState().isOperational()) {
            totalWealth += node.getWealthContribution();
        }

        for (PoliticalNode subordinate : node.getSubordinates()) {
            totalWealth += calculateWealthRecursive(subordinate);
        }

        return totalWealth;
    }

    /**
     * Calcula el riesgo total de exposición de la red
     */
    public double calculateNetworkRisk() {
        return calculateRiskRecursive(root) / Math.max(1, activeNodes);
    }

    private double calculateRiskRecursive(PoliticalNode node) {
        double totalRisk = 0;

        if (node.getCurrentState().isOperational()) {
            totalRisk += node.getExposureRisk();
        }

        for (PoliticalNode subordinate : node.getSubordinates()) {
            totalRisk += calculateRiskRecursive(subordinate);
        }

        return totalRisk;
    }

    /**
     * Encuentra nodos por tipo
     */
    public List<PoliticalNode> findNodesByType(NodeType type) {
        List<PoliticalNode> result = new ArrayList<>();
        findNodesByTypeRecursive(root, type, result);
        return result;
    }

    private void findNodesByTypeRecursive(PoliticalNode node, NodeType type, List<PoliticalNode> result) {
        if (node.getNodeType() == type) {
            result.add(node);
        }

        for (PoliticalNode subordinate : node.getSubordinates()) {
            findNodesByTypeRecursive(subordinate, type, result);
        }
    }

    /**
     * Encuentra nodos por estado
     */
    public List<PoliticalNode> findNodesByState(NodeState state) {
        List<PoliticalNode> result = new ArrayList<>();
        findNodesByStateRecursive(root, state, result);
        return result;
    }

    private void findNodesByStateRecursive(PoliticalNode node, NodeState state, List<PoliticalNode> result) {
        if (node.getCurrentState() == state) {
            result.add(node);
        }

        for (PoliticalNode subordinate : node.getSubordinates()) {
            findNodesByStateRecursive(subordinate, state, result);
        }
    }

    /**
     * Obtiene todos los nodos activos
     */
    public List<PoliticalNode> getActiveNodes() {
        return findNodesByState(NodeState.ACTIVE);
    }

    /**
     * Obtiene la profundidad máxima del árbol
     */
    public int getMaxDepth() {
        return getDepthRecursive(root, 0);
    }

    private int getDepthRecursive(PoliticalNode node, int currentDepth) {
        if (node.getSubordinates().isEmpty()) {
            return currentDepth;
        }

        int maxDepth = currentDepth;
        for (PoliticalNode subordinate : node.getSubordinates()) {
            maxDepth = Math.max(maxDepth, getDepthRecursive(subordinate, currentDepth + 1));
        }

        return maxDepth;
    }

    /**
     * Recorrido en anchura del árbol
     */
    public List<PoliticalNode> breadthFirstTraversal() {
        List<PoliticalNode> result = new ArrayList<>();
        Queue<PoliticalNode> queue = new LinkedList<>();

        queue.offer(root);

        while (!queue.isEmpty()) {
            PoliticalNode current = queue.poll();
            result.add(current);

            for (PoliticalNode subordinate : current.getSubordinates()) {
                queue.offer(subordinate);
            }
        }

        return result;
    }

    /**
     * Recorrido en profundidad del árbol
     */
    public List<PoliticalNode> depthFirstTraversal() {
        List<PoliticalNode> result = new ArrayList<>();
        depthFirstRecursive(root, result);
        return result;
    }

    private void depthFirstRecursive(PoliticalNode node, List<PoliticalNode> result) {
        result.add(node);

        for (PoliticalNode subordinate : node.getSubordinates()) {
            depthFirstRecursive(subordinate, result);
        }
    }

    /**
     * Obtiene estadísticas del árbol
     */
    public String getTreeStats() {
        return String.format(
                "Nodos Totales: %d | Activos: %d | Profundidad: %d | Influencia: %d | Riqueza: %d | Riesgo: %.1f%%",
                totalNodes, activeNodes, getMaxDepth(), calculateTotalInfluence(),
                calculateTotalWealth(), calculateNetworkRisk()
        );
    }

    // Getters
    public PoliticalNode getRoot() { return root; }
    public int getTotalNodes() { return totalNodes; }
    public int getActiveNodesCount() { return activeNodes; }
    public Map<String, PoliticalNode> getNodeRegistry() { return new HashMap<>(nodeRegistry); }
    public PoliticalNode getNode(String nodeId) { return nodeRegistry.get(nodeId); }
}
