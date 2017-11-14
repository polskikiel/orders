package com.hybris.students.orders;

import com.hybris.students.orders.domain.InventoryEntryImpl;
import com.hybris.students.orders.domain.OrderImpl;
import com.hybris.students.orders.domain.ShipmentImpl;

import java.util.*;


public class OrderFulfillmentService implements OrderFulfillment {

    private Map<String, List<String>> connections =
            new HashMap<String, List<String>>();
    // key = [(order)destination]

    // java 7 way, I can do it also using java 8 collections
    public Collection<Shipment> fulfillOrders(final Collection<Order> orders,
                                              final Collection<InventoryEntry> inventoryEntries)
            throws CannotFulfillException, ProductNotFoundException {

        if (inventoryEntries.isEmpty())
            throw new CannotFulfillException();


        Collection<Shipment> shipments = new ArrayList<Shipment>();

        Map<String, List<InventoryEntryImpl>> inventoryMap =
                inventoryToMap(inventoryEntries);  // ALL MAGAZINES

        Map<String, List<OrderImpl>> orderMap =
                ordersToMap(orders);               // ALL ORDERS // both grouped by 'sku'
        // key = [(productId)sku]


        for (String product_Id : orderMap.keySet()) {       // @for every ordered product

            if (!inventoryMap.containsKey(product_Id)) {    // @if we don't have that product in our inventories
                throw new ProductNotFoundException(product_Id + " isn't available");
            }

            List<InventoryEntryImpl> inventoryEntryList =
                    inventoryMap.get(product_Id);           // inventories containing this product

            for (Order order : orderMap.get(product_Id)) {      // @for each order of this product

                int ordered = order.getAmount();

                int i = 0;
                int unconnectedOnStock = 0;

                for (InventoryEntryImpl inventoryEntry : inventoryEntryList) {
                    // @for each 'magazine' containing this product

                    if (hasConnection(inventoryEntry.getLocation(),
                            order.getDestination())) {  // cities previously connected each other are @Primary 4us

                        if (inventoryEntry.getAmount() >= ordered) {
                            // @if magazine is connected and have products to fulfill whole order

                            inventoryEntry.setAmount(inventoryEntry.getAmount() - order.getAmount());
                            // decrease amount of product in inventory by order amount
                            inventoryEntryList.set(i, inventoryEntry);
                            // and set quantity in inventory

                            shipments.add(new ShipmentImpl(         // making a new shipment
                                    inventoryEntry.getLocation(),
                                    order.getDestination(),
                                    product_Id,
                                    order.getAmount()));

                            break;      // order has been fulfilled ---> next order

                        } else {        // if product quantity in inventory was not enough
                            ordered -= inventoryEntry.getAmount();   // send everything!

                            shipments.add(new ShipmentImpl(
                                    inventoryEntry.getLocation(),
                                    order.getDestination(),
                                    product_Id,
                                    inventoryEntry.getAmount()));

                            inventoryEntry.setAmount(0);
                            inventoryEntryList.set(i, inventoryEntry);
                        }

                    } else {

                        if (inventoryEntry.getAmount() >= ordered &&    //
                                getConnections().get(order.getDestination()) == null) {
                            // if no one connected to this destination yet

                            addConnection(order.getDestination(), inventoryEntry.getLocation(),
                                    getConnections().get(order.getDestination()));

                            inventoryEntry.setAmount(inventoryEntry.getAmount() - order.getAmount());
                            inventoryEntryList.set(i, inventoryEntry);

                            shipments.add(
                                    new ShipmentImpl(
                                    inventoryEntry.getLocation(),
                                    order.getDestination(),
                                    product_Id,
                                    order.getAmount()));
                            break;

                        }

                        unconnectedOnStock += inventoryEntry.getAmount();
                    }
                    i++;
                    if (i == inventoryEntryList.size()) {
                        // when we couldn't fulfill orders only with connected sources
                        // I could make this in separate loop after INVENTORIES

                        if (unconnectedOnStock < ordered) {
                            throw new CannotFulfillException();
                        } else {
                            i = 0;
                            for (InventoryEntryImpl entry :
                                    sortInvetoriesByAmmoutOfProduct(inventoryEntryList)) {
                                if (!hasConnection(entry.getLocation(), order.getDestination())) {
                                    if (ordered - entry.getAmount() > 0) {
                                        ordered -= entry.getAmount();

                                        addConnection(order.getDestination(), entry.getLocation(),
                                                getConnections().get(order.getDestination()));

                                        shipments.add(new ShipmentImpl(
                                                entry.getLocation(),
                                                order.getDestination(),
                                                product_Id,
                                                entry.getAmount()));

                                        entry.setAmount(0);
                                        inventoryEntryList.set(i, entry);
                                    } else {
                                        addConnection(order.getDestination(), entry.getLocation(),
                                                getConnections().get(order.getDestination()));

                                        entry.setAmount(entry.getAmount() - ordered);
                                        inventoryEntryList.set(i, entry);
                                        shipments.add(new ShipmentImpl(
                                                entry.getLocation(),
                                                order.getDestination(),
                                                product_Id,
                                                ordered));
                                        break;
                                    }
                                }
                                i++;
                            }
                        }
                    }
                }   //** END OF INVENTORIES
            }   //** END OF ORDERS
        }   //** END OF PRODUCTS

        return shipments; // To change body of implemented methods use File | Settings | File Templates.
    }


    public List<InventoryEntryImpl> sortInvetoriesByAmmoutOfProduct(List<InventoryEntryImpl> inventoryEntries) {
        Collections.sort(inventoryEntries, new Comparator<InventoryEntryImpl>() {
            public int compare(InventoryEntryImpl o1, InventoryEntryImpl o2) {
                return o2.getAmount() - o1.getAmount(); // descending
            }
        });
        return inventoryEntries;
    }


    private InventoryEntryImpl inventoryEntryAfterOrder(InventoryEntryImpl inventoryEntry, int amount) {
        inventoryEntry.setAmount(inventoryEntry.getAmount() - amount);
        return inventoryEntry;
    }

    private boolean hasConnection(String location, String destination) {

        if (getConnections().containsKey(destination))
            for (String s : getConnections().get(destination))
                if (s.equals(location))
                    return true;


        return false;

    }

    private void addConnection(String destination, String location, List<String> connections) {
        Lists<String> strings = new Lists<String>();         // my own generic implementation to the Lists

        getConnections().put(destination,
                strings.addObjToList(location, connections));

    }

    private Map<String, List<InventoryEntryImpl>> inventoryToMap(Collection<InventoryEntry> inventoryEntries) {

        Map<String, List<InventoryEntryImpl>> inventoryMap = new HashMap<String, List<InventoryEntryImpl>>();

        Lists<InventoryEntryImpl> inventoryEntryLists = new Lists<InventoryEntryImpl>();


        for (InventoryEntry inventoryEntry : inventoryEntries) {

            inventoryMap.put(inventoryEntry.getSku(),
                    addInventoryToList(
                            new InventoryEntryImpl(inventoryEntry), inventoryMap.get(inventoryEntry.getSku())));


        }
        return inventoryMap;
    }

    private Map<String, List<OrderImpl>> ordersToMap(Collection<Order> orders) {

        Map<String, List<OrderImpl>> orderMap = new HashMap<String, List<OrderImpl>>();

        for (Order order : orders) {

            orderMap.put(order.getSku(), addOrderToList(new OrderImpl(order), orderMap.get(order.getSku())));
        }

        return orderMap;
    }

    private List<InventoryEntryImpl> addInventoryToList(InventoryEntryImpl entry, List<InventoryEntryImpl> entries) {
        if (entries == null) {
            entries = new ArrayList<InventoryEntryImpl>();
        }
        entries.add(entry);
        return entries;
    }

    private List<OrderImpl> addOrderToList(OrderImpl order, List<OrderImpl> orders) {
        if (orders == null) {
            orders = new ArrayList<OrderImpl>();
        }
        orders.add(order);
        return orders;
    }


    private List<InventoryEntryImpl> fromCollection(final Collection<InventoryEntry> inventoryEntries) {
        List<InventoryEntryImpl> entries = new ArrayList<InventoryEntryImpl>() {
            @Override
            public boolean add(InventoryEntryImpl inventoryEntry) {
                Collections.sort(this, new Comparator<InventoryEntryImpl>() {
                    public int compare(InventoryEntryImpl o1, InventoryEntryImpl o2) {
                        return o2.getAmount() - o1.getAmount(); // descending
                    }
                });
                return super.add(inventoryEntry);
            }
        };

        for (InventoryEntry inventoryEntry : inventoryEntries) {
            entries.add(new InventoryEntryImpl(inventoryEntry));
        }


        return entries;
    }


    public Map<String, List<String>> getConnections() {
        return connections;
    }
}
