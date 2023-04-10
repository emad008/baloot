package com.baloot.repository;

import com.baloot.database.Column;
import com.baloot.database.Database;
import com.baloot.database.Table;
import com.baloot.model.Discount;
import java.util.Map;

public class DiscountRepository extends Repository<Discount> {
	public DiscountRepository(
		Database db
	) {
		super(db);
	}

	public Discount findByCode(String code) {
		return this.findInstance(Map.of(
				"code", code
		));
	}

	@Override
	public Table getTable() {
		return this.db.getTable("discounts");
	}

	@Override
	public Map<String, Object> convertModelToRawData(Discount discount) {
		return Map.of(
				"code", discount.getCode(),
				"percent", discount.getPercent()
		);
	}

	@Override
	public Discount convertRawDataToModel(Map<String, Object> rawDataList) {
		return new Discount(
				(String) rawDataList.get("code"),
				(Integer) rawDataList.get("percent")
		);
	}

	@Override
	public void migrate() {
		Table table = this.getTable();
		table.addColumn(new Column(
				table,
				"code",
				true
		));
		table.addColumn(new Column(
				table,
				"percent",
				false
		));
	}
}
