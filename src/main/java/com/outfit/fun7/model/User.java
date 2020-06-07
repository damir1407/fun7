package com.outfit.fun7.model;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;


@Entity(name = "User")
public class User {
    @Id
    Long id;

    String username;
    String password;
    int usage;
    String country;

    public User(Long id, String username, String password, int usage, String country) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.usage = usage;
        this.country = country;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + this.id +
                ", username='" + this.username + '\'' +
                ", password='" + this.password + '\'' +
                ", country=" + this.country + '\'' +
                ", usage=" + this.usage + '\'' +
                '}';
    }
}
