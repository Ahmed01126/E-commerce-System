package com.fawry.ecommerce;

import com.fawry.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void constructor_shouldThrow_whenNameIsNull() {
        assertThrows(NullPointerException.class, () -> new Product(null, 10, 5));
    }

    @Test
    void constructor_shouldThrow_whenQuantityIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Product("Test", 10, -1));
    }

    @Test
    void isExpired_shouldReturnFalse_forNonExpirableProduct() {
        Product product = new Product("Test", 10, 5);
        assertFalse(product.isExpired());
    }

    @Test
    void isExpired_shouldReturnTrue_whenExpired() {
        Product product = new Product("Test", 10, 5)
                .setExpirable(LocalDate.now().minusDays(1));
        assertTrue(product.isExpired());
    }

    @Test
    void reduceQuantity_shouldThrow_whenAmountInvalid() {
        Product product = new Product("Test", 10, 5);
        assertThrows(IllegalArgumentException.class, () -> product.reduceQuantity(0));
        assertThrows(IllegalArgumentException.class, () -> product.reduceQuantity(-1));
    }

    @Test
    void reduceQuantity_shouldThrow_whenInsufficientStock() {
        Product product = new Product("Test", 10, 1);
        assertThrows(IllegalStateException.class, () -> product.reduceQuantity(2));
    }

    @Test
    void reduceQuantity_shouldUpdateQuantity() {
        Product product = new Product("Test", 10, 5);
        product.reduceQuantity(3);
        assertEquals(2, product.getQuantity());
    }
}