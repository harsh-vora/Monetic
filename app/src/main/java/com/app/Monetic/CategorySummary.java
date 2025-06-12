package com.app.Monetic;
public class CategorySummary {
    private String name;
    private String paymentMethods;
    private double amount;
    private int percentage;
    private int iconResource;
    public CategorySummary(String name, String paymentMethods, double amount, int percentage, int iconResource) {
        this.name = name;
        this.paymentMethods = paymentMethods;
        this.amount = amount;
        this.percentage = percentage;
        this.iconResource = iconResource;
    }
    // Getters
    public String getName() { return name; }
    public String getPaymentMethods() { return paymentMethods; }
    public double getAmount() { return amount; }
    public int getPercentage() { return percentage; }
    public int getIconResource() { return iconResource; }
}