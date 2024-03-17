package com.backend.wear.entity;

//환경 레벨
public enum EnvironmentLevel {
    SEED("씨앗"),
    SAPLING("새싹"),
    COTTON("목화");
    // 다른 환경 레벨 값들을 추가할 수 있음

    private final String label;

    EnvironmentLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}