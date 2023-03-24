package baloot.controller;

import baloot.model.Comment;
import baloot.model.Commodity;
import baloot.model.User;
import baloot.repository.BuyListRepository;
import baloot.repository.CommentRepository;
import baloot.repository.CommodityRepository;
import baloot.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommodityController {
    private final CommodityRepository commodityRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommodityController(
        CommodityRepository commodityRepository,
        CommentRepository commentRepository,
        UserRepository userRepository
    ) {
        this.commodityRepository = commodityRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
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
        User user = this.userRepository.findByUsername(rateCommodityData.getString("username"));
        commodity.updateScore(
            user.getUsername(),
            rateCommodityData.getInt("score")
        );
        this.commodityRepository.save(commodity);
        return new JSONObject(this.commodityRepository.findById(rateCommodityData.getInt("commodityId")).describe());
    }

    public JSONObject getCommodityById(JSONObject commodityData) {
        JSONObject res = new JSONObject(
            this.commodityRepository.findById(
                commodityData.getInt("id")
            ).describe()
        );

        List<Map<String, Object>> comments = new ArrayList();

        for (Comment comment: this.commentRepository.listByCommodityId(
            commodityData.getInt("id")
        )) {
            Map<String, Object> description = new HashMap<>(comment.describe());
            User user = this.userRepository.findByEmail(comment.getUserEmail());
            description.put("user", user.describe());
            comments.add(description);
        }

        res.put("comments", comments);

        return res;
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

    public JSONObject listCommoditiesByPriceRange(JSONObject priceData) {
        int startPrice = priceData.getInt("startPrice");
        int endPrice = priceData.getInt("endPrice");

        List<Commodity> commodities = this.commodityRepository.listByPriceRange(
            startPrice,
            endPrice
        );

        List<Map<String, Object>> commoditiesDescription = new ArrayList<>();

        for (Commodity commodity: commodities)
            commoditiesDescription.add(commodity.describe());

        JSONObject res = new JSONObject();
        res.put(
            "commodities",
            commoditiesDescription
        );

        return res;
    }
}
