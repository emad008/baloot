package com.baloot.servlets;

import com.baloot.controller.CommodityController;
import com.baloot.controller.DiscountController;
import com.baloot.controller.UserController;
import com.baloot.middleware.AuthorizationMiddleware;
import com.baloot.model.User;
import com.baloot.repository.UserRepository;
import org.json.JSONObject;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DiscountServlet extends BalootServlet {
	private final DiscountController discountController;

	@Override
	protected Boolean needAuthorization() {
		return true;
	}

	public DiscountServlet(
			AuthorizationMiddleware authorizationMiddleware,
			DiscountController discountController
	) {
		super(authorizationMiddleware);
		this.discountController = discountController;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject data = (JSONObject) request.getAttribute("data");
		String username = ((User) request.getAttribute("user")).getUsername();
		data.put("username", username);
		this.discountController.useDiscount(data);
		request.getRequestDispatcher("/200.jsp").forward(request, response);
	}
}
