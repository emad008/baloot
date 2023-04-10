<%@ page import="org.json.JSONObject" %>
<%@ page import="com.baloot.model.Commodity" %>
<jsp:include page="header.jsp" />
<%
    JSONObject commodity = (JSONObject) request.getAttribute("commodity");
%>
<ul>
    <li id="id">Id: <%=commodity.getInt("id")%></li>
    <li id="name">Name: <%=commodity.getString("name")%></li>
    <li id="providerName">Provider Name: <%=commodity.getJSONObject("provider").getString("name")%></li>
    <li id="price">Price: <%=commodity.getInt("price")%></li>
    <li id="categories">
        Categories:
        <%
            for (int i = 0; i < commodity.getJSONArray("categories").length(); i++) {
				String category = commodity.getJSONArray("categories").getString(i);
        %>
        <%=
            category
        %>,
        <%
            }
        %>
    </li>
    <li id="rating">Rating: <%=commodity.getFloat("rating")%></li>
    <li id="inStock">In Stock: <%=commodity.getInt("inStock")%>
</ul>

<label>Add Your Comment:</label>
<div>
    <input type="text" id="commentText" name="comment" value="" />
    <button type="submit" id="comment">submit</button>
</div>
<script>
    document.getElementById("comment").addEventListener("click", comment);

    async function comment() {
        let text = document.getElementById("commentText").value;
        post(
            '/comments',
            {
                'commodityId': <%=commodity.getInt("id")%>,
                'text': text
            }
        )
    }
</script>
<br>
<div action="" method="POST">
    <label>Rate(between 1 and 10):</label>
    <input type="number" id="quantity" name="quantity" min="1" max="10">
    <button type="submit" id="rate">Rate</button>
</div>
<script>
    document.getElementById("rate").addEventListener("click", rate);

    async function rate() {
        let rating = document.getElementById("quantity").value;
        post(
            '/rate',
            {
                'commodityId': <%=commodity.getInt("id")%>,
                'score': rating
            }
        )
    }
</script>

<br>
<div action="" method="POST">
    <button type="submit" id="addToBuyList">Add to BuyList</button>
</div>

<script>
    document.getElementById("addToBuyList").addEventListener("click", addToBuyList);

    async function addToBuyList() {
        post(
            '/buyList',
            {
                'commodityId': <%=commodity.getInt("id")%>,
            }
        )
    }
</script>
<br />
<table>
    <caption><h2>Comments</h2></caption>
    <tr>
        <th>username</th>
        <th>comment</th>
        <th>date</th>
        <th>likes</th>
        <th>dislikes</th>
    </tr>
    <%
        for (int i = 0; i < commodity.getJSONArray("comments").length(); i++) {
			JSONObject comment = commodity.getJSONArray("comments").getJSONObject(i);
    %>
        <tr>
            <td><%=comment.getJSONObject("user").getString("username")%></td>
            <td><%=comment.getString("text")%></td>
            <td><%=comment.getString("date")%></td>
            <td>
                <div action="" method="POST">
                    <label for=""><%=comment.getInt("upVotes")%></label>
                    <input
                            id="form_comment_id"
                            type="hidden"
                            name="comment_id"
                            value="1"
                    />
                    <button type="submit" id="upVote">like</button>
                </div>
                <script>
                    document.getElementById("upVote").addEventListener("click", upVote);

                    async function upVote() {
                        post(
                            '/vote-comment/<%=comment.getString("uuid")%>',
                            {
                                'vote': 1,
                            }
                        )
                    }
                </script>
            </td>
            <td>
                <div action="" method="POST">
                    <label for=""><%=comment.getInt("downVotes")%></label>
                    <input
                            id="form_comment_id"
                            type="hidden"
                            name="comment_id"
                            value="-1"
                    />
                    <button type="submit" id="downVote">dislike</button>
                </div>
                <script>
                    document.getElementById("downVote").addEventListener("click", downVote);

                    async function downVote() {
                        post(
                            '/vote-comment/<%=comment.getString("uuid")%>',
                            {
                                'vote': -1,
                            }
                        )
                    }
                </script>
            </td>
        </tr>
    <%
        }
    %>
</table>
<br><br>
<table>
    <caption><h2>Suggested Commodities</h2></caption>
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
        for (int i = 0; i < commodity.getJSONArray("suggestedCommodities").length(); i++) {
            JSONObject suggestedCommodity = commodity.getJSONArray("suggestedCommodities").getJSONObject(i);
    %>
        <tr>
            <td><%=suggestedCommodity.getInt("id")%></td>
            <td><%=suggestedCommodity.getString("name")%></td>
            <td>Phone Provider No.<%=suggestedCommodity.getInt("providerId")%></td>
            <td><%=suggestedCommodity.getInt("price")%></td>
            <td>
                <%
                    for (int j = 0; j < suggestedCommodity.getJSONArray("categories").length(); j++) {
						String category = suggestedCommodity.getJSONArray("categories").getString(j);
                %>
                <%= category %>,
                <%
                    }
                %>
            </td>
            <td><%=suggestedCommodity.getFloat("rating")%></td>
            <td><%=suggestedCommodity.getInt("inStock")%></td>
            <td><a href="/commodities/<%=suggestedCommodity.getInt("id")%>">Link</a></td>
        </tr>
    <%
        }
    %>
</table>
<jsp:include page="footer.jsp" />