package baloot.database;

import baloot.exception.ColumnNotFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<Column, Object> convertRawRow(Map<String, Object> rawRow) {
        Map<Column, Object> convertedRow = new HashMap<>();

        for (String columnName: rawRow.keySet())
            convertedRow.put(this.getColumn(columnName), rawRow.get(columnName));

        return convertedRow;
    }

    private Map<String, Object> convertDBRow(Map<Column, Object> rawRow) {
        Map<String, Object> convertedRow = new HashMap<>();

        for (Column column: rawRow.keySet())
            convertedRow.put(column.getName(), rawRow.get(column));

        return convertedRow;
    }

    private void addRow(Map<Column, Object> newRow) {
        for (Column column: this.columns)
            column.verify(newRow.getOrDefault(column, null));

        rows.add(newRow);
    }

    public void insert(Map<String, Object> newRow) {
        this.addRow(this.convertRawRow(newRow));
    }

    private List<Integer> findRowsIndex(Map<Column, Object> conditions) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            Map<Column, Object> row = this.rows.get(i);
            boolean isOkay = true;
            for (Column condition: conditions.keySet())
                if (!row.getOrDefault(condition,null).equals(conditions.get(condition)))
                    isOkay = false;

            if (isOkay)
                result.add(i);
        }

        return result;
    }

    private List<Map<Column, Object>> findRows(Map<Column, Object> conditions) {
        List<Map<Column, Object>> result = new ArrayList<>();

        for (int i: this.findRowsIndex(conditions))
            result.add(this.rows.get(i));

        return result;
    }

    public List<Map<String, Object>> select(Map<String, Object> conditions) {
        List<Map<Column, Object>> rows = this.findRows(this.convertRawRow(conditions));
        List<Map<String, Object>> convertedRows = new ArrayList<>();

        for (Map<Column, Object> row: rows)
            convertedRows.add(this.convertDBRow(row));

        return convertedRows;
    }

    public void changeRows(Map<Column, Object> conditions, Map<Column, Object> newValues) {
        for (int i: this.findRowsIndex(conditions))
            for (Column changingColumn: newValues.keySet())
                this.rows.get(i).put(changingColumn, newValues.get(changingColumn));
    }

    public void update(Map<String, Object> conditions, Map<String, Object> newValues) {
        this.changeRows(this.convertRawRow(conditions), this.convertRawRow(newValues));
    }

    public void delete(Map<String, Object> conditions) {
        List<Map<Column, Object>> newTable = new ArrayList<>();

        List<Integer> matchingRows = this.findRowsIndex(this.convertRawRow(conditions));

        for (int i = 0; i < this.rows.size(); i++)
            if (!matchingRows.contains(i))
                newTable.add(this.rows.get(i));

        this.rows = newTable;
    }

    public void clear() {
        this.rows = new ArrayList<>();
    }
}
