package com.baloot.database;

import com.baloot.exception.EntityNotFoundInForeignKeyTargetTable;
import com.baloot.exception.UniqueConstraintException;

import java.util.Map;

public class ForeignKeyColumn extends Column {
    Column targetIdColumn;
    Table target;

    public ForeignKeyColumn(
        Table table,
        String name,
        Boolean unique,
        Table target,
        Column targetIdColumn
    ) {
        super(table, name, unique);
        this.target = target;
        this.targetIdColumn = targetIdColumn;
    }

    @Override
    public void verify(Object cell) throws UniqueConstraintException, EntityNotFoundInForeignKeyTargetTable {
        super.verify(cell);

        if (this.target.select(Map.of(targetIdColumn.getName(), cell)).isEmpty())
            throw new EntityNotFoundInForeignKeyTargetTable(
                this.getTable().getName(),
                this.target.getName(),
                cell,
                targetIdColumn.getName(),
                this.getName()
            );
    }
}
