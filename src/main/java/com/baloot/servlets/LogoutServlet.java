package com.baloot.servlets;

import com.baloot.middleware.AuthorizationMiddleware;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LogoutServlet extends BalootServlet {
	public LogoutServlet(AuthorizationMiddleware authorizationMiddleware) {
		super(authorizationMiddleware);
	}

	@Override
	protected Boolean needAuthorization() {
		return true;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.authorizationMiddleware.removeAuthorizedUser();
		request.removeAttribute("user");
		request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
	}
}
