package com.database.eventmania.backend.DTO;

import java.util.HashMap;

public class EventDTO {
    HashMap<String, Object> map;

    public void addObject(String attributeName, Object object) {
        map.put(attributeName, object);
    }
}
