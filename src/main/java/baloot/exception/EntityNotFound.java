package baloot.exception;

import java.util.Map;

public class EntityNotFound extends BalootException {
    private final String tableName;

    private final Map<String, Object> fields;

    public EntityNotFound(String tableName, Map<String, Object> fields) {
        this.tableName = tableName;
        this.fields = fields;
    }

    public String getTableName() {
        return this.tableName;
    }

    @Override
    public String getMessage() {
        String res = "no entity found in table " + this.tableName + ". " +
            "the search params where: ";

        for (String key: this.fields.keySet())
            res = res.concat(key + "=" + this.fields.get(key));

        return res;
    }
}
