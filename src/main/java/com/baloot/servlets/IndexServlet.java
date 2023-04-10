package com.baloot.servlets;

import com.baloot.middleware.AuthorizationMiddleware;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class IndexServlet extends BalootServlet {
	public IndexServlet(AuthorizationMiddleware authorizationMiddleware) {
		super(authorizationMiddleware);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
	}
}
