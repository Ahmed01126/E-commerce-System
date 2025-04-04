package com.fawry.ecommerce;

import com.fawry.ecommerce.model.Customer;
import com.fawry.ecommerce.model.Product;
import com.fawry.ecommerce.service.CartService;
import com.fawry.ecommerce.service.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {
    private CartService cartService;
    private CheckoutService checkoutService;
    private Product cheese;
    private Product biscuits;
    private Customer customer;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        cartService = new CartService();
        checkoutService = new CheckoutService();

        cheese = new Product("Cheese", 100, 10)
                .setShippable(200)  // 200g
                .setExpirable(LocalDate.now().plusDays(10));

        biscuits = new Product("Biscuits", 150, 5)
                .setShippable(700)  // 700g
                .setExpirable(LocalDate.now().plusDays(20));

        // Increased balance to cover the total amount (350 subtotal + 33 shipping = 383)
        customer = new Customer("John Doe", 500);

        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testAddItemsToCartService() {
        cartService.addItem(cheese, 2);
        cartService.addItem(biscuits, 1);

        assertEquals(2, cartService.getItems().get(0).getQuantity());
        assertEquals(1, cartService.getItems().get(1).getQuantity());
    }

    @Test
    public void testCartServiceTotalWeight() {
        cartService.addItem(cheese, 2); // 400g
        cartService.addItem(biscuits, 1); // 700g

        double totalWeight = cartService.getItems().stream()
                .filter(item -> item.getProduct().isShippable())
                .mapToDouble(item -> item.getProduct().getWeight() * item.getQuantity())
                .sum();

        assertEquals(1100, totalWeight);
    }
    @Test
    public void testCheckoutProcess() {
        // Create customer with sufficient balance
        Customer testCustomer = new Customer("Test Customer", 1000.0);

        // Create products with proper weights in grams
        Product cheese = new Product("Cheese", 100.0, 10)
                .setShippable(200); // 200g
        Product biscuits = new Product("Biscuits", 150.0, 5)
                .setShippable(700); // 700g

        cartService.addItem(cheese, 2); // 200
        cartService.addItem(biscuits, 1); // 150

        // Perform checkout and capture output
        checkoutService.checkout(testCustomer, cartService);
        String output = outputStream.toString();

        // Debug: Print actual output
        System.out.println("ACTUAL OUTPUT:\n" + output);

        // Verify the amount with flexible formatting
        assertTrue(output.contains("Amount    383") ||
                output.contains("Amount    383.0") ||
                output.contains("Amount    383.00"));

        // Verify remaining balance
        assertEquals(617.0, testCustomer.getBalance(), 0.001); // 1000 - 383
    }

    @Test
    public void testCartServiceSubtotal() {
        cartService.addItem(cheese, 2); // 200
        cartService.addItem(biscuits, 1); // 150

        double subtotal = cartService.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        assertEquals(350, subtotal);
    }



    @Test
    public void testEmptyCartException() {
        assertThrows(IllegalStateException.class, () -> {
            checkoutService.checkout(customer, cartService);
        });
    }

    @Test
    public void testInsufficientBalance() {
        Customer poorCustomer = new Customer("Poor", 100);
        cartService.addItem(cheese, 2);
        cartService.addItem(biscuits, 1);

        assertThrows(IllegalStateException.class, () -> {
            checkoutService.checkout(poorCustomer, cartService);
        });
    }

    @Test
    public void testExpiredProduct() {
        Product expiredMilk = new Product("Milk", 50, 3)
                .setExpirable(LocalDate.now().minusDays(1));

        assertThrows(IllegalStateException.class, () -> {
            cartService.addItem(expiredMilk, 1);
        });
    }
}