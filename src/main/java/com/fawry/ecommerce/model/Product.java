package com.fawry.ecommerce.model;

import java.time.LocalDate;
import java.util.Objects;

public final class Product {
    private final String name;
    private final double price;
    private int quantity;

    private boolean isExpirable;
    private LocalDate expiryDate;

    private boolean isShippable;
    private double weight;

    public Product(String name, double price, int quantity) {
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
        this.price = price;

        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
    }

    public Product setExpirable(LocalDate expiryDate) {
        this.isExpirable = true;
        this.expiryDate = expiryDate;
        return this;
    }

    public Product setShippable(double weightInGrams) {
        this.isShippable = true;
        this.weight = weightInGrams; // Store in grams
        return this;
    }

    public boolean isExpired() {
        return isExpirable && LocalDate.now().isAfter(expiryDate);
    }

    public boolean isShippable() {
        return isShippable;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void reduceQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (quantity < amount) throw new IllegalStateException("Not enough stock");
        quantity -= amount;
    }
}