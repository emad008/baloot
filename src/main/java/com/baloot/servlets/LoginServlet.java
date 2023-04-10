package com.baloot.servlets;

import com.baloot.middleware.AuthorizationMiddleware;
import org.json.JSONObject;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginServlet extends BalootServlet {
	public LoginServlet(AuthorizationMiddleware authorizationMiddleware) {
		super(authorizationMiddleware);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject data = (JSONObject) request.getAttribute("data");
		this.authorizationMiddleware.setLastAuthorizedUser(data.getString("username"));
		this.authorizationMiddleware.authorize(request);
		request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
	}
}
