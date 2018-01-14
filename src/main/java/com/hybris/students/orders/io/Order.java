package com.hybris.students.orders.io;

public interface Order
{
	/**
	 * @return destination of the order (identifier of delivery address)
	 */
	String getDestination();

	/**
	 * @return Stock-keepeng unit of ordered product
	 */
	String getSku();

	/**
	 * @return amount of ordered products
	 */
	int getAmount();
}
