package baloot.controller;

import baloot.BalootTests;
import baloot.exception.EntityNotFound;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommodityTests extends BalootTests {
    private CommodityController commodityController;

    @BeforeAll
    public void setUp() {
        super.setUp();

        this.commodityController = new CommodityController(
            this.commodityRepository,
            this.commentRepository,
            this.userRepository
        );
    }

    @AfterAll
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void rateCommodity() {
        //given
        float oldRate = this.commodityRepository.findById(1).getRating();

        //when
        this.commodityController.rateCommodity(new JSONObject(Map.of(
            "username", "emad",
            "commodityId", 1,
            "score", 5
        )));

        //then
        float newRate = this.commodityRepository.findById(1).getRating();

        assertNotEquals(oldRate, newRate);
    }

    @Test
    public void duplicateRateCommodity() {
        //given
        this.commodityController.rateCommodity(new JSONObject(Map.of(
            "username", "shayan",
            "commodityId", 1,
            "score", 7
        )));
        float oldRate = this.commodityRepository.findById(1).getRating();

        //when
        this.commodityController.rateCommodity(new JSONObject(Map.of(
            "username", "shayan",
            "commodityId", 1,
            "score", 7
        )));

        //then
        float newRate = this.commodityRepository.findById(1).getRating();

        assertEquals(oldRate, newRate);
    }

    @Test
    public void getCommodityById() {
        //given

        //when
        JSONObject res = this.commodityController.getCommodityById(new JSONObject(Map.of(
            "id", 1
        )));

        //then
        assertEquals(res.getString("name"), "atr");
    }

    @Test
    public void getCommodityByIdNotFound() {
        //given

        //when
        try {
            JSONObject res = this.commodityController.getCommodityById(new JSONObject(Map.of(
                "id", 5
            )));
        }
        catch (EntityNotFound ex) {
            return;
        }

        //then
        fail();
    }

    @Test
    public void getCommodityByCategory() {
        //given

        //when
        JSONArray res = this.commodityController.listCommoditiesByCategory(new JSONObject(Map.of(
            "category", "luxury"
        ))).getJSONArray("commoditiesListByCategory");

        //then
        assertEquals(res.length(), 1);
        assertEquals(res.getJSONObject(0).getString("name"), "atr");
    }

    @Test
    public void getCommodityByCategoryNotFound() {
        //given

        //when
        JSONArray res = this.commodityController.listCommoditiesByCategory(new JSONObject(Map.of(
            "category", "badcommodities"
        ))).getJSONArray("commoditiesListByCategory");

        //then
        assertEquals(res.length(), 0);
    }

    @Test
    public void listCommoditiesByPriceRange() {
        //given

        //when
        JSONArray res = this.commodityController.listCommoditiesByPriceRange(new JSONObject(Map.of(
            "startPrice", 1,
            "endPrice", 10
        ))).getJSONArray("commodities");

        //then
        assertEquals(res.length(), 1);
        assertEquals(res.getJSONObject(0).getString("name"), "atr");
    }
}
