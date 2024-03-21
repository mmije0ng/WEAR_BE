package com.backend.wear.entity;

//환경 레벨
public enum EnvironmentLevel {
    SEED("씨앗"),
    SAPLING("새싹"),
    COTTON("목화"),

    FLOWER("꽃"),

    CLOTHES("옷");

    private final String label;

    EnvironmentLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}