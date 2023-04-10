package baloot.database;

import java.util.List;
import java.util.Map;

public class Database {
    private List<Table> tables;

    public Database(
        List<Table> tables
    ) {
        this.tables = tables;
    }

    public Table getTable(String tableName) {
        for (Table table: this.tables)
            if (table.getName().equals(tableName))
                return table;

        return null;
    }
}
