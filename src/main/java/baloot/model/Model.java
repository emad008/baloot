package baloot.model;

import java.util.Map;

public abstract class Model {
    public abstract Map<String, Object> getKey();

    public abstract Map<String, Object> describe();
}
