package com.database.eventmania.backend.DTO;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class EventDTO {
    HashMap<String, Object> map;

    public void addObject(String attributeName, Object object) {
        map.put(attributeName, object);
    }
}
