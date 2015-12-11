<%@tag
	description="Displays the shopping cart if the user is logged in."
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.shopping-cart-container {
	margin-right: 16px;
	padding-bottom: 0 !important;
}

.shopping-cart-badge {
	display: block;
	position: relative;
	top: -10px;
	left: 6px;
	width: 14px;
	height: 14px;
	border-radius: 50%;
	background: #FF5530;
	color: white;
	font-size: 9px;
	font-weight: bold;
	line-height: 14px;
	text-align: center;
}

.shopping-cart-badge:empty {
	display: none;
}
</style>
<a href="shoppingcart" class="shopping-cart-container"> <span
	class="glyphicon glyphicon-shopping-cart"></span> <span
	class="shopping-cart-badge"></span>
</a>
<script type="text/javascript">
	$(document).ready(function() {
		var $shoppingCartBadge = $(".shopping-cart-badge");
		
		// Subscribe to updates in the number of items in the cart.
		$(document).on("cartupdate", function (evt) {
			var numItemsInCart = evt.numItems;
			
			var oldNumItemsInCart = $shoppingCartBadge.html();
			
			if (oldNumItemsInCart != numItemsInCart) {			
				// Persist new value in cookies
				$.cookie("num_items_in_cart", "" + numItemsInCart);
				
				// Animate badge
				$shoppingCartBadge.parent()
					.addClass("animated rubberBand")
					.one(
				        'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', 
				        function() {
				        	$(this).removeClass("animated rubberBand");
				       		// Update badge value
				  			$shoppingCartBadge.html(numItemsInCart);
				        });
			}
		});
		
		// Set value to last saved one. This can also be passed by server
		var lastNumItemsInCart = $.cookie("num_items_in_cart", Number);
		if (lastNumItemsInCart) {
			$shoppingCartBadge.html(lastNumItemsInCart);
		}
	});
</script>
