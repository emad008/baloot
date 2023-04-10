package com.baloot.controller;

import com.baloot.model.Comment;
import com.baloot.model.Commodity;
import com.baloot.model.User;
import com.baloot.repository.CommentRepository;
import com.baloot.repository.CommodityRepository;
import com.baloot.repository.ProviderRepository;
import com.baloot.repository.UserRepository;
import com.squareup.okhttp.internal.Internal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommodityController extends BalootController {
    private final CommodityRepository commodityRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ProviderRepository providerRepository;

    public CommodityController(
        CommodityRepository commodityRepository,
        CommentRepository commentRepository,
        UserRepository userRepository,
        ProviderRepository providerRepository
    ) {
        this.commodityRepository = commodityRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.providerRepository = providerRepository;
    }

    public JSONObject addCommodity(JSONObject commodityData) {
        var x = commodityData.get("categories");

        ArrayList<String> categories = this.convertJSONArrayToArrayList(commodityData.getJSONArray("categories"));

        this.commodityRepository.insert(
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

    public JSONObject getCommoditiesList(JSONObject filterAndSortData) {
        List<Commodity> commodities = this.commodityRepository.list(
            this.parseFilterAndSortData(filterAndSortData).getFirst(),
            this.parseFilterAndSortData(filterAndSortData).getSecond()
        );

        List<Map<String, Object>> commoditiesDescription = new ArrayList<>();

        for (Commodity commodity: commodities)
            commoditiesDescription.add(commodity.describe());

        for (Map<String, Object> commodityDescription: commoditiesDescription) {
            commodityDescription.put(
                "provider",
                this.providerRepository.findById(
                        (Integer) commodityDescription.get("providerId")
                ).describe()
            );
        }

        return new JSONObject(Map.of(
                "commoditiesList",
                new JSONArray(commoditiesDescription)
        ));
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
        Commodity commodity = this.commodityRepository.findById(
                commodityData.getInt("id")
        );
        JSONObject res = new JSONObject(
            commodity.describe()
        );

        res.put("provider", this.providerRepository.findById(
                res.getInt("providerId")
        ).describe());

        List<Map<String, Object>> comments = new ArrayList();

        for (Comment comment: this.commentRepository.listByCommodityId(
            commodityData.getInt("id")
        )) {
            Map<String, Object> description = new HashMap<>(comment.describe());
            User user = this.userRepository.findByUsername(comment.getUsername());
            description.put("user", user.describe());
            comments.add(description);
        }

        List<Commodity> suggestedCommodities = this.commodityRepository.list();
        suggestedCommodities.sort((commodityA, commodityB) -> {
            float a = (float) 11 * (commodityA.getCategories().stream().filter(
                    f -> commodity.getCategories().contains(f)
            ).toList().size() >= 1 ? 1 : 0) + commodityA.getRating();
            float b = (float) 11 * (commodityB.getCategories().stream().filter(
                    f -> commodity.getCategories().contains(f)
            ).toList().size() >= 1 ? 1 : 0) + commodityB.getRating();

            return Float.compare(b, a);
        });

        res.put("suggestedCommodities", new JSONArray());
        int i = 0;
        while (res.getJSONArray("suggestedCommodities").length() < Integer.min(5, suggestedCommodities.size() - 1)) {
            if (suggestedCommodities.get(i).getId() != commodity.getId()) {
                res.getJSONArray("suggestedCommodities").put(suggestedCommodities.get(i).describe());
            }
            i++;
        }

        res.put("comments", comments);

        return res;
    }
}
