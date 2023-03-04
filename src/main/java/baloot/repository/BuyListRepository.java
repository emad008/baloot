package baloot.repository;

import baloot.database.Column;
import baloot.database.Database;
import baloot.database.ForeignKeyColumn;
import baloot.database.Table;
import baloot.model.BuyListItem;

import java.util.List;
import java.util.Map;

public class BuyListRepository extends Repository<BuyListItem> {
    public BuyListRepository(Database db) {
        super(db);
    }

    public List<BuyListItem> listByUsername(String username) {
        return this.list(Map.of(
            "username", username
        ));
    }

    @Override
    public BuyListItem convertRawDataToModel(Map<String, Object> rawDataList) {
        return new BuyListItem(
            (String) rawDataList.get("userId"),
            (Integer) rawDataList.get("commodityId")
        );
    }

    @Override
    public Map<String, Object> convertModelToRawData(BuyListItem buyListItem) {
        return Map.of(
            "username", buyListItem.getUsername(),
            "commodityId", buyListItem.getCommodityId()
        );
    }

    @Override
    public Table getTable() {
        return this.db.getTable("buyListItems");
    }

    @Override
    public void migrate() {
        Table table = this.getTable();
        table.addColumn(new ForeignKeyColumn(
            table,
            "username",
            false,
            this.db.getTable("users"),
            this.db.getTable("users").getColumn("username")
        ));
        table.addColumn(new ForeignKeyColumn(
            table,
            "commodityId",
            false,
            this.db.getTable("commodities"),
            this.db.getTable("commodities").getColumn("id")
        ));
    }
}
