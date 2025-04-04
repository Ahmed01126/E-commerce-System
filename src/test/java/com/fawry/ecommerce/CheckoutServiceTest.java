package com.fawry.ecommerce;

import com.fawry.ecommerce.model.*;
import com.fawry.ecommerce.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {
    private CheckoutService checkoutService;
    private CartService cart;
    private Customer customer;
    private Product shippableProduct;
    private Product nonShippableProduct;

    @BeforeEach
    void setUp() {
        checkoutService = new CheckoutService();
        cart = new CartService();
        customer = new Customer("Test", 1000);

        shippableProduct = new Product("Shippable", 100, 10)
                .setShippable(1.0);

        nonShippableProduct = new Product("Digital", 50, 100);
    }

    @Test
    public void checkout_shouldProcessValidOrder() {
        // Setup with sufficient balance
        CartService cart = new CartService();
        Customer customer = new Customer("John", 2000.0); // Increased balance

        // Products with weights in grams
        Product tv = new Product("TV", 500.0, 2)
                .setShippable(15000); // 15kg
        Product card = new Product("Gift Card", 50.0, 100)
                .setShippable(0); // Digital

        cart.addItem(tv, 1); // 500
        cart.addItem(card, 2); // 100

        // Expected calculations:
        // Subtotal: 500 + 100 = 600
        // Shipping: 15kg * 30 = 450
        // Total: 600 + 450 = 1050

        // Perform checkout
        checkoutService.checkout(customer, cart);

        // Verify final balance
        assertEquals(950.0, customer.getBalance(), 0.001); // 2000 - 1050
    }// But since we can't have negative balance, the service should prevent this


    @Test
    void checkout_shouldThrow_whenCartEmpty() {
        assertThrows(IllegalStateException.class,
                () -> checkoutService.checkout(customer, cart));
    }

    @Test
    void checkout_shouldThrow_whenInsufficientBalance() {
        cart.addItem(shippableProduct, 10);
        Customer poorCustomer = new Customer("Poor", 10);
        assertThrows(IllegalStateException.class,
                () -> checkoutService.checkout(poorCustomer, cart));
    }
}