package baloot.database.source;

import baloot.model.*;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Comment> getComments() {
        return List.of(
            new Comment(
                "6b52b772-bdd3-482f-aaa8-a1ca315073d",
                "emamiemad8@gmail.com",
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
}
