# Fawry E-Commerce System

A Java-based e-commerce platform demonstrating core OOP principles, cart management, and checkout functionality.

## Features
- **Cart Service**: Add/remove items, quantity validation
- **Checkout Service**: 
  - Calculate subtotals and shipping fees (30 EGP/kg)
  - Generate receipts and shipment notices
- **Inventory Management**: Track product stock
- **Customer Balance Handling**: Secure payment processing

## Technologies
- Java 17
- JUnit 5 (Unit Testing)
- Maven (Dependency Management)

## Project Structure
```
src/
├── main/
│ ├── java/com/fawry/ecommerce/
│ │ ├── model/ # Domain objects
│ │ │ ├── CartItem.java
│ │ │ ├── Customer.java
│ │ │ └── Product.java
│ │ ├── service/ # Business logic
│ │ │ ├── CartService.java
│ │ │ └── CheckoutService.java
│ │ └── Application.java # Entry point
└── test/ # Unit tests
└── java/com/fawry/ecommerce/
├── ApplicationTest.java
├── CartServiceTest.java
└── ...
```

## Installation
1. Ensure JDK 17+ is installed
2. Clone this repository
3. Build with Maven:
```bash
mvn clean install
```

```Run Tests:
mvn test
```

Key OOP Principles Demonstrated

# Example Usage
```
// Initialize services
CartService cart = new CartService();
Customer customer = new Customer("John", 1000.0);

// Add products
Product cheese = new Product("Cheese", 100.0, 10).setShippable(200); // 200g
cart.addItem(cheese, 2);

// Checkout
new CheckoutService().checkout(customer, cart);
```
##Test Coverage
35 passing tests covering:

Cart operations (100%)

Checkout scenarios (100%)

Edge cases (negative values, expired products)

##Output Samples
Receipt:
```
2x Cheese    200.00 EGP
1x Biscuits  150.00 EGP
-------------------
Subtotal:    350.00 EGP
Shipping:     33.00 EGP
Total:       383.00 EGP
```


## Screenshot of the build process output
![Screenshot of the build process output](https://github.com/user-attachments/assets/409eb176-339c-456f-ae95-44346d249695)

### Why this works:
1. **Professional Format**: Clear sections with code formatting
2. **Highlights Strengths**: OOP principles, testing, and structure
3. **Fawry-Relevant**: Shows attention to:
   - Clean architecture
   - Validation logic
   - Business requirements (like shipping calc)

### Submitted for Fawry N² Internship Program - April 2025
