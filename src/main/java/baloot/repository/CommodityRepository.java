package baloot.repository;

import baloot.database.Column;
import baloot.database.Database;
import baloot.database.ForeignKeyColumn;
import baloot.database.Table;
import baloot.model.Commodity;
import baloot.model.Provider;
import org.json.JSONArray;

import java.util.*;
public class CommodityRepository extends Repository<Commodity> {
    public CommodityRepository(Database db) {
        super(db);
    }

    public Commodity findById(int id) {
        return this.findInstance(Map.of(
            "id", id
        ));
    }

    public List<Commodity> listByCategory(String category) {
        // TODO. database should handle 'in' filter but for now, we will get all rows and search manually
        List<Commodity> commodities = this.list();

        List<Commodity> result = new ArrayList<>();

        for (Commodity commodity: commodities)
            if (commodity.getCategories().contains(category))
                result.add(commodity);

        return result;
    }

    @Override
    public Table getTable() {
        return this.db.getTable("commodities");
    }

    @Override
    public Commodity convertRawDataToModel(Map<String, Object> rawData) {
        return new Commodity(
            (Integer) rawData.get("id"),
            (String) rawData.get("name"),
            (Integer) rawData.get("providerId"),
            (Integer) rawData.get("price"),
            (List<String>) rawData.get("categories"),
            (Map<String, Integer>) rawData.get("scores"),
            (Integer) rawData.get("inStock")
        );
    }

    @Override
    public Map<String, Object> convertModelToRawData(Commodity commodity) {
        return Map.of(
            "id", commodity.getId(),
            "name", commodity.getName(),
            "providerId", commodity.getProviderId(),
            "price", commodity.getPrice(),
            "categories", commodity.getCategories(),
            "inStock", commodity.getInStock(),
            "scores", commodity.getScores()
        );
    }

    @Override
    public void migrate() {
        Table table = this.getTable();
        table.addColumn(new Column(
            table,
            "id",
            true
        ));
        table.addColumn(new Column(
            table,
            "name",
            false
        ));
        table.addColumn(new ForeignKeyColumn(
            table,
            "providerId",
            false,
            this.db.getTable("providers"),
            this.db.getTable("providers").getColumn("id")
        ));
        table.addColumn(new Column(
            table,
            "price",
            false
        ));
        table.addColumn(new Column(
            table,
            "scores",
            false
        ));
        table.addColumn(new Column(
            table,
            "categories",
            false
        ));
        table.addColumn(new Column(
            table,
            "inStock",
            false
        ));
    }
}
