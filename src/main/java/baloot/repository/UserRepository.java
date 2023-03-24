package baloot.repository;
import baloot.database.Column;
import baloot.database.Database;
import baloot.database.Table;
import baloot.exception.EntityNotFound;
import baloot.model.User;

import java.util.*;

public class UserRepository extends Repository<User> {
    public UserRepository(Database db) {
        super(db);
    }

    @Override
    public Table getTable() {
        return this.db.getTable("users");
    }

    public User findByUsername(String username) throws EntityNotFound {
       return this.findInstance(Map.of("username", username));
    }

    public User findByEmail(String email) throws EntityNotFound {
        return this.findInstance(Map.of(
            "email", email
        ));
    }

    @Override
    public User convertRawDataToModel(Map<String, Object> rawData) {
        return new User(
            (String) rawData.get("username"),
            (String) rawData.get("password"),
            (String) rawData.get("email"),
            (String) rawData.get("birthDate"),
            (String) rawData.get("address"),
            (Integer) rawData.get("credit")
        );
    }

    @Override
    public Map<String, Object> convertModelToRawData(User user) {
        return Map.of(
            "username", user.getUsername(),
            "password", user.getPassword(),
            "email", user.getEmail(),
            "birthDate", user.getBirthDate(),
            "address", user.getAddress(),
            "credit", user.getCredit()
        );
    }
    @Override
    public void migrate() {
        Table table = this.getTable();
        table.addColumn(new Column(
            table,
            "username",
            true
        ));
        table.addColumn(new Column(
            table,
            "password",
            false
        ));
        table.addColumn(new Column(
            table,
            "email",
            false
        ));
        table.addColumn(new Column(
            table,
            "birthDate",
            false
        ));
        table.addColumn(new Column(
            table,
            "address",
            false
        ));
        table.addColumn(new Column(
            table,
            "credit",
            false
        ));
    }
}
