package com.baloot.controller;

import kotlin.Pair;
import kotlin.ParameterName;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalootController {
	private Map<String, Object> parseFilterData(JSONObject data) {
		if (data.has("filters"))
			return data.getJSONObject("filters").toMap();
		return new HashMap<>();
	}

	private List<String> parseSortData(JSONObject data) {
		List<String> sortData = new ArrayList<>();
		if (data.has("sort"))
			for (Object attrName: data.getJSONArray("sort").toList())
				sortData.add((String) attrName);
		return sortData;
	}

	protected Pair<Map<String, Object>, List<String>> parseFilterAndSortData(JSONObject filterAndSortData) {
		return new Pair<>(
				this.parseFilterData(filterAndSortData),
				this.parseSortData(filterAndSortData)
		);
	}

}
