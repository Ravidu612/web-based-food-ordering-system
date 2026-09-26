package com.foodie.app.entity.enums;

public enum ExpenseCategory {
    RAW_INGREDIENTS("Raw Food Ingredients & Supplies"),
    UTILITIES("Electricity, Gas & Water Utilities"),
    SALARIES("Staff Salaries & Wages"),
    EQUIPMENT("Kitchen Equipment & Maintenance"),
    MARKETING("Marketing & Advertising"),
    MISCELLANEOUS("Other Operating Expenses");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
