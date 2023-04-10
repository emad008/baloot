package baloot.repository;

import baloot.database.Column;
import baloot.database.Database;
import baloot.database.ForeignKeyColumn;
import baloot.database.Table;
import baloot.model.BuyListItem;
import baloot.model.Comment;

import java.util.List;
import java.util.Map;

public class CommentRepository extends Repository<Comment> {
    public CommentRepository(Database db) {
        super(db);
    }

    @Override
    public Table getTable() {
        return this.db.getTable("comments");
    }

    public Comment findById(String id) {
        return this.findInstance(Map.of(
            "uuid", id
        ));
    }

    public List<Comment> listByCommodityId(int commodityId) {
        return this.list(Map.of(
            "commodityId", commodityId
        ));
    }

    @Override
    public Map<String, Object> convertModelToRawData(Comment modelInstance) {
        return Map.of(
            "uuid", modelInstance.getUuid(),
            "userEmail", modelInstance.getUserEmail(),
            "commodityId", modelInstance.getCommodityId(),
            "text", modelInstance.getText(),
            "date", modelInstance.getDate(),
            "upVotes", modelInstance.getUpVotes(),
            "downVotes", modelInstance.getDownVotes()
        );
    }

    @Override
    public Comment convertRawDataToModel(Map<String, Object> rawDataList) {
        return new Comment(
            (String) rawDataList.get("uuid"),
            (String) rawDataList.get("userEmail"),
            (Integer) rawDataList.get("commodityId"),
            (String) rawDataList.get("text"),
            (String) rawDataList.get("date"),
            (List<String>) rawDataList.get("upVotes"),
            (List<String>) rawDataList.get("downVotes")
        );
    }

    @Override
    public void migrate() {
        Table table = this.getTable();
        table.addColumn(new Column(
            table,
            "uuid",
            true
        ));
        table.addColumn(new ForeignKeyColumn(
            table,
            "userEmail",
            false,
            this.db.getTable("users"),
            this.db.getTable("users").getColumn("email")
        ));
        table.addColumn(new ForeignKeyColumn(
            table,
            "commodityId",
            false,
            this.db.getTable("commodities"),
            this.db.getTable("commodities").getColumn("id")
        ));
        table.addColumn(new Column(
            table,
            "text",
            false
        ));
        table.addColumn(new Column(
            table,
            "date",
            false
        ));
        table.addColumn(new Column(
            table,
            "upVotes",
            false
        ));
        table.addColumn(new Column(
            table,
            "downVotes",
            false
        ));
    }
}
