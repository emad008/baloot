package baloot.controller;

import baloot.model.Commodity;
import baloot.model.User;
import baloot.repository.CommodityRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommodityController {
    private final CommodityRepository commodityRepository;

    public CommodityController(CommodityRepository commodityRepository) {
        this.commodityRepository = commodityRepository;
    }

    public JSONObject addCommodity(JSONObject commodityData) {
        var x = commodityData.get("categories");

        ArrayList<String> categories = this.convertJSONArrayToArrayList(commodityData.getJSONArray("categories"));

        this.commodityRepository.save(
            new Commodity(
                commodityData.getInt("id"),
                commodityData.getString("name"),
                commodityData.getInt("providerId"),
                commodityData.getInt("price"),
                categories,
                commodityData.getInt("inStock")
            )
        );

        return new JSONObject(this.commodityRepository.findById(commodityData.getInt("id")).describe());
    }

    private ArrayList<String> convertJSONArrayToArrayList(JSONArray source) {
        if (source == null)
            return null;

        ArrayList<String> target = new ArrayList<>();

        for (int i = 0; i < source.length(); i++){
            target.add(source.getString(i));
        }

        return target;
    }

    public JSONObject getCommoditiesList() {
        JSONObject commoditiesList = new JSONObject();
        List<JSONObject> commoditiesAsJson = new ArrayList<>();

        for (Commodity commodity: this.commodityRepository.list())
            commoditiesAsJson.add(new JSONObject(commodity.describe()));

        commoditiesList.put(
            "commoditiesList",
            commoditiesAsJson
        );
        return commoditiesList;
    }

    public JSONObject rateCommodity(JSONObject rateCommodityData) {
        Commodity commodity = this.commodityRepository.findById(rateCommodityData.getInt("commodityId"));
        commodity.updateScore(
            rateCommodityData.getString("username"),
            rateCommodityData.getInt("score")
        );
        this.commodityRepository.save(commodity);
        return new JSONObject(this.commodityRepository.findById(rateCommodityData.getInt("commodityId")).describe());
    }

    public JSONObject getCommodityById(JSONObject commodityData) {
        return new JSONObject(
            this.commodityRepository.findById(
                commodityData.getInt("id")
            ).describe()
        );
    }

    public JSONObject listCommoditiesByCategory(JSONObject categoryData) {
        JSONObject result = new JSONObject();
        List<Map<String, Object>> descriptions = new ArrayList<>();
        List<Commodity> commoditiesList = this.commodityRepository.listByCategory(
            categoryData.getString("category")
        );

        for (Commodity commodity: commoditiesList)
            descriptions.add(commodity.describe());

        result.put(
            "commoditiesListByCategory",
            descriptions
        );

        return result;
    }
}
