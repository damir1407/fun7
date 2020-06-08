package com.outfit.fun7.model;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;


@Entity(name = "User")
public class User {
    @Id
    Long id;
    int usage;

    public User(Long id, int usage) {
        this.id = id;
        this.usage = usage;
    }

    public long getId() {
        return this.id;
    }

    public int getUsage() {
        return this.usage;
    }

    public void incrementUsage() {
        this.usage = this.usage + 1;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": " + "\"" + this.id + "\"" +
                ", \"usage\": " + "\"" + this.usage + "\"" +
                "}";
    }
}
