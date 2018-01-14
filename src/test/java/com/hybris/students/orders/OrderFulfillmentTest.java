package com.hybris.students.orders;

import com.hybris.students.orders.exceptions.CannotFulfillException;
import com.hybris.students.orders.io.InventoryEntry;
import com.hybris.students.orders.io.Order;
import com.hybris.students.orders.io.OrderFulfillment;
import com.hybris.students.orders.io.Shipment;
import com.hybris.students.orders.services.OrderFulfillmentService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;


public class OrderFulfillmentTest {

    private OrderFulfillment fulfillment = new OrderFulfillmentService();

    @Test
    public void simpleTestCase() {
        // given

        Collection<InventoryEntry> entries = new ArrayList<InventoryEntry>();
        entries.add(createInventoryEntry("Gliwice", "p1", 10));
        entries.add(createInventoryEntry("Gliwice", "p2", 12));
        entries.add(createInventoryEntry("Gliwice", "p3", 5));
        entries.add(createInventoryEntry("Munich", "p1", 4));
        entries.add(createInventoryEntry("Munich", "p2", 4));
        entries.add(createInventoryEntry("Munich", "p3", 4));
        entries.add(createInventoryEntry("Montreal", "p1", 4));
        entries.add(createInventoryEntry("Montreal", "p4", 5));
        entries.add(createInventoryEntry("Montreal", "p124", 15));
        entries.add(createInventoryEntry("Munich", "p124", 4));

        Collection<Order> orders = new ArrayList<Order>();
        orders.add(createOrder("Sydney", "p1", 14));
        orders.add(createOrder("Sydney", "p2", 10));
        orders.add(createOrder("Sydney", "p3", 3));
        orders.add(createOrder("Tokyo", "p2", 6));

        orders.add(createOrder("Tokyo", "p4", 5));
        orders.add(createOrder("Tokyo", "p1", 4));
        orders.add(createOrder("Tokyo", "p124", 4));
        orders.add(createOrder("Berlin", "p124", 6));
        orders.add(createOrder("Corleone", "p124", 6));


        // when

        final Collection<Shipment> shipments = fulfillment.fulfillOrders(orders, entries);

        for (Shipment shipment : shipments) {
            System.out.println(shipment.getLocation() + " " + shipment.getDestination() + " " + shipment.getSku() + " " + shipment.getAmount());
        }


        Collection<Shipment> shipmentCollection = shipments;

        // Test if orders were fulfilled
        for (Order order : orders) {
            int ordered = 0;
            for (Shipment shipment : shipmentCollection) {
                if (shipment.getDestination().equals(order.getDestination())
                        && shipment.getSku().equals(order.getSku())) {

                    ordered += shipment.getAmount();
                }
            }
            assertThat(ordered).isEqualTo(order.getAmount());
        }
    }

    @Test(expected = CannotFulfillException.class)
    public void shouldThrowException() {
        // given
        final Order order = createOrder("Munich", "p1", 10);

        // when
        fulfillment.fulfillOrders(Collections.singleton(order),
                Collections.<InventoryEntry>emptyList());

    }

    private Order createOrder(final String destination, final String sku, final int amount) {
        return new Order() {
            public String getDestination() {
                return destination;
            }

            public String getSku() {
                return sku;
            }

            public int getAmount() {
                return amount;
            }
        };
    }

    private InventoryEntry createInventoryEntry(final String location, final String sku, final int amount) {
        return new InventoryEntry() {
            public String getLocation() {
                return location;
            }

            public String getSku() {
                return sku;
            }

            public int getAmount() {
                return amount;
            }
        };
    }
}
