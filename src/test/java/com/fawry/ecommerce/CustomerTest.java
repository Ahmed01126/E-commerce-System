package com.fawry.ecommerce;

import com.fawry.ecommerce.model.Customer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void constructor_shouldThrow_whenNameIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Customer(null, 100));
        assertThrows(IllegalArgumentException.class, () -> new Customer("", 100));
        assertThrows(IllegalArgumentException.class, () -> new Customer("   ", 100));
    }
    @Test
    void deductBalance_shouldThrow_whenInsufficientBalance() {
        Customer customer = new Customer("Test", 50);
        assertThrows(IllegalStateException.class, () -> customer.deductBalance(100));
        // Verify balance didn't change
        assertEquals(50, customer.getBalance(), 0.001);

    }

    @Test
    void deductBalance_shouldThrow_whenAmountInvalid() {
        Customer customer = new Customer("Test", 100);
        assertThrows(IllegalArgumentException.class, () -> customer.deductBalance(0));
        assertThrows(IllegalArgumentException.class, () -> customer.deductBalance(-10));
    }


    @Test
    void deductBalance_shouldUpdateBalanceCorrectly() {
        Customer customer = new Customer("Test", 100);
        customer.deductBalance(30);
        assertEquals(70, customer.getBalance());
    }
}