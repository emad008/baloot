package com.baloot.servlets;

import com.baloot.controller.CommodityController;
import com.baloot.middleware.AuthorizationMiddleware;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CommodityServlet extends BalootServlet {
	private final CommodityController commodityController;
	public CommodityServlet(
			AuthorizationMiddleware authorizationMiddleware,
			CommodityController commodityController
	) {
		super(authorizationMiddleware);
		this.commodityController = commodityController;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer commodityId = Integer.parseInt(request.getPathInfo().replace("/", ""));
		JSONObject commodityData = this.commodityController.getCommodityById(new JSONObject(Map.of("id", commodityId)));
		request.setAttribute("commodity", commodityData);
		request.getRequestDispatcher("/WEB-INF/commodity.jsp").forward(request, response);
	}
}
