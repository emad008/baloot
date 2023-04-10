package baloot.command;

import baloot.controller.BuyListController;
import baloot.controller.CommodityController;
import baloot.controller.ProviderController;
import baloot.controller.UserController;
import baloot.domain.CommandDTO;
import baloot.exception.AlreadyInBuyList;
import baloot.exception.EntityNotFound;
import baloot.exception.EntityNotFoundInForeignKeyTargetTable;
import baloot.exception.InvalidCommand;
import org.json.*;

public class CommandHandler {
    private final CommodityController commodityController;
    private final ProviderController providerController;
    private final UserController userController;

    private final BuyListController buyListController;

    public CommandHandler(
            UserController userController,
            ProviderController providerController,
            CommodityController commodityController,
            BuyListController buyListController
    ) {
        this.userController = userController;
        this.providerController = providerController;
        this.commodityController = commodityController;
        this.buyListController = buyListController;
    }
    public JSONObject doCommand(CommandDTO command) {
        JSONObject response = new JSONObject();
        response.put("success", false);

        try {
            JSONObject responseData = switch (command.getType()) {
                case "addUser" -> this.userController.addUser(
                    command.getData()
                );
                case "addProvider" -> this.providerController.addProvider(
                    command.getData()
                );
                case "addCommodity" -> this.commodityController.addCommodity(
                    command.getData()
                );
                case "getCommoditiesList" -> this.commodityController.getCommoditiesList();
                case "rateCommodity" -> this.commodityController.rateCommodity(
                    command.getData()
                );
                case "addToBuyList" -> this.buyListController.addToBuyList(
                    command.getData()
                );
                case "removeFromBuyList" -> this.buyListController.removeFromBuyList(
                    command.getData()
                );
                case "getCommodityById" -> this.commodityController.getCommodityById(
                    command.getData()
                );
                case "getCommoditiesByCategory" -> this.commodityController.listCommoditiesByCategory(
                    command.getData()
                );
                case "getBuyList" -> this.buyListController.getBuyList(
                    command.getData()
                );
                default -> throw new InvalidCommand("Invalid command");
            };
            response.put("success", true);
            response.put("data", responseData);
        }
        catch (InvalidCommand | EntityNotFound | AlreadyInBuyList e) {
            response.put("data", e.getMessage());
        }
        catch (JSONException e) {
            response.put("data", "serialization exception. invalid input for command " + command.getType());
        }

        return response;
    }
}
