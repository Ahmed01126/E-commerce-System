package com.fawry.ecommerce.model;

public final class Customer {
    private final String name;
    private double balance;

    public Customer(String name, double initialBalance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        this.name = name;
        this.balance = initialBalance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deductBalance(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deduction amount must be positive");
        }
        if (balance < amount) {
            throw new IllegalStateException("Insufficient balance");
        }
        this.balance = balance - amount;
    }
}
