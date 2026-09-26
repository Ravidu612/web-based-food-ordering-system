package com.foodie.app.entity.enums;

public enum ReportType {
    DAILY_SALES("Daily Sales Summary"),
    MONTHLY_PROFIT("Monthly Profit & Loss Statement"),
    EXPENSE_SUMMARY("Operational Expense Breakdown"),
    CATEGORY_ANALYTICS("Category Popularity & Performance");

    private final String title;

    ReportType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
