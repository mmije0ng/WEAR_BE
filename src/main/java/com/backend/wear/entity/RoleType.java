package com.backend.wear.entity;

public enum RoleType {
    MEMBER("사용자"),
    ADMIN("관리자");

    private final String role;

    RoleType (String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
