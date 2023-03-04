package baloot.factory;

import baloot.model.BuyListItem;
import baloot.model.Commodity;
import baloot.model.Provider;
import baloot.model.User;
import baloot.repository.BuyListRepository;
import baloot.repository.CommodityRepository;
import baloot.repository.ProviderRepository;
import baloot.repository.UserRepository;

import java.util.List;

public class BasicFactory {

    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final CommodityRepository commodityRepository;
    private final BuyListRepository buyListRepository;

    public BasicFactory(
        UserRepository userRepository,
        ProviderRepository providerRepository,
        CommodityRepository commodityRepository,
        BuyListRepository buyListRepository
    ) {
        this.userRepository = userRepository;
        this.providerRepository = providerRepository;
        this.commodityRepository = commodityRepository;
        this.buyListRepository = buyListRepository;
    }

    private List<User> getUsers() {
        return List.of(
            new User(
                "emad",
                "1234",
                "emamiemad8@gmail.com",
                "2000-10-30",
                "tehran",
                5
            )
        );
    }

    private List<Provider> getProviders() {
        return List.of(
            new Provider(
                1,
                "emad.Co",
                "2000-10-30"
            )
        );
    }

    private List<Commodity> getCommodities() {
        return List.of(
            new Commodity(
                1,
                "atr",
                1,
                10,
                List.of("luxury", "consumable"),
                5
            ),
            new Commodity(
                2,
                "kif",
                1,
                100,
                List.of("wearable"),
                1
            ),
            new Commodity(
                2,
                "much",
                1,
                1000,
                List.of("lovely"),
                1000
            )
        );
    }

    public List<BuyListItem> getBuyListItems() {
        return List.of(
            new BuyListItem(
                "emad",
                1
            )
        );
    }

    public void build() {
        for (User user: this.getUsers())
            this.userRepository.save(
                user
            );

        for (Provider provider: this.getProviders())
            this.providerRepository.save(
                provider
            );

        for (Commodity commodity: this.getCommodities())
            this.commodityRepository.save(
                commodity
            );

        for (BuyListItem buyListItem: this.getBuyListItems())
            this.buyListRepository.save(
                buyListItem
            );
    }
}
