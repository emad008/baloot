package com.baloot.model;

import java.util.Map;

public class UsedDiscount extends Model {
	private String discountCode, username;
	private Boolean expired;

	public UsedDiscount(String discountCode, String username, Boolean expired) {
		this.discountCode = discountCode;
		this.username = username;
		this.expired = expired;
	}

	public UsedDiscount(String discountCode, String username) {
		this(discountCode, username, false);
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public String getUsername() {
		return username;
	}

	public Boolean isExpired() {
		return expired;
	}

	@Override
	public Map<String, Object> getKey() {
		return Map.of(
				"discountCode",
				this.getDiscountCode(),
				"username",
				this.getUsername()
		);
	}

	@Override
	public Map<String, Object> describe() {
		return Map.of(
				"discountCode", this.getDiscountCode(),
				"username", this.getUsername(),
				"expired", this.isExpired()
		);
	}
}
