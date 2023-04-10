<%--
  Created by IntelliJ IDEA.
  User: emad
  Date: 09.04.23
  Time: 00:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="header.jsp" />
<div class="container">
    <div>
        <div class="form-group col-md-8">
            <label for="username">Username</label> <input
                type="text" id="username" class="form-control" name="username"
        >
        </div>
        <div class="col-md-12 text-center">
            <button id="loginButton" class="btn btn-primary">Login</button>
        </div>
    </div>
    <script>
        document.getElementById("loginButton").addEventListener("click", login);

        async function login() {
            let username = document.getElementById("username").value

            post(
                '/login',
                {
                    'username': username
                }
            )
        }
    </script>
</div>
<jsp:include page="footer.jsp" />

