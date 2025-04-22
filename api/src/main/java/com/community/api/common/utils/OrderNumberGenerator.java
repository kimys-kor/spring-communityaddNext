package com.community.api.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class OrderNumberGenerator {

    // Format for the date part of the order number
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    // Method to generate the order number
    public static String generateOrderNumber() {
        // Get the current date
        String datePart = dateFormat.format(new Date());

        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Get the first 4 characters of the UUID
        String uuidPart = uuid.toString().substring(0, 4);

        // Combine the date and UUID parts to form the final order number
        String orderNumber = datePart + uuidPart;

        return orderNumber;
    }

}