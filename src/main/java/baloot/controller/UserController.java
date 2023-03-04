package baloot.controller;

import baloot.model.User;
import baloot.repository.UserRepository;
import org.json.JSONObject;

public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JSONObject addUser(JSONObject userData) {
        this.userRepository.save(
            new User(
                userData.getString("username"),
                userData.getString("password"),
                userData.getString("email"),
                userData.getString("birthDate"),
                userData.getString("address"),
                userData.getInt("credit")
            )
        );

        return userData;
    }

}
