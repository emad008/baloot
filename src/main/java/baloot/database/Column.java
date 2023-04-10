package baloot.database;

import baloot.exception.UniqueConstraintException;

import java.util.Map;

public class Column {
    private String name;
    private Boolean unique;

    private Table table;

    public Column(
        Table table,
        String name,
        Boolean unique
    ) {
        this.table = table;
        this.name = name;
        this.unique = unique;
    }

    public String getName() {
        return this.name;
    }

    public Table getTable() {
        return this.table;
    }

    public void verify(Object cell) throws UniqueConstraintException {
        if (this.unique)
            if (!this.table.select(Map.of(this.getName(), cell)).isEmpty())
                throw new UniqueConstraintException();
    }

    @Override
    public String toString() {
        return "Column{" +
            "name='" + name + '\'' +
            '}';
    }
}
