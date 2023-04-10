package com.baloot.exception;

public class IncreaseByNegativeCredit extends BalootException {
	private final Integer amount;

	public IncreaseByNegativeCredit(Integer amount) {
		this.amount = amount;
	}

	@Override
	public String getMessage() {
		return "Increase credit amount cant be negative. " + this.amount + " is given.";
	}
}
