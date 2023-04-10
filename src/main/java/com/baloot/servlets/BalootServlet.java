package com.baloot.servlets;

import com.baloot.exception.BalootException;
import com.baloot.exception.Unauthorized;
import com.baloot.middleware.AuthorizationMiddleware;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

public class BalootServlet extends HttpServlet {
	protected AuthorizationMiddleware authorizationMiddleware;

	BalootServlet(AuthorizationMiddleware authorizationMiddleware) {
		this.authorizationMiddleware = authorizationMiddleware;
	}

	protected Boolean needAuthorization() {
		return false;
	}

	protected void callServlets(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Authorize
		this.authorizationMiddleware.authorize(req);

		if (this.needAuthorization() && req.getAttribute("user") == null)
			throw new Unauthorized();

		// Get Request Data
		if (req.getMethod().equals("POST")) {
			// We will assume that data is given as json object or form. We will handle the other content types later

			if (req.getHeader("Content-Type").equals("application/x-www-form-urlencoded")) {
				JSONObject data = new JSONObject();
				for (String parameter: Collections.list(req.getParameterNames()))
					data.put(parameter, req.getParameter(parameter));

				req.setAttribute("data", data);
			}

			if (req.getHeader("Content-Type").equals("application/json"))
				req.setAttribute("data", new JSONObject(req.getReader().lines().collect(Collectors.joining())));
		}

		super.service(req, resp);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			this.callServlets(req, resp);
		}
		catch (Unauthorized ex) {
			resp.setStatus(401);
			req.getRequestDispatcher("/401.jsp").forward(req, resp);
		}
		catch (BalootException ex) {
			resp.setStatus(400);
			req.setAttribute("errorMsg", ex.getMessage());
			req.getRequestDispatcher("/400.jsp").forward(req, resp);
		}
		catch (Exception ex) {
			resp.setStatus(500);
			req.setAttribute("errorMsg", ex.getMessage());
			req.getRequestDispatcher("/500.jsp").forward(req, resp);
			throw ex;
		}
	}
}
