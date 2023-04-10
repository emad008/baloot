package com.baloot.exception;

import java.util.Map;

public class EntityNotFoundInForeignKeyTargetTable extends EntityNotFound {
    private final String sourceTable;

    private final String targetColumn;
    private final String foreignKeyName;
    private final Object foreignKeyId;

    public EntityNotFoundInForeignKeyTargetTable(
        String sourceTable,
        String targetTable,
        Object foreignKeyId,
        String targetColumn,
        String foreignKeyName
    ) {
        super(targetTable, Map.of(targetColumn, foreignKeyId));
        this.sourceTable = sourceTable;
        this.foreignKeyName = foreignKeyName;
        this.targetColumn = targetColumn;
        this.foreignKeyId = foreignKeyId;
    }

    @Override
    public String getMessage() {
        return "invalid id for foreign key " +
            this.foreignKeyName + " in table " + this.sourceTable + ". " +
            "cant find " + this.targetColumn + "=" + this.foreignKeyId + " " +
            "in table " + this.getTableName() +
        '}';
    }
}
