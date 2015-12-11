<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template>
	<jsp:attribute name="styles">
		<%-- Here we add custom stylesheets to be loaded in our page only --%>
		<%-- e.g.:
		<link href="css/shop-homepage.css" rel="stylesheet">
		--%>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<%-- Here we add custom scripts to be loaded in our page only --%>
		<%-- e.g.:
		<script src="js/testscript.js"></script>
		--%>
	</jsp:attribute>
	<jsp:body>
		<%-- Here we add the custom content of our page --%>
		<%-- e.g.:
		<div class="container">
			<h1>a robá carterahh</h1>
		</div>
		--%>
	</jsp:body>
</t:template>
