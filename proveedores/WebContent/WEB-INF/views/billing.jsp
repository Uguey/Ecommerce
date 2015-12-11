<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<t:template>
	<jsp:attribute name="styles">	
	</jsp:attribute>
	<jsp:attribute name="scripts">
	</jsp:attribute>
	<jsp:body>          
		<div class="container">
			<div class="row">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 main">
					<h1 class="page-header">Billing</h1>		
					<div class="table-responsive">
						<table class="table table-hover">
							<thead>
								<tr>
									<th style="width: 30px">#</th>
									<th>Product</th>
									<th>Selling price/unit</th>
									<th>Your margin/unit</th>
									<th>Units</th>
									<th>Total margin</th>
									<th>Purchase date</th>
									<th>Reconciled</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${purchaseItems}" var="purchaseItem">
								<tr>
									<td>
										${purchaseItem.id}
									</td>
									<td>
										${purchaseItem.productName}
									</td>
									<td>
										$ ${purchaseItem.productPrice}
									</td>
									<td>
										$ ${purchaseItem.productPrice - purchaseItem.margin}
									</td>
									<td>
										&times; ${purchaseItem.quantity}
									</td>
									<td>
										= ${purchaseItem.supplierRevenue}
									</td>
									<td>
										<fmt:formatDate value="${purchaseItem.purchas.purchaseDate}"
												pattern="dd/MM/yyyy HH:mm" />
									</td>
									<td>
										${purchaseItem.reconciled}
									</td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="table-responsive">
						<h3>Revenue per month</h3>
						<table class="table table-hover">
							<thead>
								<tr>
									<th>Revenue</th>
									<th>Year</th>
									<th>Month</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${sales}" var="sale">
								<tr>
									<td>
										$ ${sale.supplierSales}
									</td>
									<td>
										${sale.year}
									</td>
									<td>
										${sale.month}
									</td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</t:template>

