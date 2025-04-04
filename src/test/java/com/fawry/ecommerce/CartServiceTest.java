package com.fawry.ecommerce;

import com.fawry.ecommerce.model.Product;
import com.fawry.ecommerce.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {
    private CartService cartService;
    private Product product;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
        product = new Product("Test", 10.0, 5);
    }

    @Test
    void addItem_shouldThrow_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> cartService.addItem(null, 1));
    }

    @Test
    void addItem_shouldThrow_whenQuantityInvalid() {
        assertThrows(IllegalArgumentException.class, () -> cartService.addItem(product, 0));
        assertThrows(IllegalArgumentException.class, () -> cartService.addItem(product, -1));
    }

    @Test
    void addItem_shouldThrow_whenProductExpired() {
        Product expired = new Product("Expired", 10, 5)
                .setExpirable(LocalDate.now().minusDays(1));
        assertThrows(IllegalStateException.class, () -> cartService.addItem(expired, 1));
    }

    @Test
    void addItem_shouldThrow_whenInsufficientStock() {
        assertThrows(IllegalStateException.class, () -> cartService.addItem(product, 6));
    }

    @Test
    void addItem_shouldAddNewItem() {
        cartService.addItem(product, 1);
        assertEquals(1, cartService.getItems().size());
    }

    @Test
    void addItem_shouldUpdateExistingItem() {
        cartService.addItem(product, 1);
        cartService.addItem(product, 2);
        assertEquals(1, cartService.getItems().size());
        assertEquals(3, cartService.getItems().get(0).getQuantity());
    }

    @Test
    void getItems_shouldReturnCopy() {
        cartService.addItem(product, 1);
        cartService.getItems().clear();
        assertFalse(cartService.isEmpty());
    }

    @Test
    void isEmpty_shouldReturnTrueForNewCart() {
        assertTrue(cartService.isEmpty());
    }

    @Test
    void addItem_shouldThrow_whenProductNull() {
        assertThrows(NullPointerException.class,
                () -> cartService.addItem(null, 1));
    }
}