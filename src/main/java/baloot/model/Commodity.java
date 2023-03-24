package baloot.model;

import baloot.database.Column;
import org.json.JSONObject;

import java.util.*;

public class Commodity extends Model {
    private int id, inStock, providerId, price;
    private String name;
    private List<String> categories;
    private Map<String, Float> scores;

    public Commodity(
        int id,
        String name,
        int providerId,
        int price,
        List<String> categories,
        Map<String, Float> scores,
        int inStock
    ) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.categories = categories;
        this.scores = scores;
        this.inStock = inStock;
    }

    public Commodity(
        int id,
        String name,
        int providerId,
        int price,
        List<String> categories,
        int inStock
    ) {
        this(
            id,
            name,
            providerId,
            price,
            categories,
            new HashMap<>(),
            inStock
        );
    }

    public boolean isSame(int id) { return id == this.id; }

    public void updateScore(String username, float score) {
        this.scores.put(username, score);
    }

    public boolean isAvailable() { return this.inStock > 0; }

    public void decreaseStock(int count) { this.inStock -= count; }

    public void increaseStock(int count) {
        this.inStock += count;
    }

    public int getProviderId() {
        return providerId;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public float getRating() {
        int sumScores = 0;

        for (Float score: this.scores.values())
            sumScores += score;

        return this.scores.size() > 0 ? (float) sumScores / this.scores.size() : 0;
    }

    public Map<String, Float> getScores() {
        return this.scores;
    }

    public int getInStock() {
        return this.inStock;
    }

    @Override
    public Map<String, Object> getKey() {
        return Map.of(
            "id",
            this.getId()
        );
    }

    @Override
    public Map<String, Object> describe() {
        Map<String, Object> res = new HashMap<>();

        res.put("id", this.getId());
        res.put("name", this.getName());
        res.put("providerId", this.getProviderId());
        res.put("price", this.getPrice());
        res.put("categories", this.getCategories());
        res.put("rating", this.getRating());
        res.put("inStock", this.getInStock());
        return res;
    }
}
