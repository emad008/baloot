package com.baloot.model;

import java.util.Map;

public class Discount extends Model {
	private String code;
	private Integer percent;

	public Discount(String code, int percent) {
		this.code = code;
		this.percent = percent;
	}

	public Integer getPercent() {
		return percent;
	}

	public String getCode() {
		return code;
	}

	@Override
	public Map<String, Object> getKey() {
		return Map.of(
				"code",
				this.getCode()
		);
	}

	@Override
	public Map<String, Object> describe() {
		return Map.of(
				"code", this.getCode(),
				"percent", this.getPercent()
		);
	}
}
