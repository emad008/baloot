package baloot.controller;

import baloot.model.BuyListItem;
import baloot.model.Commodity;
import baloot.model.User;
import baloot.repository.BuyListRepository;
import baloot.repository.CommodityRepository;
import baloot.repository.UserRepository;
import baloot.service.BuyListService;
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
        List<BuyListItem> buyList = this.buyListRepository.listByUsername(userData.getString("username"));

        List<Map<String, Object>> commoditiesDescriptions = new ArrayList<>();

        for (BuyListItem buyListItem: buyList)
            commoditiesDescriptions.add(
                this.commodityRepository.findById(
                    buyListItem.getCommodityId()
                ).describe()
            );

        JSONObject response = new JSONObject();
        response.put(
            "buyList",
            commoditiesDescriptions
        );

        return response;
    }

    public JSONObject purchaseAll(JSONObject userData) {
        User user = this.userRepository.findByUsername(userData.getString("username"));

        this.buyListService.purchaseBuyList(user);

        return new JSONObject();
    }
}
