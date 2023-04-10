package com.baloot.servlets;

import com.baloot.controller.BuyListController;
import com.baloot.middleware.AuthorizationMiddleware;
import com.baloot.model.User;
import org.json.JSONObject;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BuyListServlet extends BalootServlet {
	private final BuyListController buyListController;

	@Override
	protected Boolean needAuthorization() {
		return true;
	}

	public BuyListServlet(
			AuthorizationMiddleware authorizationMiddleware,
			BuyListController buyListController
	) {
		super(authorizationMiddleware);
		this.buyListController = buyListController;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		JSONObject data = new JSONObject();
		data.put("username", ((User) request.getAttribute("user")).getUsername());

		request.setAttribute("buyList", this.buyListController.getBuyList(data));
		request.getRequestDispatcher("/WEB-INF/buyList.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		JSONObject data = (JSONObject) request.getAttribute("data");
		data.put("username", ((User) request.getAttribute("user")).getUsername());

		this.buyListController.addToBuyList(data);
		request.getRequestDispatcher("/200.jsp").forward(request, response);
	}
}
