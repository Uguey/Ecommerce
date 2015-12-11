<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template>
	<jsp:attribute name="styles">
	</jsp:attribute>
	<jsp:attribute name="scripts">	
		<script src="js/validator.js"></script>	
		<c:if test="${not empty badCredentials}">		
			<script type="text/javascript">
				$(document).ready(function(){
				  alert(${badCredentials});
				});
			</script>
		</c:if>
	</jsp:attribute>
	<jsp:body>
		<div class="container-fluid">
			<div class="row clearfix">
				<div
					class="col-xs-12 col-sm-offset-1 col-sm-10 col-md-offset-3 col-md-6 col-lg-offset-4 col-lg-4">
					<h3>User authentication</h3>
					<p>
						The site you are trying to access requires user authentication. Please log in using your credentials:
					</p>
					<form role="form" action="login" method="post">
						<div class="form-group">
							<label for="email">Email</label><input type="email"
								onInvalid="emailError(this);" onInput="emailError(this);"
								required="required" class="form-control" id="email" name="email" />
						</div>
						<div class="form-group">
							<label for="password">Password</label><input type="password"
								class="form-control" id="password" name="password"
								onInvalid="standardError(this);" required="required"
								onInput="standardError(this);" />
						</div>
						<button type="submit" class="btn btn-primary">Login</button>
					</form>
				</div>
			</div>
		</div>
	</jsp:body>
</t:template>
