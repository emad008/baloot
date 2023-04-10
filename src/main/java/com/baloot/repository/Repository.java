package com.baloot.repository;

import com.baloot.database.Database;
import com.baloot.database.Table;
import com.baloot.exception.EntityNotFound;
import com.baloot.exception.KeyConstraint;
import com.baloot.exception.MultipleObjectsReturned;
import com.baloot.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Repository <M extends Model> {
    protected final Database db;

    public Repository(Database db) {
        this.db = db;
        this.migrate();
    }

    public abstract Table getTable();

    public void insert(M newEntity) {
        try {
            this.findInstance(newEntity.getKey());
            throw new KeyConstraint();
        }
        catch (EntityNotFound ex) {
            this.getTable().insert(this.convertModelToRawData(newEntity));
        }
    }

    public void save(M entity) {
        try {
            this.findInstance(entity.getKey());
            this.update(entity.getKey(), this.convertModelToRawData(entity));
        }
        catch (EntityNotFound ex) {
            this.insert(entity);
        }
    }

    public void update(
        Map<String, Object> conditions,
        Map<String, Object> newValues
    ) {
        this.getTable().update(conditions, newValues);
    }

    public void delete(M entity) {
        this.findInstance(entity.getKey());
        this.getTable().delete(entity.getKey());
    }
    public abstract Map<String, Object> convertModelToRawData(M modelInstance);

    public List<Map<String, Object>> convertModelToRawData(List<M> modelInstances) {
        List<Map<String, Object>> rawDataList = new ArrayList<>();
        for (M modelInstance: modelInstances)
            rawDataList.add(this.convertModelToRawData(modelInstance));

        return rawDataList;
    }

    public abstract M convertRawDataToModel(Map<String, Object> rawDataList);

    public List<M> convertRawDataToModel(List<Map<String, Object>> rawDataList) {
        List<M> models = new ArrayList<>();
        for (Map<String, Object> rawData: rawDataList)
            models.add(this.convertRawDataToModel(rawData));

        return models;
    }

    public List<M> list(Map<String, Object> conditions, List<String> sortFields) {
        List<Map<String, Object>> rawData = this.getTable().select(
                conditions,
                sortFields
        );

        return this.convertRawDataToModel(rawData);
    }

    public List<M> list(Map<String, Object> conditions) {
        List<Map<String, Object>> rawData = this.getTable().select(
                conditions,
                new ArrayList<>()
        );

        return this.convertRawDataToModel(rawData);
    }

    public List<M> list() {
        return this.list(new HashMap<>());
    }

    public M findInstance(Map<String, Object> key) throws EntityNotFound {
        List<Map<String, Object>> matchingRows = this.getTable().select(key);

        if (matchingRows.isEmpty())
            throw new EntityNotFound(
                this.getTable().getName(),
                key
            );

        if (matchingRows.size() != 1)
            throw new MultipleObjectsReturned();

        return this.convertRawDataToModel(matchingRows.get(0));
    }

    public void clear() {
        this.getTable().clear();
    }

    public abstract void migrate();
}
