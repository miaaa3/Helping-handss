package com.example.HelpingHands.Listener;

import jakarta.persistence.PrePersist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class EntityCreatedTimestampListener {
    @PrePersist
    public void setCreatedAt(Object entity) {
        try {
            Method setCreatedAtMethod = entity.getClass().getMethod("setCreatedAt", LocalDateTime.class);
            if (setCreatedAtMethod != null) {
                setCreatedAtMethod.invoke(entity, LocalDateTime.now());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage());
        }
    }
}
