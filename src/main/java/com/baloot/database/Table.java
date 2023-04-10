package com.baloot.database;

import com.baloot.exception.ColumnNotFound;
import com.baloot.exception.InvalidConditionType;
import com.baloot.exception.NotComparableColumn;
import com.baloot.exception.ColumnTypeIsNotIterable;
import kotlin.Pair;

import java.util.*;

public class Table {
    private String name;
    private List<Map<Column, Object>> rows;
    protected List<Column> columns;

    public Table(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public void addColumn(Column newColumn) {
        this.columns.add(newColumn);
    }

    public Column getColumn(String columnName) {
        for (Column column: this.columns)
            if (column.getName().equals(columnName))
                return column;

        throw new ColumnNotFound();
    }

    public String getName() {
        return this.name;
    }

    private List<Column> convertRawRowToDBRow(List<String> rawRow) {
        List<Column> dbRow = new ArrayList<>();

        for (String columnName: rawRow)
            dbRow.add(this.getColumn(columnName));

        return dbRow;
    }

    private Map<Column, Object> convertRawRowToDBRow(Map<String, Object> rawRow) {
        Map<Column, Object> dbRow = new HashMap<>();

        for (String columnName: rawRow.keySet())
            dbRow.put(this.getColumn(columnName), rawRow.get(columnName));

        return dbRow;
    }

    private Map<String, Object> convertDBRowToRawRow(Map<Column, Object> dbRow) {
        Map<String, Object> rawRow = new HashMap<>();

        for (Column column: dbRow.keySet())
            rawRow.put(column.getName(), dbRow.get(column));

        return rawRow;
    }

    private Pair<Column, ConditionType> parseCondition(String conditionName) {
        if (conditionName.contains("__")) {
            String columnName = conditionName.split("__")[0];
            String conditionSymbol = conditionName.split("__")[1];

            if (conditionSymbol.equals("contains"))
                return new Pair<>(this.getColumn(columnName), ConditionType.CONTAINS);

            throw new InvalidConditionType(conditionSymbol);
        }

        return new Pair<>(this.getColumn(conditionName), ConditionType.EQ);
    }

    private Map<Column, Pair<ConditionType, Object>> parseConditions(Map<String, Object> conditions) {
        Map<Column, Pair<ConditionType, Object>> result = new HashMap<>();

        for (String conditionName: conditions.keySet()) {
            Pair<Column, ConditionType> parsedCondition = this.parseCondition(conditionName);
            result.put(
                    parsedCondition.getFirst(),
                    new Pair<>(
                            parsedCondition.getSecond(),
                            conditions.get(conditionName)
                    )
            );
        }
        return result;
    }

    private void addRow(Map<Column, Object> newRow) {
        for (Column column: this.columns)
            column.verify(newRow.getOrDefault(column, null));

        rows.add(newRow);
    }

    public void insert(Map<String, Object> newRow) {
        this.addRow(this.convertRawRowToDBRow(newRow));
    }

    private Boolean checkCondition(
            Map<Column, Object> row,
            Column conditionColumn,
            ConditionType conditionType,
            Object conditionValue
    ) {
        Object rowValue = row.getOrDefault(conditionColumn,null);

        switch (conditionType) {
            case CONTAINS -> {
                if (rowValue instanceof Collection<?>) {
                    return ((Collection<?>) rowValue).contains(conditionValue);
                }
                if (rowValue instanceof String) {
                    return ((String) rowValue).contains((String) conditionValue);
                }
                throw new ColumnTypeIsNotIterable(
                    rowValue.getClass().getName(),
                    conditionColumn.getName()
                );
            }
            case EQ -> {
                return rowValue.equals(conditionValue);
            }
        }

        return false;
    }

    private List<Integer> findRowsIndex(Map<Column, Pair<ConditionType, Object>> conditions) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            Map<Column, Object> row = this.rows.get(i);
            boolean isOkay = true;
            for (Column conditionColumn: conditions.keySet())
                if (!this.checkCondition(
                        row,
                        conditionColumn,
                        conditions.get(conditionColumn).getFirst(),
                        conditions.get(conditionColumn).getSecond()
                ))
                    isOkay = false;

            if (isOkay)
                result.add(i);
        }

        return result;
    }

    private List<Map<Column, Object>> findRows(Map<Column, Pair<ConditionType, Object>> conditions) {
        List<Map<Column, Object>> result = new ArrayList<>();

        for (int i: this.findRowsIndex(conditions))
            result.add(this.rows.get(i));

        return result;
    }

    private List<Map<Column, Object>> sort(List<Map<Column, Object>> rows, List<Column> orderBy) {
        rows.sort((rowB, rowA) -> {
            for (Column column: orderBy)
                if (rowA.get(column) != rowB.get(column)) {
                    Object valueA = rowA.get(column);
                    Object valueB = rowB.get(column);

                    if (valueA instanceof Integer)
                        return ((Integer) valueA).compareTo((Integer) valueB);
                    if (valueA instanceof String)
                        return ((String) valueA).compareTo((String) valueB);
                    if (valueA instanceof Float)
                        return ((Float) valueA).compareTo((Float) valueB);
                    throw new NotComparableColumn(
                            column.getName(),
                            valueA.getClass().getName()
                    );
                }
            return 0;
        });
        return rows;
    }

    public List<Map<String, Object>> select(Map<String, Object> conditions, List<String> orderBy) {
        List<Map<Column, Object>> rows = this.findRows(this.parseConditions(conditions));
        List<Map<Column, Object>> sortedRows = this.sort(rows, this.convertRawRowToDBRow(orderBy));
        List<Map<String, Object>> convertedRows = new ArrayList<>();

        for (Map<Column, Object> row: sortedRows)
            convertedRows.add(this.convertDBRowToRawRow(row));

        return convertedRows;
    }

    public List<Map<String, Object>> select(Map<String, Object> conditions) {
        return this.select(conditions, new ArrayList<>());
    }

    public void changeRows(Map<Column, Pair<ConditionType, Object>> conditions, Map<Column, Object> newValues) {
        for (int i: this.findRowsIndex(conditions))
            for (Column changingColumn: newValues.keySet())
                this.rows.get(i).put(changingColumn, newValues.get(changingColumn));
    }

    public void update(Map<String, Object> conditions, Map<String, Object> newValues) {
        this.changeRows(this.parseConditions(conditions), this.convertRawRowToDBRow(newValues));
    }

    public void delete(Map<String, Object> conditions) {
        List<Map<Column, Object>> newTable = new ArrayList<>();

        List<Integer> matchingRows = this.findRowsIndex(this.parseConditions(conditions));

        for (int i = 0; i < this.rows.size(); i++)
            if (!matchingRows.contains(i))
                newTable.add(this.rows.get(i));

        this.rows = newTable;
    }

    public void clear() {
        this.rows = new ArrayList<>();
    }
}
