<%@ page import="com.baloot.model.Commodity" %>
<%@ page import="java.util.List" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.json.JSONArray" %><%--
  Created by IntelliJ IDEA.
  User: emad
  Date: 09.04.23
  Time: 08:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="header.jsp" />
<div id="filterForm" action="" method="POST">
    <label>Search:</label>
    <input type="text" name="searchValue" id="searchValue" value="">
    <button type="submit" name="action" id="filterByCategory">Search By Cagtegory</button>
    <button type="submit" name="action" id="filterByName">Search By Name</button>
    <button type="submit" name="action" id="clear">Clear Search</button>
</div>
<script>
    document.getElementById("filterByCategory").addEventListener("click", filterByCategory);
    document.getElementById("clear").addEventListener("click", clearFilters);
    document.getElementById("filterByName").addEventListener("click", filterByName);

    async function filterByCategory() {
        let category = document.getElementById("searchValue").value
        post(
            '/commodities',
            {
                'filters': {
                    "categories__contains": category
                }
            }
        )
    }

    async function filterByName() {
        let name = document.getElementById("searchValue").value
        post(
            '/commodities',
            {
                'filters': {
                    "name__contains": name
                }
            }
        )
    }

    async function clearFilters() {
        post(
            '/commodities',
            {}
        )
    }
</script>
<br><br>
<div>
    <label>Sort By:</label>
    <button type="submit" name="action" id="sortByRate">Rate</button>
</div>
<script>
    document.getElementById("sortByRate").addEventListener("click", sortByRate);

    async function sortByRate() {
        post(
            '/commodities',
            {
                'sort': ["rating"]
            }
        )
    }
</script>
<br><br>
<%
    JSONArray commodities = (JSONArray) request.getAttribute("commodities");
%>
<table>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th>Links</th>
    </tr>
    <%
        for (int i = 0; i < commodities.length(); i++) {
			JSONObject commodity = commodities.getJSONObject(i);
    %>
        <tr>
            <td><%=commodity.getInt("id")%></td>
            <td><%=commodity.getString("name")%></td>
            <td><%=commodity.getJSONObject("provider").getString("name")%></td>
            <td><%=commodity.getInt("price")%></td>
            <td>
                <%
                    for (int j = 0; j < commodity.getJSONArray("categories").length(); j++) {
						String category = commodity.getJSONArray("categories").getString(j);
                %>
                    <%=category%>
                    <%
                        if (j != commodity.getJSONArray("categories").length() - 1) {
                    %>
                        ,
                    <%
                        }
                    %>
                <%
                    }
                %>
            </td>
            <td><%=commodity.getFloat("rating")%></td>
            <td><%=commodity.getInt("inStock")%></td>
            <td><a href="/commodities/<%=commodity.getInt("id")%>">Link</a></td>
        </tr>
    <%
        }
    %>
</table>
<jsp:include page="footer.jsp" />
