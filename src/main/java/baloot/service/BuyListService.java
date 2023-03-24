package baloot.service;

import baloot.exception.AlreadyInBuyList;
import baloot.exception.EntityNotFound;
import baloot.exception.NoCommodityAvailable;
import baloot.exception.InsufficientFunds;
import baloot.model.BuyListItem;
import baloot.model.Commodity;
import baloot.model.User;
import baloot.repository.BuyListRepository;
import baloot.repository.CommodityRepository;
import baloot.repository.UserRepository;

import java.util.List;

public class BuyListService {
    private final BuyListRepository buyListRepository;
    private final CommodityRepository commodityRepository;
    private final UserRepository userRepository;

    public BuyListService(
        BuyListRepository buyListRepository,
        CommodityRepository commodityRepository,
        UserRepository userRepository
    ) {
        this.buyListRepository = buyListRepository;
        this.commodityRepository = commodityRepository;
        this.userRepository = userRepository;
    }

    public void addToBuyList(User user, Commodity commodity) {
        if (!commodity.isAvailable())
            throw new NoCommodityAvailable(commodity.getName());

        BuyListItem buyListItem = new BuyListItem(
            user.getUsername(),
            commodity.getId()
        );

        try {
            this.buyListRepository.findInstance(buyListItem.getKey());
            throw new AlreadyInBuyList(user.getUsername(), commodity.getName());
        }
        catch (EntityNotFound ex) {
            commodity.decreaseStock(1);
            this.commodityRepository.save(commodity);

            this.buyListRepository.save(buyListItem);
        }
    }

    public void removeFromBuyList(User user, Commodity commodity) {
        commodity.increaseStock(1);
        this.commodityRepository.save(commodity);

        this.buyListRepository.delete(new BuyListItem(
            user.getUsername(),
            commodity.getId()
        ));
    }

    public void purchaseBuyList(User user) {
        List<BuyListItem> buyListItems = this.buyListRepository.listByUsername(user.getUsername());

        int pricesSum = 0;

        for (BuyListItem buyListItem: buyListItems) {
            pricesSum += this.commodityRepository.findById(
                buyListItem.getCommodityId()
            ).getPrice();
        }

        if (user.getCredit() < pricesSum)
            throw new InsufficientFunds(
                user.getUsername(),
                pricesSum,
                user.getCredit()
            );

        user.decreaseCredit(
            pricesSum
        );
        this.userRepository.save(user);
        this.buyListRepository.purchaseAllUserBuyList(
            user.getUsername()
        );
    }
}
