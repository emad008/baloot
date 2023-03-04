package baloot;

import baloot.command.CommandHandler;
import baloot.command.CommandParser;
import baloot.controller.BuyListController;
import baloot.controller.CommodityController;
import baloot.controller.ProviderController;
import baloot.controller.UserController;
import baloot.database.Database;
import baloot.database.Table;
import baloot.repository.BuyListRepository;
import baloot.repository.CommodityRepository;
import baloot.repository.ProviderRepository;
import baloot.repository.UserRepository;
import baloot.service.BuyListService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Database db = new Database(
            List.of(
                new Table("users"),
                new Table("providers"),
                new Table("commodities"),
                new Table("buyListItems")
            )
        );
        ProviderRepository providerRepository = new ProviderRepository(db);
        UserRepository userRepository = new UserRepository(db);
        CommodityRepository commodityRepository = new CommodityRepository(db);
        BuyListRepository buyListRepository = new BuyListRepository(db);

        new Application(
            new Scanner(System.in),
            new CommandParser(),
            new CommandHandler(
                new UserController(
                    userRepository
                ),
                new ProviderController(
                    providerRepository
                ),
                new CommodityController(
                    commodityRepository
                ),
                new BuyListController(
                    userRepository,
                    commodityRepository,
                    buyListRepository,
                    new BuyListService(
                        buyListRepository,
                        commodityRepository
                    )
                )
            )
        ).run();
    }
}