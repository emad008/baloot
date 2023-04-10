package com.baloot.service;

import com.baloot.exception.AlreadyInBuyList;
import com.baloot.exception.EntityNotFound;
import com.baloot.exception.InsufficientFunds;
import com.baloot.exception.NoCommodityAvailable;
import com.baloot.model.BuyListItem;
import com.baloot.model.Commodity;
import com.baloot.model.User;
import com.baloot.repository.*;

import java.util.List;
import java.util.Map;

public class BuyListService {
    private final BuyListRepository buyListRepository;
    private final CommodityRepository commodityRepository;
    private final UserRepository userRepository;
    private final UsedDiscountRepository usedDiscountRepository;
    private final DiscountRepository discountRepository;

    public BuyListService(
        BuyListRepository buyListRepository,
        CommodityRepository commodityRepository,
        UserRepository userRepository,
        UsedDiscountRepository usedDiscountRepository,
        DiscountRepository discountRepository
    ) {
        this.buyListRepository = buyListRepository;
        this.commodityRepository = commodityRepository;
        this.userRepository = userRepository;
        this.usedDiscountRepository = usedDiscountRepository;
        this.discountRepository = discountRepository;
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

    public Integer getBuyListTotalPrice(User user) {
        List<BuyListItem> buyListItems = this.buyListRepository.listByUsername(user.getUsername());

        return buyListItems.stream().map(
                (BuyListItem buyListItem) -> this.commodityRepository.findById(buyListItem.getCommodityId())
        ).mapToInt(
                Commodity::getPrice
        ).sum();
    }

    public Integer getUserDiscount(User user) {
        try {
            String code = this.usedDiscountRepository.findNotExpiredByUsername(user.getUsername()).getDiscountCode();
            return this.discountRepository.findByCode(code).getPercent();
        }
        catch (EntityNotFound ex) {
            return 0;
        }
    }

    public Integer getBuyListFinalPrice(User user) {
        return ((Long) (((long) this.getBuyListTotalPrice(user)) * ((long) (100 - this.getUserDiscount(user))) / 100)).intValue();
    }

    public void purchaseBuyList(User user) {
        int finalPrice = this.getBuyListFinalPrice(user);

        if (user.getCredit() < finalPrice)
            throw new InsufficientFunds(
                user.getUsername(),
                    finalPrice,
                user.getCredit()
            );

        user.decreaseCredit(
            finalPrice
        );
        this.userRepository.save(user);
        this.buyListRepository.purchaseAllUserBuyList(
            user.getUsername()
        );
        this.usedDiscountRepository.expireAllDiscounts(user.getUsername());
    }
}
