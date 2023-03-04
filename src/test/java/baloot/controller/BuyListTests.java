package baloot.controller;

import baloot.BalootTests;
import baloot.exception.AlreadyInBuyList;
import baloot.exception.EntityNotFound;
import baloot.service.BuyListService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.print.attribute.SupportedValuesAttribute;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BuyListTests extends BalootTests {
    private BuyListController buyListController;

    @BeforeAll
    public void setUp() {
        super.setUp();

        this.buyListController = new BuyListController(
            this.userRepository,
            this.commodityRepository,
            this.buyListRepository,
            new BuyListService(
                this.buyListRepository,
                this.commodityRepository
            )
        );
    }

    @AfterAll
    public void tearDown() {
        super.tearDown();
    }


    @Test
    public void addToBuyList() {
        //given

        //when
        this.buyListController.addToBuyList(new JSONObject(Map.of(
            "username", "emad",
            "commodityId", 2
        )));

        //then
        JSONArray buyList = this.buyListController.getBuyList(new JSONObject(Map.of(
            "username", "emad"
        ))).getJSONArray("buyList");

        for (int i = 0; i < buyList.length(); i++)
            if (buyList.getJSONObject(i).getInt("id") == 2)
                return;

        fail();
    }

    @Test
    public void duplicateAddToBuyList() {
        //given

        //when
        try {
            this.buyListController.addToBuyList(new JSONObject(Map.of(
                "username", "emad",
                "commodityId", 1
            )));
        }
        catch (AlreadyInBuyList ex) {
            return;
        }

        //then
        fail();
    }
}
