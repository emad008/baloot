package com.baloot.controller;

import com.baloot.model.Comment;
import com.baloot.model.User;
import com.baloot.repository.CommentRepository;
import com.baloot.repository.UserRepository;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public JSONObject comment(JSONObject commentData) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String currentDateStr = formatter.format(date);

        this.commentRepository.insert(
            new Comment(
                    commentData.getString("username"),
                    commentData.getInt("commodityId"),
                    commentData.getString("text"),
                    currentDateStr
            )
        );

        return new JSONObject();
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
