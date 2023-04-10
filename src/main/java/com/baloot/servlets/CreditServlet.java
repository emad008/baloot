package com.baloot.servlets;

import com.baloot.controller.CommodityController;
import com.baloot.controller.UserController;
import com.baloot.middleware.AuthorizationMiddleware;
import com.baloot.model.User;
import com.baloot.repository.UserRepository;
import org.json.JSONObject;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CreditServlet extends BalootServlet {
	private final UserController userController;
	private final UserRepository userRepository;

	@Override
	protected Boolean needAuthorization() {
		return true;
	}

	public CreditServlet(
			AuthorizationMiddleware authorizationMiddleware,
			UserController userController,
			UserRepository userRepository
	) {
		super(authorizationMiddleware);
		this.userController = userController;
		this.userRepository = userRepository;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/credit.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject data = (JSONObject) request.getAttribute("data");
		String username = ((User) request.getAttribute("user")).getUsername();
		data.put("username", username);
		this.userController.addCredit(data);
		request.getRequestDispatcher("/200.jsp").forward(request, response);
	}
}
