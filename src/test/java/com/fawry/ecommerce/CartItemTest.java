package com.fawry.ecommerce;

import com.fawry.ecommerce.model.CartItem;
import com.fawry.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {
    private final Product sampleProduct = new Product("Test", 10.0, 5);

    @Test
    void constructor_shouldThrow_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> new CartItem(null, 1));
    }

    @Test
    void constructor_shouldThrow_whenQuantityIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(sampleProduct, 0));
        assertThrows(IllegalArgumentException.class, () -> new CartItem(sampleProduct, -1));
    }

    @Test
    void getTotalPrice_shouldCalculateCorrectly() {
        CartItem item = new CartItem(sampleProduct, 3);
        assertEquals(30.0, item.getTotalPrice());
    }

    @Test
    void updateQuantity_shouldThrow_whenInvalidQuantity() {
        CartItem item = new CartItem(sampleProduct, 1);
        assertThrows(IllegalArgumentException.class, () -> item.updateQuantity(0));
        assertThrows(IllegalArgumentException.class, () -> item.updateQuantity(-5));
    }

    @Test
    void updateQuantity_shouldUpdateSuccessfully() {
        CartItem item = new CartItem(sampleProduct, 1);
        item.updateQuantity(5);
        assertEquals(5, item.getQuantity());
    }
}