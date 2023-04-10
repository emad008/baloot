package com.baloot.exception;

public class Unauthorized extends BalootException {
	@Override
	public String getMessage() {
		return "Unauthorized";
	}
}
