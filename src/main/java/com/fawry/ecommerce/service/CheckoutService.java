package com.fawry.ecommerce.service;

import com.fawry.ecommerce.model.CartItem;
import com.fawry.ecommerce.model.Customer;
import com.fawry.ecommerce.model.Product;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CheckoutService {
    private static final double SHIPPING_FEE_PER_KG = 30.0;

    public void checkout(Customer customer, CartService cart) {
        Objects.requireNonNull(customer, "Customer cannot be null");
        Objects.requireNonNull(cart, "Cart cannot be null");
        validateCart(cart);

        double subtotal = calculateSubtotal(cart.getItems());
        List<CartItem> shippableItems = getShippableItems(cart);
        double shippingFee = calculateShippingFee(shippableItems);
        double totalAmount = subtotal + shippingFee;

        processPayment(customer, totalAmount);
        updateInventory(cart);
        printShipmentNotice(shippableItems);
        printReceipt(cart, subtotal, shippingFee, totalAmount, customer);

        cart.clear();
    }

    private void validateCart(CartService cart) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot checkout with empty cart");
        }
    }

    public double calculateSubtotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    private List<CartItem> getShippableItems(CartService cart) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().isShippable())
                .collect(Collectors.toList());
    }

    private double calculateShippingFee(List<CartItem> shippableItems) {
        double weightKg = shippableItems.stream()
                .mapToDouble(item -> item.getProduct().getWeight() * item.getQuantity())
                .sum() / 1000; // convert grams to kg
        return weightKg * SHIPPING_FEE_PER_KG;
    }

    private void processPayment(Customer customer, double amount) {
        if (customer.getBalance() < amount) {
            throw new IllegalStateException("Insufficient customer balance. Required: "
                    + amount + ", Available: " + customer.getBalance());
        }
        customer.deductBalance(amount);
    }

    private void updateInventory(CartService cart) {
        cart.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.reduceQuantity(item.getQuantity());
        });
    }


    private void printShipmentNotice(List<CartItem> shippableItems) {
        System.out.println("** Shipment notice **");
        double totalWeight = 0;

        for (CartItem item : shippableItems) {
            Product product = item.getProduct();
            double itemWeight = product.getWeight() * item.getQuantity();
            System.out.printf("%dx %s    %.0fg%n",
                    item.getQuantity(),
                    product.getName(),
                    itemWeight * 1000);
            totalWeight += itemWeight;
        }

        System.out.printf("Total package weight %.1fkg%n%n", totalWeight);
    }

    private void printReceipt(CartService cart, double subtotal,
                              double shippingFee, double totalAmount,
                              Customer customer) {
        System.out.println("** Checkout receipt **");
        for (CartItem item : cart.getItems()) {
            System.out.printf("%dx %-15s %.2f%n",
                    item.getQuantity(),
                    item.getProduct().getName(),
                    item.getTotalPrice());
        }
        System.out.println("-------------------");
        System.out.printf("Subtotal    %.2f%n", subtotal);
        System.out.printf("Shipping    %.2f%n", shippingFee);
        System.out.printf("Amount    %.2f%n", totalAmount);
        System.out.printf("Remaining balance: %.2f%n", customer.getBalance());
    }
}