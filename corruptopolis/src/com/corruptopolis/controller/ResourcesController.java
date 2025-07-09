package com.corruptopolis.controller;

import com.corruptopolis.model.*;
import com.corruptopolis.model.entities.PoliticalNode;

public class ResourcesController {

    /**
     * Attempts to bribe the given node.
     * 
     * @return true if the player had enough money and the bribe succeeded; false
     *         otherwise.
     */
    public boolean bribe(PoliticalNode node, PlayerResources player) {
        double cost = node.getBribeCost();
        if (player.getDirtyMoney() < cost) {
            return false;
        }
        // 1) Descontar dinero
        player.setDirtyMoney(player.getDirtyMoney() - cost);
        // 2) Ganar influencia
        player.setInfluence(player.getInfluence() + node.getInfluenceContribution());
        // 3) Aumentar sospecha
        player.setSuspicionLevel(player.getSuspicionLevel() + 5);
        // 4) Activar el nodo tras el soborno
        node.activate();
        return true;
    }

    /**
     * Extracts resources (money and influence) from a bribed node.
     */
    public void extractResources(PoliticalNode node, PlayerResources player) {
        if (!node.canBeBribed()) {
            throw new IllegalStateException("Node has not been bribed yet.");
        }
        // Extract wealth
        player.setDirtyMoney(player.getDirtyMoney() + node.getWealthContribution());
        // Extract additional influence
        player.setInfluence(player.getInfluence() + node.getInfluenceContribution());
        // Reduce suspicion slightly (e.g., -2 points)
        player.setSuspicionLevel(player.getSuspicionLevel() - 2);
    }
}
