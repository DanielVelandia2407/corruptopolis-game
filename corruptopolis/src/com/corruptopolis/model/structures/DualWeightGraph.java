package com.corruptopolis.model.structures;

import com.corruptopolis.model.entities.PoliticalNode;
import java.util.*;

/**
 * Grafo con doble peso para análisis de riesgo y reputación
 */
public class DualWeightGraph {
    private Map<String, PoliticalNode> nodes;
    private Map<String, Map<String, EdgeWeight>> adjacencyList;
    
    public static class EdgeWeight {
        public final int suspicionRisk;
        public final int reputationGain;
        
        public EdgeWeight(int suspicionRisk, int reputationGain) {
            this.suspicionRisk = suspicionRisk;
            this.reputationGain = reputationGain;
        }
    }
    
    public DualWeightGraph() {
        this.nodes = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }
    
    public void addNode(PoliticalNode node) {
        nodes.put(node.getId(), node);
        adjacencyList.putIfAbsent(node.getId(), new HashMap<>());
    }
    
    public void addEdge(String nodeId1, String nodeId2) {
        PoliticalNode node1 = nodes.get(nodeId1);
        PoliticalNode node2 = nodes.get(nodeId2);
        
        if (node1 != null && node2 != null) {
            int avgSuspicion = (getSuspicionProbability(node1) + getSuspicionProbability(node2)) / 2;
            int avgReputation = (getReputationGain(node1) + getReputationGain(node2)) / 2;
            
            EdgeWeight weight = new EdgeWeight(avgSuspicion, avgReputation);
            adjacencyList.get(nodeId1).put(nodeId2, weight);
            adjacencyList.get(nodeId2).put(nodeId1, weight);
        }
    }
    
    public void generateConnections() {
        Random random = new Random();
        List<String> nodeIds = new ArrayList<>(nodes.keySet());
        
        for (int i = 0; i < nodeIds.size(); i++) {
            for (int j = i + 1; j < nodeIds.size(); j++) {
                String nodeId1 = nodeIds.get(i);
                String nodeId2 = nodeIds.get(j);
                
                if (shouldConnect(nodes.get(nodeId1), nodes.get(nodeId2), random)) {
                    addEdge(nodeId1, nodeId2);
                }
            }
        }
    }
    
    private boolean shouldConnect(PoliticalNode node1, PoliticalNode node2, Random random) {
        if (node1.getNodeType() == node2.getNodeType()) return random.nextDouble() < 0.6;
        if (areRelatedTypes(node1.getNodeType(), node2.getNodeType())) return random.nextDouble() < 0.4;
        return random.nextDouble() < 0.2;
    }
    
    private boolean areRelatedTypes(com.corruptopolis.model.entities.NodeType type1, 
                                   com.corruptopolis.model.entities.NodeType type2) {
        return (type1.name().equals("MAYOR") && type2.name().equals("CONTRACTOR")) ||
               (type1.name().equals("JUDGE") && type2.name().equals("BUSINESSMAN")) ||
               (type1.name().equals("JOURNALIST") && type2.name().equals("MAYOR"));
    }
    
    private int getSuspicionProbability(PoliticalNode node) {
        switch (node.getNodeType().name()) {
            case "JUDGE": return 70 + new Random().nextInt(25);
            case "GOVERNOR": return 60 + new Random().nextInt(20);
            case "BUSINESSMAN": return 35 + new Random().nextInt(20);
            case "MAYOR": return 25 + new Random().nextInt(15);
            case "CONTRACTOR": return 20 + new Random().nextInt(20);
            case "JOURNALIST": return 15 + new Random().nextInt(15);
            default: return 30 + new Random().nextInt(20);
        }
    }
    
    private int getReputationGain(PoliticalNode node) {
        switch (node.getNodeType().name()) {
            case "JUDGE": return 80 + new Random().nextInt(15);
            case "GOVERNOR": return 70 + new Random().nextInt(20);
            case "JOURNALIST": return 60 + new Random().nextInt(25);
            case "BUSINESSMAN": return 40 + new Random().nextInt(25);
            case "MAYOR": return 30 + new Random().nextInt(20);
            case "CONTRACTOR": return 15 + new Random().nextInt(15);
            default: return 25 + new Random().nextInt(20);
        }
    }
    
    public List<String> findSafestPath(String start, String end) {
        return dijkstra(start, end, true);
    }
    
    public List<String> findBestReputationPath(String start, String end) {
        return dijkstra(start, end, false);
    }
    
    private List<String> dijkstra(String start, String end, boolean optimizeForSafety) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        
        for (String nodeId : nodes.keySet()) {
            distances.put(nodeId, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        queue.offer(start);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) break;
            
            for (Map.Entry<String, EdgeWeight> neighbor : adjacencyList.get(current).entrySet()) {
                String neighborId = neighbor.getKey();
                EdgeWeight weight = neighbor.getValue();
                
                int edgeWeight = optimizeForSafety ? 
                    weight.suspicionRisk : 
                    (100 - weight.reputationGain);
                
                int newDistance = distances.get(current) + edgeWeight;
                
                if (newDistance < distances.get(neighborId)) {
                    distances.put(neighborId, newDistance);
                    previous.put(neighborId, current);
                    queue.offer(neighborId);
                }
            }
        }
        
        return reconstructPath(previous, start, end);
    }
    
    private List<String> reconstructPath(Map<String, String> previous, String start, String end) {
        List<String> path = new ArrayList<>();
        String current = end;
        
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        
        return path.isEmpty() || !path.get(0).equals(start) ? new ArrayList<>() : path;
    }
    
    // Getters para el panel
    public Collection<PoliticalNode> getAllNodes() {
        return nodes.values();
    }
    
    public PoliticalNode getNode(String nodeId) {
        return nodes.get(nodeId);
    }
    
    public Map<String, EdgeWeight> getConnections(String nodeId) {
        return adjacencyList.getOrDefault(nodeId, new HashMap<>());
    }
    
    public int getNodeSuspicionProbability(String nodeId) {
        PoliticalNode node = nodes.get(nodeId);
        return node != null ? getSuspicionProbability(node) : 0;
    }
    
    public int getNodeReputationGain(String nodeId) {
        PoliticalNode node = nodes.get(nodeId);
        return node != null ? getReputationGain(node) : 0;
    }
    
    public void clear() {
        nodes.clear();
        adjacencyList.clear();
    }
}