<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="com.baloot.model.User" %>
<jsp:include page="header.jsp" />
<%
    JSONObject result = (JSONObject) request.getAttribute("buyList");
	JSONArray buyList = result.getJSONArray("items");
    User user = (User) request.getAttribute("user");
%>
<ul>
    <li id="username">Username: <%=user.getUsername()%></li>
    <li id="email">Email: <%=user.getEmail()%></li>
    <li id="birthDate">Birth Date: <%=user.getBirthDate()%></li>
    <li id="address"><%=user.getAddress()%></li>
    <li id="credit">Credit: <%=user.getCredit()%></li>
    <li>Current Buy List Price: <%=result.getInt("creditSum")%></li>
    <li>Discount: <%=result.getInt("discount")%>%</li>
    <li>Final: <%=result.getInt("final")%></li>
    <li>
        <a href="/credit">Add Credit</a>
    </li>
    <li>
        <div>
            <label>Submit & Pay</label>
            <input id="form_payment" type="hidden" name="userId" value="Farhad">
            <button type="submit" id="payment">Payment</button>
        </div>
        <script>
            document.getElementById("payment").addEventListener("click", payment);

            async function payment() {
                post(
                    '/payment',
                    {}
                )
            }
        </script>
    </li>
</ul>
<table>
    <caption>
        <h2>Buy List</h2>
    </caption>
    <tbody><tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th></th>
        <th></th>
    </tr>
    <%
        for (int i = 0; i < buyList.length(); i++) {
		    JSONObject commodity = buyList.getJSONObject(i);
    %>
        <tr>
            <td><%=commodity.getInt("id")%></td>
            <td><%=commodity.getString("name")%></td>
            <td>Phone Provider No.<%=commodity.getInt("providerId")%></td>
            <td><%=commodity.getInt("price")%></td>
            <td>
                <%
                    for (int j = 0; j < commodity.getJSONArray("categories").length(); j++) {
                        String category = commodity.getJSONArray("categories").getString(j);
                %>
                <%= category %>,
                <%
                    }
                %>
            </td>
            <td><%=commodity.getFloat("rating")%></td>
            <td><%=commodity.getInt("inStock")%></td>
            <td><a href="/commodities/<%=commodity.getInt("id")%>">Link</a></td>
            <td>
                <div>
                    <button type="submit" id="remove_<%=commodity.getInt("id")%>">Remove</button>
                </div>
                <script>
                    document.getElementById("remove_<%=commodity.getInt("id")%>").addEventListener("click", remove);

                    async function remove() {
                        post(
                            '/buyList/remove/<%=commodity.getInt("id")%>',
                            {}
                        )
                    }
                </script>
            </td>
        </tr>
    <%
        }
    %>
    </tbody>
</table>

<div>
    <h2>Discount</h2>

    <div>
        <input name="discountCode" id="code" value="">
        <button type="submit" id="discount">Activate Discount</button>
    </div>

    <script>
        document.getElementById("discount").addEventListener("click", discount);

        async function discount() {
            let code = document.getElementById("code").value
            post(
                '/discount',
                {
                    "code": code
                }
            )
        }
    </script>
</div>
<jsp:include page="footer.jsp" />