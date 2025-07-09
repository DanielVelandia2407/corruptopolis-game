package com.corruptopolis.model.structures;

import com.corruptopolis.model.entities.PoliticalNode;
import java.util.*;

public class ConnectionGraph {
    private Map<String, PoliticalNode> availableContacts;

    public ConnectionGraph() {
        this.availableContacts = new HashMap<>();
    }

    public void addAvailableContact(PoliticalNode contact) {
        availableContacts.put(contact.getId(), contact);
    }

    public void removeAvailableContact(String contactId) {
        availableContacts.remove(contactId);
    }

    public PoliticalNode getAvailableContact(String contactId) {
        return availableContacts.get(contactId);
    }

    public List<PoliticalNode> getAllAvailableContacts() {
        return new ArrayList<>(availableContacts.values());
    }
}

