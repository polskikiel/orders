package com.hybris.students.orders.io;

public interface InventoryEntry
{
	/**
	 * @return inventory location (store)
	 */
	String getLocation();

	/**
	 * @return Stock-keepeng unit of product
	 */
	String getSku();

	/**
	 * @return amount of available products
	 */
	int getAmount();
}
