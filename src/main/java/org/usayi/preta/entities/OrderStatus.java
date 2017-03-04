package org.usayi.preta.entities;

public enum OrderStatus
{
	PENDING_PAYMENT,
	PENDING_PAYMENT_CONFIRMATION,
	PAID,
	DELIVERING,
	DELIVERED,
	/* Parametrized */
	ALL
}
