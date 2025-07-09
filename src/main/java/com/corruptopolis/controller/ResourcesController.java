package com.corruptopolis.controller;

import src.main.java.com.corruptopolis.model.Node;
import src.main.java.com.corruptopolis.model.PlayerResources;

public class ResourcesController {

    /**
     * Attempts to bribe the given node.
     * @return true if the player had enough money and the bribe succeeded; false otherwise.
     */
    public boolean bribe(Node node, PlayerResources player) {
        double cost = node.getBribeCost();
        if (player.getDirtyMoney() < cost) {
            return false; // insufficient funds
        }
        // 1) Deduct dirty money
        player.setDirtyMoney(player.getDirtyMoney() - cost);
        // 2) Gain influence from the node
        player.setInfluence(player.getInfluence() + node.getInfluenceYield());
        // 3) Increase suspicion (e.g., +5 points)
        player.setSuspicionLevel(player.getSuspicionLevel() + 5);
        // 4) Mark node as bribed/active
        node.setBribed(true);
        return true;
    }

    /**
     * Extracts resources (money and influence) from a bribed node.
     */
    public void extractResources(Node node, PlayerResources player) {
        if (!node.isBribed()) {
            throw new IllegalStateException("Node has not been bribed yet.");
        }
        // Extract wealth
        player.setDirtyMoney(player.getDirtyMoney() + node.getWealthYield());
        // Extract additional influence
        player.setInfluence(player.getInfluence() + node.getInfluenceYield());
        // Reduce suspicion slightly (e.g., -2 points)
        player.setSuspicionLevel(player.getSuspicionLevel() - 2);
    }
}
