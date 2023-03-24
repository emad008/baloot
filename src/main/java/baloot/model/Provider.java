package baloot.model;

import java.util.HashMap;
import java.util.Map;

public class Provider extends Model {
    private String name, registerDate;
    private int id;

    public Provider(int id, String name, String registerDate) {
        this.id = id;
        this.name = name;
        this.registerDate = registerDate;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getRegisterDate() { return registerDate; }

    @Override
    public Map<String, Object> getKey() {
        return Map.of(
            "id",
            this.getId()
        );
    }

    @Override
    public Map<String, Object> describe() {
        return Map.of(
            "id", this.getId(),
            "name", this.getName(),
            "registerDate", this.getRegisterDate()
        );
    }
}
