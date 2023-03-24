package baloot.controller;

import baloot.model.Comment;
import baloot.model.User;
import baloot.repository.CommentRepository;
import baloot.repository.UserRepository;
import org.json.JSONObject;

public class CommentController {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentController(
        CommentRepository commentRepository,
        UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public JSONObject voteComment(JSONObject voteCommentData) {
        User user = this.userRepository.findByUsername(voteCommentData.getString("username"));
        Comment comment = this.commentRepository.findById(voteCommentData.getString("commentId"));

        if (voteCommentData.getInt("vote") == 1)
            comment.upVote(user);
        else if (voteCommentData.getInt("vote") == -1)
            comment.downVote(user);

        this.commentRepository.save(comment);
        return new JSONObject(this.commentRepository.findById(comment.getUuid()).describe());
    }
}
