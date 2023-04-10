package com.baloot.servlets;

import com.baloot.controller.BuyListController;
import com.baloot.middleware.AuthorizationMiddleware;
import com.baloot.model.User;
import org.json.JSONObject;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BuyListItemRemoveServlet extends BalootServlet {
	private final BuyListController buyListController;

	@Override
	protected Boolean needAuthorization() {
		return true;
	}

	public BuyListItemRemoveServlet(
			AuthorizationMiddleware authorizationMiddleware,
			BuyListController buyListController
	) {
		super(authorizationMiddleware);
		this.buyListController = buyListController;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer commodityId = Integer.parseInt(request.getPathInfo().replace("/", ""));
		JSONObject data = new JSONObject();
		data.put("username", ((User) request.getAttribute("user")).getUsername());
		data.put("commodityId", commodityId);

		this.buyListController.removeFromBuyList(data);
		request.getRequestDispatcher("/200.jsp").forward(request, response);
	}
}
