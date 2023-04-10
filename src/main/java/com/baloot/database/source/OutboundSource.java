package com.baloot.database.source;

import com.baloot.model.*;
import com.baloot.repository.UserRepository;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OutboundSource implements DataSource {
    private final OkHttpClient restClient;
    private final String baseUrl;
    private final UserRepository userRepository;

    public OutboundSource(
            UserRepository userRepository
    ) {
        this.restClient = new OkHttpClient();
        this.restClient.setConnectTimeout(20, TimeUnit.SECONDS);
        this.restClient.setWriteTimeout(20, TimeUnit.SECONDS);
        this.restClient.setReadTimeout(20, TimeUnit.SECONDS);
        this.baseUrl = "http://5.253.25.110:5000/api";
        this.userRepository = userRepository;
    }

    public JSONArray call(String url, int triesLeft) {
        if (triesLeft <= 0)
            throw new RuntimeException("no tries left");

        Request request = new Request.Builder()
            .url(url)
            .build();

        JSONArray apiResult = new JSONArray();
        try (ResponseBody body = this.restClient.newCall(request).execute().body()) {
            apiResult = new JSONArray(body.string());
        }
        catch (Exception ex) {
            return this.call(url, triesLeft - 1);
        }

        if (apiResult.length() == 0)
            return this.call(url, triesLeft - 1);

        return apiResult;
    }

    public List<User> getUsers() {
        List<User> res = new ArrayList<>();
        JSONArray apiResult = this.call(this.baseUrl + "/users", 3);
        for (int i = 0; i < apiResult.length(); i++) {
            JSONObject userData = apiResult.getJSONObject(i);
            res.add(new User(
                userData.getString("username"),
                userData.getString("password"),
                userData.getString("email"),
                userData.getString("birthDate"),
                userData.getString("address"),
                userData.getInt("credit")
            ));
        }

        return res;
    }

    public List<Provider> getProviders() {
        JSONArray apiResult = this.call(this.baseUrl + "/providers", 3);
        List<Provider> res = new ArrayList<>();

        for (int i = 0; i < apiResult.length(); i++) {
            JSONObject providerData = apiResult.getJSONObject(i);
            res.add(new Provider(
                providerData.getInt("id"),
                providerData.getString("name"),
                providerData.getString("registryDate")
            ));
        }

        return res;
    }

    public List<Commodity> getCommodities() {
        List<Commodity> res = new ArrayList<>();

        JSONArray apiResult = this.call(this.baseUrl + "/commodities", 3);

        for (int i = 0; i < apiResult.length(); i++) {
            JSONObject commodityData = apiResult.getJSONObject(i);

            List<String> categories = new ArrayList<>();
            for (int j = 0; j < commodityData.getJSONArray("categories").length(); j++)
                categories.add(
                    commodityData.getJSONArray("categories").getString(
                        j
                    )
                );

            Map<String, Float> ratingMap = new HashMap<>();
            ratingMap.put("default", commodityData.getFloat("rating"));

            res.add(new Commodity(
                commodityData.getInt("id"),
                commodityData.getString("name"),
                commodityData.getInt("providerId"),
                commodityData.getInt("price"),
                categories,
                ratingMap,
                commodityData.getInt("inStock")
            ));
        }

        return res;
    }

    public List<BuyListItem> getBuyListItems() {
        return new ArrayList<>();
    }

    public List<Comment> getComments() {
        List<Comment> res = new ArrayList<>();

        JSONArray apiResult = this.call(this.baseUrl + "/comments", 3);
        for (int i = 0; i < apiResult.length(); i++) {
            JSONObject commentData = apiResult.getJSONObject(i);
            res.add(new Comment(
                this.userRepository.findByEmail(commentData.getString("userEmail")).getUsername(),
                commentData.getInt("commodityId"),
                commentData.getString("text"),
                commentData.getString("date")
            ));
        }

        return res;
    }

    public List<Discount> getDiscounts() {
        List<Discount> res = new ArrayList<>();

        JSONArray apiResult = this.call(this.baseUrl + "/discount", 3);
        for (int i = 0; i < apiResult.length(); i++) {
            JSONObject discountData = apiResult.getJSONObject(i);
            res.add(new Discount(
                    discountData.getString("discountCode"),
                    discountData.getInt("discount")
            ));
        }

        return res;
    }
}
