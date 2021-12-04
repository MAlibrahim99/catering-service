package catering.order.payment;

import org.salespointframework.payment.PaymentMethod;

public class DebitCharge extends PaymentMethod {

	public DebitCharge(String description) {
		super(description);
	}
}
