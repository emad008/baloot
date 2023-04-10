package com.baloot.exception;

public class ColumnTypeIsNotIterable extends BalootException {
	private final String columnType;
	private final String columnName;

	public ColumnTypeIsNotIterable(
		String columnType,
		String columnName
	) {
		this.columnType = columnType;
		this.columnName = columnName;
	}

	@Override
	public String getMessage() {
		return "Can not check contains in column " + this.columnName + " with type " + this.columnType;
	}
}
