package baloot.database.source;

import baloot.model.*;

import java.util.List;

public interface DataSource {
    List<User> getUsers();
    List<Commodity> getCommodities();
    List<Provider> getProviders();
    List<Comment> getComments();
    List<BuyListItem> getBuyListItems();
}
