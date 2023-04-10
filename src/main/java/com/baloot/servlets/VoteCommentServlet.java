package com.baloot.servlets;

import com.baloot.controller.CommentController;
import com.baloot.controller.CommodityController;
import com.baloot.middleware.AuthorizationMiddleware;
import com.baloot.model.User;
import org.json.JSONObject;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class VoteCommentServlet extends BalootServlet {
	private final CommentController commentController;

	@Override
	protected Boolean needAuthorization() {
		return true;
	}

	public VoteCommentServlet(
			AuthorizationMiddleware authorizationMiddleware,
			CommentController commentController
	) {
		super(authorizationMiddleware);
		this.commentController = commentController;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String commentId = request.getPathInfo().replace("/", "");
		JSONObject data = (JSONObject) request.getAttribute("data");
		data.put("username", ((User) request.getAttribute("user")).getUsername());
		data.put("commentId", commentId);

		this.commentController.voteComment(
				data
		);
		request.getRequestDispatcher("/200.jsp").forward(request, response);
	}
}
