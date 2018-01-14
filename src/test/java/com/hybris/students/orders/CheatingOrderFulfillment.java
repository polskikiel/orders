package com.hybris.students.orders;

import com.hybris.students.orders.exceptions.CannotFulfillException;
import com.hybris.students.orders.io.InventoryEntry;
import com.hybris.students.orders.io.Order;
import com.hybris.students.orders.io.OrderFulfillment;
import com.hybris.students.orders.io.Shipment;

import java.util.Collection;
import java.util.Collections;


/**
 * Sample implementation of OrderFulfillment that can pass OrderFulfillmentTest#simpleTestCase()
 * It actually doesn't work ;-)
 */
public class CheatingOrderFulfillment implements OrderFulfillment
{
	public Collection<Shipment> fulfillOrders(final Collection<Order> orders, final Collection<InventoryEntry> inventoryEntries)
			throws CannotFulfillException
	{
		if (inventoryEntries.isEmpty())
		{
			throw new CannotFulfillException();
		}

		final Shipment shipment = new Shipment()
		{

			public String getLocation()
			{
				return inventoryEntries.iterator().next().getLocation(); // To change body of implemented methods use File |
																							// Settings | File Templates.
			}

			public String getDestination()
			{
				return orders.iterator().next().getDestination();
			}

			public String getSku()
			{
				return orders.iterator().next().getSku();
			}

			public int getAmount()
			{
				return orders.iterator().next().getAmount();
			}
		};
		return Collections.singleton(shipment);
	}
}
