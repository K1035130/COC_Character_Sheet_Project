package com.coc.sheet.model;

public class Skill {

    private static final int BASE = 5;

    private String name;
    private int addValue;

    public Skill() {
    }

    public Skill(String name, int addValue) {
        this.name = name;
        this.addValue = addValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddValue() {
        return addValue;
    }

    public void setAddValue(int addValue) {
        this.addValue = addValue;
    }

    public int getTotalValue() {
        return addValue + BASE;
    }
}
