<%@ page import="java.util.Map" %>
<%@ page import="com.baloot.model.User" %>
<html>
<head>
<title>Baloots</title>
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="../fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="../vendor/animate/animate.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="../vendor/select2/select2.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="../vendor/perfect-scrollbar/perfect-scrollbar.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="../css/util.css">
<link rel="stylesheet" type="text/css" href="../css/main.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="../css/styles.css" media="all" />
<link rel="stylesheet" type="text/css" href="../css/demo.css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>


</head>

<%
	User user = (User) request.getAttribute("user");
%>

<script>
	function fetchAPI(
			url,
			body,
			method,
	) {
		fetch(url, {
			method: method,
			body: JSON.stringify(body),
			headers: {
				"Content-Type": "application/json",
			},
		})
			.then(function(response) {
				// When the page is loaded convert it to text
				return response.text()
			})
			.then(function(html) {
				const parser = new DOMParser();
				const doc = parser.parseFromString(html, "text/html");
				document.body.innerHTML = "";
				window.document.write(html)
			})
			.catch(function(err) {
				console.log('Failed to fetch page: ', err);
			});
	}

	function post(
			url,
			body
	) {
		fetchAPI(
			url,
			body,
			"post"
		)
	}

	function delAPI(
			url,
			body
	) {
		fetchAPI(
				url,
				body,
				"delete"
		)
	}

	async function postRestAPI(
			url,
			body
	) {
		const response = await fetch(url, {
			method: 'post',
			body: JSON.stringify(body),
			headers: {
				"Content-Type": "application/json",
			},
		})

	}
</script>

<body>
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="/index">Baloot</a>
			</div>
			<ul class="nav navbar-nav">
				<li class="active"><a href="/index">Home</a></li>
				<li><a href="/commodities">Commodities</a></li>
				<li><a href="/buyList">Profile</a></li>
				<li><a href="/credit">Credit</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<%
					if (user != null) {
				%>
					<li><a href="#">Credit: <%=user.getCredit()%></a></li>
					<li><a href="#"><span class="glyphicon glyphicon-user"></span>
						Username: <%=user.getUsername()%></a></li>
					<li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span>
						Logout</a></li>
				<%
					}
					else {
				%>
					<li><a href="/login"><span class="glyphicon glyphicon-log-in"></span>
						Login</a></li>
				<%
					}
				%>
			</ul>
		</div>
	</nav>
	</br>
	<div style="padding: 2%">
