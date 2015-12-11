<%@tag description="Template tag" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="styles" fragment="true"%>
<%@attribute name="scripts" fragment="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title>Supplier console - Telaveramica</title>
<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Custom CSS -->
<link href="css/shop-homepage.css" rel="stylesheet">
<link href="css/logo-nav.css" rel="stylesheet">
<jsp:invoke fragment="styles" />
</head>
<body>
	<!-- Header -->
	<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="."> <img src="images/logo.png"
					alt="Logo" style="width: 150px; height: 50px">
				</a>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a href=".">My catalog</a></li>
					<li><a href="subscribedproducts">Client subscriptions</a></li>
					<li><a href="margin">Billing</a></li>
				</ul>
				<ul class="nav navbar-nav pull-right">
					<c:choose>
						<c:when test="${not empty email}">
							<p class="navbar-text">
								<a href="profile">My account</a>
							</p>
							<form class="pull-right form" action="logout" method="post">
								<button type="submit" id="btnLogout"
									class="btn btn-default navbar-btn">Logout</button>
							</form>
						</c:when>
						<c:otherwise>
							<c:if test="${not empty badCredentials}">
								<script type="text/javascript">
										alert("${badCredentials}");
										</script>
							</c:if>
							<li class="dropdown" id="menuLogin"><a
								class="dropdown-toggle" href="#" data-toggle="dropdown"
								id="navLogin">Login<span class="caret"></span></a>
								<div class="dropdown-menu" style="padding: 17px;">
									<form class="form" id="formLogin" action="login" method="post">
										<input name="email" id="email" placeholder="Email"
											type="email"> <input name="password" id="password"
											placeholder="Password" type="password"><br>
										<button type="submit" id="btnLogin" class="btn">Login</button>
									</form>
								</div></li>
							<li><a href="register">Register</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>
	</nav>
	<!-- ./Header -->
	<!-- Body -->
	<jsp:doBody />
	<!-- ./Body -->
	<!-- Footer -->
	<div class="container">
		<hr>
		<footer>
			<div class="row">
				<div class="col-lg-12">
					<p class="muted-credit pull-right">
						<a href="#">About us </a> | <a href="#"> Privacy </a> | <a
							href="#"> Contact </a> | <a href="#"> Legal agreement </a>
					</p>
					<p>Copyright &copy; Telaveramica Holdings Inc.</p>
				</div>
			</div>
		</footer>
	</div>
	<!-- /.Footer -->

	<!-- jQuery Version 1.11.0 -->
	<script src="js/jquery-1.11.0.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>

	<!-- Custom scripts -->
	<jsp:invoke fragment="scripts" />
</body>
</html>