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


public class RateServlet extends BalootServlet {
	private final CommodityController commodityController;

	@Override
	protected Boolean needAuthorization() {
		return true;
	}

	public RateServlet(
			AuthorizationMiddleware authorizationMiddleware,
			CommodityController commodityController
	) {
		super(authorizationMiddleware);
		this.commodityController = commodityController;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject data = (JSONObject) request.getAttribute("data");
		data.put("username", ((User) request.getAttribute("user")).getUsername());

		this.commodityController.rateCommodity(data);
		request.getRequestDispatcher("/200.jsp").forward(request, response);
	}
}
