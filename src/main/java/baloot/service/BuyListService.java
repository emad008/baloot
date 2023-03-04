package baloot.service;

import baloot.exception.AlreadyInBuyList;
import baloot.exception.EntityNotFound;
import baloot.exception.NoCommodityAvailable;
import baloot.model.BuyListItem;
import baloot.model.Commodity;
import baloot.model.User;
import baloot.repository.BuyListRepository;
import baloot.repository.CommodityRepository;

public class BuyListService {
    private final BuyListRepository buyListRepository;

    private final CommodityRepository commodityRepository;

    public BuyListService(
        BuyListRepository buyListRepository,
        CommodityRepository commodityRepository
    ) {
        this.buyListRepository = buyListRepository;
        this.commodityRepository = commodityRepository;
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
}
