package baloot;

import baloot.database.Database;
import baloot.database.Table;
import baloot.repository.*;
import baloot.factory.BasicFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;
import java.util.Map;

public class BalootTests {
    protected ProviderRepository providerRepository;
    protected UserRepository userRepository;
    protected CommodityRepository commodityRepository;
    protected BuyListRepository buyListRepository;
    protected CommentRepository commentRepository;

    @BeforeAll
    public void setUp() {
        Database db = new Database(
            List.of(
                new Table("users"),
                new Table("providers"),
                new Table("commodities"),
                new Table("buyListItems"),
                new Table("comments")
            )
        );
        this.providerRepository = new ProviderRepository(db);
        this.userRepository = new UserRepository(db);
        this.commodityRepository = new CommodityRepository(db);
        this.buyListRepository = new BuyListRepository(db);
        this.commentRepository = new CommentRepository(db);

        new BasicFactory(
            this.userRepository,
            this.providerRepository,
            this.commodityRepository,
            this.buyListRepository
        ).build();
    }

    @AfterAll
    public void tearDown() {
        this.userRepository.clear();
        this.providerRepository.clear();
        this.commodityRepository.clear();
        this.buyListRepository.clear();
    }
}
