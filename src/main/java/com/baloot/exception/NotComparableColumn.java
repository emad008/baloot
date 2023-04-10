package com.baloot.exception;

public class NotComparableColumn extends BalootException {
	private final String columnName;
	private final String columnType;

	public NotComparableColumn(String columnName, String columnType) {
		this.columnName = columnName;
		this.columnType = columnType;
	}

	@Override
	public String getMessage() {
		return "Two values of column " + columnName + " with type " + columnType + "are not comparable";
	}
}
