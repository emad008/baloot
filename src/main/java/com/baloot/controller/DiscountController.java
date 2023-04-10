package com.baloot.controller;

import com.baloot.exception.ExpiredDiscountCode;
import com.baloot.exception.KeyConstraint;
import com.baloot.model.Commodity;
import com.baloot.model.Provider;
import com.baloot.model.UsedDiscount;
import com.baloot.repository.CommodityRepository;
import com.baloot.repository.DiscountRepository;
import com.baloot.repository.ProviderRepository;
import com.baloot.repository.UsedDiscountRepository;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountController {
	private final UsedDiscountRepository usedDiscountRepository;

	public DiscountController(
			UsedDiscountRepository usedDiscountRepository
	) {
		this.usedDiscountRepository = usedDiscountRepository ;
	}

	public JSONObject useDiscount(JSONObject discountUseData) {
		UsedDiscount usedDiscount = new UsedDiscount(
				discountUseData.getString("code"),
				discountUseData.getString("username")
		);
		try {
			this.usedDiscountRepository.insert(
					usedDiscount
			);
		}
		catch (KeyConstraint ex) {
			throw new ExpiredDiscountCode(
				discountUseData.getString("code")
			);
		}
		return new JSONObject(usedDiscount.describe());
	}
}
