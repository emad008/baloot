package baloot.model;


import java.util.*;

public class Comment extends Model {
    private String uuid;
    private String userEmail;
    private int commodityId;
    private String text;
    private String date;
    private Map<String, Integer> votes;

    public Comment(
        String uuid,
        String userEmail,
        int commodityId,
        String text,
        String date,
        List<String> upVotes,
        List<String> downVotes
    ) {
        this.uuid = uuid;
        this.userEmail = userEmail;
        this.commodityId = commodityId;
        this.text = text;
        this.date = date;
        this.votes = new HashMap<>();

        for (String username: upVotes)
            this.votes.put(username, 1);
        for (String username: downVotes)
            this.votes.put(username, -1);
    }

    public Comment(
        String userEmail,
        int commodityId,
        String text,
        String date
    ) {
        this(
            UUID.randomUUID().toString(),
            userEmail,
            commodityId,
            text,
            date,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }
    public String getUuid() {
        return uuid;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public List<String> getUpVotes() {
        List<String> res = new ArrayList<>();

        for (String username: this.votes.keySet())
            if (this.votes.get(username) == 1)
                res.add(username);

        return res;
    }

    public List<String> getDownVotes() {
        List<String> res = new ArrayList<>();

        for (String username: this.votes.keySet())
            if (this.votes.get(username) == -1)
                res.add(username);

        return res;
    }

    public void upVote(User user) {
        this.votes.put(
            user.getUsername(),
            1
        );
    }

    public void downVote(User user) {
        this.votes.put(
            user.getUsername(),
            -1
        );
    }

    @Override
    public Map<String, Object> getKey() {
        return Map.of(
            "uuid", this.uuid
        );
    }

    @Override
    public Map<String, Object> describe() {
        return Map.of(
            "uuid", this.uuid,
            "userEmail", this.userEmail,
            "commodityId", this.commodityId,
            "text", this.text,
            "date", this.date,
            "upVotes", this.getUpVotes().size(),
            "downVotes", this.getDownVotes().size()
        );
    }
}
