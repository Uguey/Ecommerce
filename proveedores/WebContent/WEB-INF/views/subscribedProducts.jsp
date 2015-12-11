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
					<h1 class="page-header">Clients subscribed to your products</h1>
					
					<div class="row">
					<ul class="nav nav-pills nav-stacked" role="pilllist">
						<li>
							<a href=".">Go back to your console</a>
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
										<th style="width: 200px">Client</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${subscriptions}" var="subscription">
									    <tr class="entry">
											<td class="center-aligned id-holder">
												${subscription.id}
											</td>
											<td>
												${subscription.product.name}
											</td>
											<td>
												${subscription.client.surname}, ${subscription.client.name}
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

