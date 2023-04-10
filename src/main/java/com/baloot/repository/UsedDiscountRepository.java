package com.baloot.repository;

import com.baloot.database.Column;
import com.baloot.database.Database;
import com.baloot.database.ForeignKeyColumn;
import com.baloot.database.Table;
import com.baloot.model.Discount;
import com.baloot.model.Provider;
import com.baloot.model.UsedDiscount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsedDiscountRepository extends Repository<UsedDiscount> {
	public UsedDiscountRepository(Database db) {
		super(db);
	}

	public UsedDiscount findNotExpiredByUsername(String username) {
		return this.findInstance(Map.of(
				"username", username,
				"expired", false
		));
	}

	public void expireAllDiscounts(String username) {
		this.update(
				Map.of(
				"username", username,
				"expired", false
				),
				Map.of(
						"expired", true
				)
		);
	}

	@Override
	public Table getTable() {
		return this.db.getTable("used-discounts");
	}

	@Override
	public UsedDiscount convertRawDataToModel(Map<String, Object> rawData) {
		return new UsedDiscount(
				(String) rawData.get("discountCode"),
				(String) rawData.get("username"),
				(Boolean) rawData.get("expired")
		);
	}

	@Override
	public Map<String, Object> convertModelToRawData(UsedDiscount usedDiscount) {
		return Map.of(
				"username", usedDiscount.getUsername(),
				"discountCode", usedDiscount.getDiscountCode(),
				"expired", usedDiscount.isExpired()
		);
	}

	@Override
	public void insert(UsedDiscount entity) {
		if (!entity.isExpired())
			this.expireAllDiscounts(entity.getUsername());
		super.insert(entity);
	}

	@Override
	public void migrate() {
		Table table = this.getTable();
		table.addColumn(new Column(
				table,
				"expired",
				false
		));
		table.addColumn(new ForeignKeyColumn(
				table,
				"username",
				false,
				this.db.getTable("users"),
				this.db.getTable("users").getColumn("username")
		));
		table.addColumn(new ForeignKeyColumn(
				table,
				"discountCode",
				false,
				this.db.getTable("discounts"),
				this.db.getTable("discounts").getColumn("code")
		));
	}
}
