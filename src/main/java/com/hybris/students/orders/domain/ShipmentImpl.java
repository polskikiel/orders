package com.hybris.students.orders.domain;

import com.hybris.students.orders.Shipment;

public class ShipmentImpl implements Shipment {

    private String location;
    private String destination;
    private String sku;
    private int amount;

    public ShipmentImpl(String location, String destination, String sku, int amount) {
        this.location = location;
        this.destination = destination;
        this.sku = sku;
        this.amount = amount;
    }

    public String getLocation() {
        return location;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ShipmentImpl{" +
                "location='" + location + '\'' +
                ", destination='" + destination + '\'' +
                ", sku='" + sku + '\'' +
                ", amount=" + amount +
                '}';
    }
}
