<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<t:template>
	<jsp:attribute name="styles">
		<link
			href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
			rel="stylesheet">
		<style type="text/css">
.prod-row td {
	padding: 15px 5px !important;
}

tfoot td {
	padding-top: 30px !important;
}

.product-name {
	font-weight: bold;
	font-size: 1.2em;
}

.inline-action-form {
	display: inline-block;
	float: left;
}

.remove-from-cart {
	margin-left: 10px;
}
</style>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/jquery.form.min.js"></script>
		<script src="js/shoppingCart.js"></script> 	
	</jsp:attribute>
	<jsp:body>
	<div class="container">
	<c:choose>
		<c:when test="${fn:length(purchaseItems) eq 0}">
			<div class="alert alert-danger" role="alert">
			  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
			  <span class="sr-only">Error:</span>
			  <strong>Your shopping cart is empty!</strong> click on "continue shopping" below and add some products to it. 
			</div>
			<a href="." class="btn btn-warning"><i class="fa fa-angle-left"></i> Continue Shopping</a>
		</c:when>
		<c:otherwise>
		<table id="cart" class="table table-hover table-condensed table-striped">
	   		<thead>
				<tr>
					<th style="width: 50%">Product</th>
					<th style="width: 10%">Price</th>
					<th style="width: 10%">Quantity</th>
					<th style="width: 10%">Subtotal</th>
					<th style="width: 20%"></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${purchaseItems}" var="purchaseItem">					
				<tr class="prod-row">
					<td data-th="Product">
						<div class="row">
							<div class="col-sm-2 hidden-xs">
								<img src="${purchaseItem.product.image}" alt="..."
									class="img-responsive" />
							</div>
							<div class="col-sm-10">
								<p class="product-name">${purchaseItem.product.name}</p>
								<p>${purchaseItem.product.description}</p>
							</div>
						</div>
					</td>
					<td data-th="Price">
						<p class="price">${purchaseItem.product.retailPrice}</p>
					</td>
					<td data-th="Quantity">
						<p class="quantity">${purchaseItem.quantity}</p>
					</td>
					<td data-th="Subtotal">
						<p class="subtotal"></p>					
         				</td>										
					<td class="actions" data-th="">
					  <form action="shoppingcart" method="post"
						class="inline-action-form decrement-unit">
                            		<input type="hidden" class="id"
							name="id" value="${purchaseItem.product.id}">
						<input type="hidden" class="action" name="action"
							value="decrement">
                             	<button class="btn btn-default btn-sm">
							<i class="glyphicon glyphicon-minus"></i>
						</button>
                           </form>
                           <form action="shoppingcart"
						method="post" class="inline-action-form increment-unit">
                            		<input type="hidden" class="id"
							name="id" value="${purchaseItem.product.id}">
						<input type="hidden" class="action" name="action"
							value="increment">
                             	<button class="btn btn-default btn-sm">
							<i class="glyphicon glyphicon-plus"></i>
						</button>
                           </form>
                           <form action="shoppingcart" method="post" class="inline-action-form remove-from-cart">
                          	<input type="hidden" class="id" name="id" value="${purchaseItem.product.id}">
						<input type="hidden" class="action" name="action"
						value="delete">
                            	<button class="btn btn-danger btn-sm">
						<i class="fa fa-trash-o"></i>
						</button>
                      </form>
					</td>
				</tr>				
			</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td>
						<a href="." class="btn btn-warning"><i
							class="fa fa-angle-left"></i> Continue Shopping</a>
					</td>
					<td colspan="2" class="hidden-xs"></td>
					<td class="hidden-xs text-center">
						<strong>Total $ <span id="total"></span></strong>
					</td>
					<td><a href="payment" class="btn btn-success btn-block">Checkout <i
							class="fa fa-angle-right"></i></a>
					</td>
				</tr>
			</tfoot>
		</table>			
		</c:otherwise>
	</c:choose>
	</div>					
	</jsp:body>
</t:template>