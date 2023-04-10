package com.baloot.database.initializer;


import com.baloot.database.source.DataSource;
import com.baloot.model.*;
import com.baloot.repository.*;

import java.io.IOException;

public class Initializer {
    private DataSource dataSource;
    private UserRepository userRepository;
    private CommodityRepository commodityRepository;
    private ProviderRepository providerRepository;
    private CommentRepository commentRepository;
    private BuyListRepository buyListRepository;
    private DiscountRepository discountRepository;

    public Initializer(
        DataSource dateSource,
        UserRepository userRepository,
        CommodityRepository commodityRepository,
        ProviderRepository providerRepository,
        CommentRepository commentRepository,
        BuyListRepository buyListRepository,
        DiscountRepository discountRepository
    ) {
        this.dataSource = dateSource;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.providerRepository = providerRepository;
        this.commodityRepository = commodityRepository;
        this.buyListRepository = buyListRepository;
        this.discountRepository = discountRepository;
    }

    public void initialize() {
        for (User user : this.dataSource.getUsers())
            this.userRepository.insert(
                user
            );

        for (Provider provider : this.dataSource.getProviders())
            this.providerRepository.insert(
                provider
            );

        for (Commodity commodity : this.dataSource.getCommodities())
            this.commodityRepository.insert(
                commodity
            );

        for (Comment comment : this.dataSource.getComments())
            this.commentRepository.insert(
                comment
            );

        for (BuyListItem buyListItem : this.dataSource.getBuyListItems())
            this.buyListRepository.insert(
                buyListItem
            );

        for (Discount discount : this.dataSource.getDiscounts())
            this.discountRepository.insert(
                    discount
            );
    }
}
