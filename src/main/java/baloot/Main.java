package baloot;

import baloot.controller.*;
import baloot.database.Database;
import baloot.database.Table;
import baloot.database.initializer.Initializer;
import baloot.database.source.OutboundSource;
import baloot.middleware.JavelinMiddleware;
import baloot.repository.*;

import java.io.IOException;
import java.util.List;

import baloot.service.BuyListService;
import io.javalin.Javalin;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinMustache;
import org.eclipse.jetty.util.IO;


public class Main {
    private static Database createDatabase() {
        Database db = new Database(
            List.of(
                new Table("users"),
                new Table("providers"),
                new Table("commodities"),
                new Table("buyListItems"),
                new Table("comments")
            )
        );

        return db;
    }

    private static Javalin createApplication() throws IOException {
        Database db = Main.createDatabase();

        ProviderRepository providerRepository = new ProviderRepository(db);
        UserRepository userRepository = new UserRepository(db);
        CommodityRepository commodityRepository = new CommodityRepository(db);
        BuyListRepository buyListRepository = new BuyListRepository(db);
        CommentRepository commentRepository = new CommentRepository(db);

        BuyListController buyListController = new BuyListController(
            userRepository,
            commodityRepository,
            buyListRepository,
            new BuyListService(
                buyListRepository,
                commodityRepository,
                userRepository
            )
        );
        CommodityController commodityController = new CommodityController(
            commodityRepository,
            commentRepository,
            userRepository
        );
        ProviderController providerController = new ProviderController(
            providerRepository,
            commodityRepository
        );
        UserController userController = new UserController(
            userRepository,
            buyListRepository,
            commodityRepository
        );
        CommentController commentController = new CommentController(
            commentRepository,
            userRepository
        );

        new Initializer(
            new OutboundSource(),
            userRepository,
            commodityRepository,
            providerRepository,
            commentRepository,
            buyListRepository
        ).initialize();

        JavalinRenderer.register(
            new JavalinMustache(),
            ".html"
        );
        var app = Javalin.create(/*config*/)
            .get("/commodities", ctx -> new JavelinMiddleware(
                (j -> commodityController.getCommoditiesList()),
                "templates/Commodities.html"
            ).middleware(ctx))
            .get("/commodities/{id}", ctx -> new JavelinMiddleware(
                commodityController::getCommodityById,
                "templates/Commodity.html"
            ).middleware(ctx))
            .get("/providers/{id}", ctx -> new JavelinMiddleware(
                providerController::getProviderById,
                "templates/Provider.html"
            ).middleware(ctx))
            .get("/users/{username}", ctx -> new JavelinMiddleware(
                userController::getUserById,
                "templates/User.html"
            ).middleware(ctx))
            .post("/rateCommodity/{username}/{commodityId}/{score}", ctx -> new JavelinMiddleware(
                commodityController::rateCommodity,
                "templates/200.html"
            ).middleware(ctx))
            .post("/addToBuyList/{username}/{commodityId}", ctx -> new JavelinMiddleware(
                buyListController::addToBuyList,
                "templates/200.html"
            ).middleware(ctx))
            .post("/removeFromBuyList/{username}/{commodityId}", ctx -> new JavelinMiddleware(
                buyListController::removeFromBuyList,
                "templates/200.html"
            ).middleware(ctx))
            .post("/voteComment/{username}/{commentId}/{vote}", ctx -> new JavelinMiddleware(
                commentController::voteComment,
                "templates/200.html"
            ).middleware(ctx))
            .post("/purchaseBuyList/{username}", ctx -> new JavelinMiddleware(
                buyListController::purchaseAll,
                "templates/200.html"
            ).middleware(ctx))
            .post("/addCredit/{username}/{amount}", ctx -> new JavelinMiddleware(
                userController::addCredit,
                "templates/200.html"
            ).middleware(ctx))
            .get("/commodities/search/{startPrice}/{endPrice}", ctx -> new JavelinMiddleware(
                commodityController::listCommoditiesByPriceRange,
                "templates/200.html"
            ).middleware(ctx))
            .get("/commodities/search/{category}", ctx -> new JavelinMiddleware(
                commodityController::listCommoditiesByCategory,
                "templates/200.html"
            ).middleware(ctx))
            .get("/200", ctx -> ctx.render("templates/200.html"))
            .get("/400", ctx -> ctx.render("templates/400.html"))
            .get("/404", ctx -> ctx.render("templates/404.html"))
            .get("/500", ctx -> ctx.render("templates/500.html"))
            .start(7070);

        return app;
    }

    public static void main(String[] args) throws IOException {
        Main.createApplication();
    }
}