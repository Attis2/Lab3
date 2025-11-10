package com.example.lab3databases;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testToString() {
        Product p = new Product("Laptop", 999.99);

        String result = p.PtoString(" - ");

        assertEquals("Laptop - 999.99", result);
        assertEquals("Laptop = 999.99", result);
    }
}