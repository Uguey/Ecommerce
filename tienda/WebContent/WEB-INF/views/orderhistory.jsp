<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



<t:template>
	<jsp:attribute name="styles">
		<style type="text/css">
.center {
	text-align: center;
	font-style: oblique;
}

td.center-aligned {
	text-align: center;
}

.entry input {
	border: none;
	background: transparent;
}

.action-icon {
	cursor: pointer;
	color: #FF7B7B;
	visibility: hidden;
}

.entry:hover .action-icon {
	visibility: visible;
}
</style>

	</jsp:attribute>
	<jsp:attribute name="scripts">
	</jsp:attribute>
	<jsp:body>
		<div class="container">
			<div class="row">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 main">
					<h2 class="page-header">Order history from ${client.email}</h2>
					
					<div class="row">
					<ul class="nav nav-pills nav-stacked" role="pilllist">
						<li>
							<a href="profile">Go back to your profile</a>
						</li>
					</ul>
					</div>

					<div class="row">
						<div class="col-md-2">
							<div class="dropdown">
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
						    	Order by...
						    	<span class="caret"></span>
						  		</button>
								<ul class="dropdown-menu" role="menu"
									aria-labelledby="dropdownMenu1">
								  <li role="presentation"><a role="menuitem" tabindex="-1"
										href="orderhistory?orderby=dateAsc">Date (Oldest first)</a></li>
								  <li role="presentation"><a role="menuitem" tabindex="-1"
										href="orderhistory?orderby=dateDesc">Date (Most recent first)</a></li>
								</ul>
							</div>	
						</div>		
					</div>
					<!-- pill panes -->
					<div class="tab-content">
						<div class="tab-pane fade in active" id="clients">
							<div class="table-responsive">
								<table class="table table-striped table-hover">
								<thead>
									<tr>
										<th style="width: 30px">#</th>
										<th>Delivery Info</th>
										<th>Date</th>
										<th style="width: 40px">Order Details</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${purchases}" var="purchase">
									    <tr class="entry product-entry" data-type="purchase">
											<td class="id-holder">
												${purchase.id}
											</td>
											<td>
												<c:set var="fullAddress"
														value="${fn:replace(purchase.deliveryFulladdress, '%', ', ')}" />
												${fullAddress}
											</td>
											<td>
												<fmt:formatDate value="${purchase.purchaseDate}"
														pattern="MM/dd/yyyy HH:mm" />
											</td>
											<td>
												<a href="orderhistorydetails?pid=${purchase.id}">Access</a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</t:template>

