package com.hybris.students.orders.domain;

import com.hybris.students.orders.io.InventoryEntry;

public class InventoryEntryImpl implements InventoryEntry {

    private String location;
    private String sku;
    private Integer amount;

    public InventoryEntryImpl(String location, String sku, int amount) {
        this.location = location;
        this.sku = sku;
        this.amount = amount;
    }

    public InventoryEntryImpl(InventoryEntry inventoryEntry) {
        this.location = inventoryEntry.getLocation();
        this.amount = inventoryEntry.getAmount();
        this.sku = inventoryEntry.getSku();
    }

    public String getLocation() {
        return location;
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

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
