package com.baloot;

import com.baloot.controller.*;
import com.baloot.database.Database;
import com.baloot.database.Table;
import com.baloot.database.initializer.Initializer;
import com.baloot.database.source.MockSource;
import com.baloot.database.source.OutboundSource;
import com.baloot.middleware.AuthorizationMiddleware;
import com.baloot.repository.*;
import com.baloot.service.BuyListService;
import com.baloot.servlets.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class Application implements ServletContextListener {
    private static Database createDatabase() {
        return new Database(
            List.of(
                new Table("users"),
                new Table("providers"),
                new Table("commodities"),
                new Table("buyListItems"),
                new Table("comments"),
                new Table("discounts"),
                new Table("used-discounts")
            )
        );
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Database db = Application.createDatabase();

        ProviderRepository providerRepository = new ProviderRepository(db);
        UserRepository userRepository = new UserRepository(db);
        CommodityRepository commodityRepository = new CommodityRepository(db);
        BuyListRepository buyListRepository = new BuyListRepository(db);
        CommentRepository commentRepository = new CommentRepository(db);
        DiscountRepository discountRepository = new DiscountRepository(db);
        UsedDiscountRepository usedDiscountRepository = new UsedDiscountRepository(db);

        BuyListController buyListController = new BuyListController(
                userRepository,
                commodityRepository,
                buyListRepository,
                new BuyListService(
                        buyListRepository,
                        commodityRepository,
                        userRepository,
                        usedDiscountRepository,
                        discountRepository
                )
        );
        CommodityController commodityController = new CommodityController(
                commodityRepository,
                commentRepository,
                userRepository,
                providerRepository
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
        DiscountController discountController = new DiscountController(
                usedDiscountRepository
        );

        new Initializer(
                new OutboundSource(
                        userRepository
                ),
                userRepository,
                commodityRepository,
                providerRepository,
                commentRepository,
                buyListRepository,
                discountRepository
        ).initialize();

        AuthorizationMiddleware authorizationMiddleware = new AuthorizationMiddleware(
                userRepository
        );

        ServletContext context = sce.getServletContext();
        context.addServlet("indexServlet", new IndexServlet(authorizationMiddleware)).addMapping("/index");
        context.addServlet("loginServlet", new LoginServlet(authorizationMiddleware)).addMapping("/login");
        context.addServlet("logoutServlet", new LogoutServlet(authorizationMiddleware)).addMapping("/logout");
        context.addServlet("commoditiesServlet", new CommoditiesServlet(
                authorizationMiddleware,
                commodityController
        )).addMapping("/commodities");
        context.addServlet("creditServlet", new CreditServlet(
                authorizationMiddleware,
                userController,
                userRepository
        )).addMapping("/credit");
        context.addServlet("commodityServlet", new CommodityServlet(
                authorizationMiddleware,
                commodityController
        )).addMapping("/commodities/*");
        context.addServlet("commentServlet", new CommentServlet(
                authorizationMiddleware,
                commentController
        )).addMapping("/comments");
        context.addServlet("rateServlet", new RateServlet(
                authorizationMiddleware,
                commodityController
        )).addMapping("/rate");
        context.addServlet("buyListServlet", new BuyListServlet(
                authorizationMiddleware,
                buyListController
        )).addMapping("/buyList", "/buyList/*");
        context.addServlet("buyListRemoveServlet", new BuyListItemRemoveServlet(
                authorizationMiddleware,
                buyListController
        )).addMapping("/buyList/remove/*");
        context.addServlet("voteServlet", new VoteCommentServlet(
                authorizationMiddleware,
                commentController
        )).addMapping("/vote-comment/*");
        context.addServlet("paymentServlet", new PaymentServlet(
                authorizationMiddleware,
                buyListController
        )).addMapping("/payment");
        context.addServlet("discountServlet", new DiscountServlet(
                authorizationMiddleware,
                discountController
        )).addMapping("/discount");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
