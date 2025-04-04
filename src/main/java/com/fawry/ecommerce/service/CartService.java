package com.fawry.ecommerce.service;


import com.fawry.ecommerce.model.CartItem;
import com.fawry.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CartService {
    private final List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        Objects.requireNonNull(product, "Product cannot be null");
        validateAddItem(product, quantity);

        findExistingItem(product.getName())
                .ifPresentOrElse(
                        item -> updateExistingItem(item, product, quantity),
                        () -> addNewItem(product, quantity)
                );
    }

    private void validateAddItem(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (product.isExpired()) throw new IllegalStateException(product.getName() + " is expired");
        if (product.getQuantity() < quantity) {
            throw new IllegalStateException("Not enough stock for " + product.getName());
        }
    }
    private Optional<CartItem> findExistingItem(String productName) {
        return items.stream()
                .filter(item -> item.getProduct().getName().equalsIgnoreCase(productName))
                .findFirst();
    }

    private void updateExistingItem(CartItem item, Product product, int additionalQuantity) {
        int newQuantity = item.getQuantity() + additionalQuantity;
        if (product.getQuantity() < newQuantity) {
            throw new IllegalStateException("Not enough stock for " + product.getName());
        }
        item.updateQuantity(newQuantity);
    }

    private void addNewItem(Product product, int quantity) {
        items.add(new CartItem(product, quantity));
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }
}
