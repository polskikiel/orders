package com.hybris.students.orders;

import java.util.Collection;


public interface OrderFulfillment
{
	/**
	 * Finds the way with a minimum number of shipments to process all orders for given inventory entries.
	 * Result should have also a minimum number of location->destination pairs in the result.
	 * 
	 * @param orders collection of orders to fulfill
	 * @param inventoryEntries collection of available inventory entries
	 * @return collection of shipments
	 * @throws CannotFulfillException is thrown if orders cannot be fulfilled
	 * @throws ProductNotFoundException is thrown if product don't exist in any inventory
	 */
	Collection<Shipment> fulfillOrders(Collection<Order> orders, Collection<InventoryEntry> inventoryEntries)
			throws CannotFulfillException, ProductNotFoundException;
}
