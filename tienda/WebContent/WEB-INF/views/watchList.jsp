<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
					<h1 class="page-header">Watch list from ${email}</h1>
					
					<div class="row">
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
										<th>Product name</th>
										<th>Supplier</th>
										<th style="width: 40px">Go to product</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${subscriptions}" var="subscription">
									    <tr class="entry">
											<td class="center-aligned id-holder">
												${subscription.id}
											</td>
											<td class="center-aligned">
												${subscription.product.name}
											</td>
											<td class="center-aligned">
												${subscription.product.supplier.companyName}
											</td>
											<td class="center-aligned">
												<a href="product?id=${subscription.product.id}">View product</a>
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

