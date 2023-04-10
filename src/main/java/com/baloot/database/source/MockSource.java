package com.baloot.database.source;

import com.baloot.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MockSource implements DataSource {
    public List<User> getUsers() {
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

    public List<Provider> getProviders() {
        return List.of(
            new Provider(
                1,
                "emad.Co",
                "2000-10-30"
            )
        );
    }

    public List<Commodity> getCommodities() {
        return List.of(
            new Commodity(
                2,
                "kif",
                1,
                100,
                List.of("wearable"),
                1
            ),
            new Commodity(
                3,
                "much",
                1,
                1000,
                List.of("lovely", "consumable"),
                1000
            ),
            new Commodity(
                1,
                "atr",
                1,
                10,
                List.of("luxury", "consumable"),
                Map.of("default", (float) 3),
                5
            )
        );
    }

    public List<Comment> getComments() {
        return List.of(
            new Comment(
                "6b52b772-bdd3-482f-aaa8-a1ca315073d",
                "emad",
                1,
                "fuck this shit",
                "2022-1-1",
                new ArrayList<>(List.of("emad")),
                new ArrayList<>()
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

    public List<Discount> getDiscounts() {
        return List.of(
                new Discount(
                        "ABCD",
                        10
                )
        );
    }
}
