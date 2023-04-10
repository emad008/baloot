package com.baloot.exception;

public class InvalidConditionType extends BalootException {
	private final String conditionSymbol;

	public InvalidConditionType(String conditionSymbol) {
		this.conditionSymbol = conditionSymbol;
	}

	@Override
	public String getMessage() {
		return "Invalid condition type: " + this.conditionSymbol;
	}
}
