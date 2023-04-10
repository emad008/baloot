package baloot.controller;

import baloot.model.BuyListItem;
import baloot.model.User;
import baloot.repository.BuyListRepository;
import baloot.repository.CommentRepository;
import baloot.repository.CommodityRepository;
import baloot.repository.UserRepository;
import org.json.JSONObject;

import java.util.*;

public class UserController {
    private final UserRepository userRepository;
    private final BuyListRepository buyListRepository;
    private final CommodityRepository commodityRepository;

    public UserController(
        UserRepository userRepository,
        BuyListRepository buyListRepository,
        CommodityRepository commodityRepository
    ) {
        this.userRepository = userRepository;
        this.buyListRepository = buyListRepository;
        this.commodityRepository = commodityRepository;
    }

    public JSONObject addUser(JSONObject userData) {
        this.userRepository.save(
            new User(
                userData.getString("username"),
                userData.getString("password"),
                userData.getString("email"),
                userData.getString("birthDate"),
                userData.getString("address"),
                userData.getInt("credit")
            )
        );

        return userData;
    }

    public JSONObject getUserById(JSONObject userData) {
        User user = this.userRepository.findByUsername(userData.getString("username"));

        List<Map<String, Object>> purchasedItemsDescription = new ArrayList<>();

        for (BuyListItem buyListItem: this.buyListRepository.listPurchasedByUsername(user.getUsername())) {
            Map<String, Object> buyListItemDescription = new HashMap<>(buyListItem.describe());
            buyListItemDescription.put(
                "commodity",
                this.commodityRepository.findById(buyListItem.getCommodityId()).describe()
            );
            purchasedItemsDescription.add(buyListItemDescription);
        }

        List<Map<String, Object>> buyListItemsDescription = new ArrayList<>();

        for (BuyListItem buyListItem: this.buyListRepository.listByUsername(user.getUsername())) {
            Map<String, Object> buyListItemDescription = new HashMap<>(buyListItem.describe());
            buyListItemDescription.put(
                "commodity",
                this.commodityRepository.findById(buyListItem.getCommodityId()).describe()
            );
            buyListItemsDescription.add(buyListItemDescription);
        }

        Map<String, Object> userDescription = new HashMap<>(user.describe());
        userDescription.put("buyListItems", buyListItemsDescription);
        userDescription.put("purchasedListItems", purchasedItemsDescription);

        return new JSONObject(userDescription);
    }

    public JSONObject addCredit(JSONObject addCreditData) {
        User user = this.userRepository.findByUsername(addCreditData.getString("username"));

        user.increaseCredit(addCreditData.getInt("amount"));

        this.userRepository.save(user);

        return new JSONObject();
    }
}
