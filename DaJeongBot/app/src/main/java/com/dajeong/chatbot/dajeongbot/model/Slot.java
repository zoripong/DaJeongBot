package com.dajeong.chatbot.dajeongbot.model;

/**
 * Created by Mirim on 2018-09-11.
 */

public class Slot {
/*
* {"id":"1","label":"응","type":"btn","value":"응"}
* */
    private int id;
    private String label;
    private String value;

    public Slot(int id, String label, String value) {
        this.id = id;
        this.label = label;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
