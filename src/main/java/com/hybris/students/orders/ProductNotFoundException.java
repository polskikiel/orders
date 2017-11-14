package com.hybris.students.orders;

public class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -626077657378646574L;

    public ProductNotFoundException(String message) {
        super(message);
    }
}
