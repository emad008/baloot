package baloot.database.initializer;


import baloot.database.source.DataSource;
import baloot.database.source.OutboundSource;
import baloot.model.*;
import baloot.repository.*;
import org.eclipse.jetty.util.IO;

import java.io.IOException;

public class Initializer {
    private DataSource dataSource;
    private UserRepository userRepository;
    private CommodityRepository commodityRepository;
    private ProviderRepository providerRepository;
    private CommentRepository commentRepository;
    private BuyListRepository buyListRepository;

    public Initializer(
        DataSource dateSource,
        UserRepository userRepository,
        CommodityRepository commodityRepository,
        ProviderRepository providerRepository,
        CommentRepository commentRepository,
        BuyListRepository buyListRepository
    ) {
        this.dataSource = dateSource;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.providerRepository = providerRepository;
        this.commodityRepository = commodityRepository;
        this.buyListRepository = buyListRepository;
    }

    public void initialize() throws IOException {
        for (User user : this.dataSource.getUsers())
            this.userRepository.save(
                user
            );

        for (Provider provider : this.dataSource.getProviders())
            this.providerRepository.save(
                provider
            );

        for (Commodity commodity : this.dataSource.getCommodities())
            this.commodityRepository.save(
                commodity
            );

        for (Comment comment : this.dataSource.getComments())
            this.commentRepository.save(
                comment
            );

        for (BuyListItem buyListItem : this.dataSource.getBuyListItems())
            this.buyListRepository.save(
                buyListItem
            );
    }
}
