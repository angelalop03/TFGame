package com.us.TFG.model;

import com.us.TFG.annotations.Column;
import com.us.TFG.annotations.Table;

@Table(name ="users")
public class User{
    @Column(name = "id", type = "INTEGER", primaryKey = true,notNull = true)
    private int id;  // ID único para la base de datos

    @Column(name = "username", type = "TEXT", unique = true,notNull = true)
    private String username;

    @Column(name = "password", type = "TEXT",notNull = true)
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
