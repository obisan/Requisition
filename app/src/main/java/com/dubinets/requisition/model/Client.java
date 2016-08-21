package com.dubinets.requisition.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by dubinets on 21.08.2016.
 */
public class Client {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "client_name")
    private String client_name;

    @DatabaseField(columnName = "client_address")
    private String client_address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", client_name='" + client_name + '\'' +
                ", client_address='" + client_address + '\'' +
                '}';
    }
}
