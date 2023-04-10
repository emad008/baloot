package com.baloot.controller;

import com.baloot.exception.EntityNotFound;
import com.baloot.model.BuyListItem;
import com.baloot.model.Commodity;
import com.baloot.model.User;
import com.baloot.repository.*;
import com.baloot.service.BuyListService;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyListController {
    private final UserRepository userRepository;
    private final CommodityRepository commodityRepository;
    private final BuyListRepository buyListRepository;
    private final BuyListService buyListService;

    public BuyListController(
        UserRepository userRepository,
        CommodityRepository commodityRepository,
        BuyListRepository buyListRepository,
        BuyListService buyListService
    ) {
        this.userRepository = userRepository;
        this.commodityRepository = commodityRepository;
        this.buyListRepository = buyListRepository;
        this.buyListService = buyListService;
    }
    public JSONObject addToBuyList(JSONObject buyListItemData) {
        User user = this.userRepository.findByUsername(
            buyListItemData.getString("username")
        );

        Commodity commodity = this.commodityRepository.findById(
            buyListItemData.getInt("commodityId")
        );

        this.buyListService.addToBuyList(
            user,
            commodity
        );

        return new JSONObject();
    }

    public JSONObject removeFromBuyList(JSONObject buyListItemData) {
        User user = this.userRepository.findByUsername(
            buyListItemData.getString("username")
        );

        Commodity commodity = this.commodityRepository.findById(
            buyListItemData.getInt("commodityId")
        );

        this.buyListService.removeFromBuyList(
            user,
            commodity
        );

        return new JSONObject();
    }

    public JSONObject getBuyList(JSONObject userData) {
        User user = this.userRepository.findByUsername(userData.getString("username"));
        List<BuyListItem> buyList = this.buyListRepository.listByUsername(user.getUsername());

        List<Map<String, Object>> commoditiesDescriptions = new ArrayList<>();

        for (BuyListItem buyListItem: buyList)
            commoditiesDescriptions.add(
                this.commodityRepository.findById(
                    buyListItem.getCommodityId()
                ).describe()
            );

        JSONObject response = new JSONObject();

        response.put(
            "items",
            commoditiesDescriptions
        );

        response.put(
            "creditSum",
            this.buyListService.getBuyListTotalPrice(user)
        );
        response.put(
            "discount",
            this.buyListService.getUserDiscount(user)
        );
        response.put(
            "final",
            this.buyListService.getBuyListFinalPrice(user)
        );

        return response;
    }

    public JSONObject purchaseAll(JSONObject userData) {
        User user = this.userRepository.findByUsername(userData.getString("username"));

        this.buyListService.purchaseBuyList(user);

        return new JSONObject();
    }
}
