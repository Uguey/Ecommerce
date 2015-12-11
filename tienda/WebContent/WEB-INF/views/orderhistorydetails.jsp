<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


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

#left {
	margin-right: 80%;
}

#right {
	margin-left: 80%;
}
</style>

	</jsp:attribute>
	<jsp:attribute name="scripts">
	</jsp:attribute>
	<jsp:body>
		<div class="container">
			<div class="row">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 main">
					<h2 class="page-header">Order history Details</h2>
					
					<!-- Nav pills -->
					<div id="left">
					<ul class="nav nav-pills nav-stacked" role="pilllist">
						<li>
							<a href="profile">Go back to your profile</a>
						</li>
					</ul>
					</div>					
					<!-- pill panes -->
					<div class="tab-content">
						<div class="tab-pane fade in active" id="clients">
							<div class="table-responsive">
								<table class="table table-striped table-hover">
								<thead>
									<tr>
										<th style="width: 30px">#</th>
										<th>Product Name</th>
										<th>Company</th>		
										<th>Price</th>
										<th>Quantity</th>
										
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${purchaseItems}" var="pi">
										<c:set var="total"
												value="${total + (pi.productPrice * pi.quantity) }"></c:set>
									    <tr class="entry">
											<td class="id-holder">
												${pi.id}
											</td>
											<td>
												${pi.productName}
											</td>												
											<td>
												${pi.supplierCompany}
											</td>
											<td>
												${pi.productPrice} $/unit
											</td>
											<td>
												${pi.quantity}
											</td>
										</tr>
									</c:forEach>
								</tbody>
								</table>
								<h4 id="right">Total price: ${total} $</h4>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</t:template>