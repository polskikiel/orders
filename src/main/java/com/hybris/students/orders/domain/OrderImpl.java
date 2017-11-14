package com.hybris.students.orders.domain;

import com.hybris.students.orders.Order;

public class OrderImpl implements Order {

    private String destination;
    private String sku;
    private int amount;


    public OrderImpl(Order order) {
        this.destination = order.getDestination();
        this.sku = order.getSku();
        this.amount = order.getAmount();

    }

    public String getDestination() {
        return destination;
    }

    public String getSku() {
        return sku;
    }

    public int getAmount() {
        return amount;
    }
}
