package com.backend.wear.entity;

public enum Style {
    VINTAGE("빈티지"),
    FEMININ("페미닌"),
    CASUAL("캐쥬얼");
    // 다른 환경 레벨 값들을 추가할 수 있음

    private final String label;

    Style(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
