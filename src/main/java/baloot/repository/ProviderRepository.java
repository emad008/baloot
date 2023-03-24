package baloot.repository;
import baloot.database.Column;
import baloot.database.Database;
import baloot.database.Table;
import baloot.model.Commodity;
import baloot.model.Provider;
import baloot.model.User;

import java.util.*;

public class ProviderRepository extends Repository<Provider> {
    public ProviderRepository(Database db) {
        super(db);
    }

    public Provider findById(int id) {
        return this.findInstance(Map.of(
            "id", id
        ));
    }

    @Override
    public Table getTable() {
        return this.db.getTable("providers");
    }

    @Override
    public Provider convertRawDataToModel(Map<String, Object> rawData) {
        return new Provider(
            (Integer) rawData.get("id"),
            (String) rawData.get("name"),
            (String) rawData.get("registryDate")
        );
    }

    @Override
    public Map<String, Object> convertModelToRawData(Provider provider) {
        return Map.of(
            "id", provider.getId(),
            "name", provider.getName(),
            "registryDate", provider.getRegisterDate()
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
        table.addColumn(new Column(
            table,
            "registryDate",
            false
        ));
    }
}
