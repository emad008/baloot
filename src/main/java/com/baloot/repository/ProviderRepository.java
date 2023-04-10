package com.baloot.repository;

import com.baloot.database.Column;
import com.baloot.database.Database;
import com.baloot.database.Table;
import com.baloot.model.Provider;

import java.util.Map;

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
