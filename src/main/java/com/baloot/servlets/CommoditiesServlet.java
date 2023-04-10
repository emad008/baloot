package com.baloot.servlets;

import com.baloot.controller.CommodityController;
import com.baloot.middleware.AuthorizationMiddleware;
import org.json.JSONObject;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CommoditiesServlet extends BalootServlet {
	private final CommodityController commodityController;

	public CommoditiesServlet(
			AuthorizationMiddleware authorizationMiddleware,
			CommodityController commodityController
	) {
		super(authorizationMiddleware);
		this.commodityController = commodityController;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setAttribute("commodities", this.commodityController.getCommoditiesList(new JSONObject()).getJSONArray("commoditiesList"));
		request.getRequestDispatcher("/WEB-INF/commodities.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject data = (JSONObject) request.getAttribute("data");
		request.setAttribute("commodities", this.commodityController.getCommoditiesList(data).getJSONArray("commoditiesList"));
		request.getRequestDispatcher("/WEB-INF/commodities.jsp").forward(request, response);
	}
}
