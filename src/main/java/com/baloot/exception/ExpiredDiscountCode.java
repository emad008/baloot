package com.baloot.exception;

public class ExpiredDiscountCode extends BalootException {
	private String discountCode;

	public ExpiredDiscountCode(
			String code
	) {
		this.discountCode = code;
	}

	@Override
	public String getMessage() {
		return "Discount code " + this.discountCode + " has been expired for you";
	}
}
