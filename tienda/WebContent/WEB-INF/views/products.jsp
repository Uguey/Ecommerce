<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template>
	<jsp:attribute name="styles">
		<link href="css/ribbon.css" rel="stylesheet">
	
		<style type="text/css">
.space {
	margin-top: 26%;
}

.prod-entry {
	margin-bottom: 25px;
	border: 1px solid #ccc;
	-webkit-transition: all 0.2s ease;
	-moz-transition: all 0.2s ease;
	-o-transition: all 0.2s ease;
	transition: all 0.2s ease;
	z-index: 0;
}

.prod-entry:hover, .prod-entry.flying {
	z-index: 1000;
	box-shadow: rgba(0, 0, 0, 0.3) 0 16px 16px 0;
	-webkit-box-shadow: rgba(0, 0, 0, 0.3) 0 16px 16px 0;
	-moz-box-shadow: rgba(0, 0, 0, 0.3) 0 16px 16px 0;
}

.prod-thumbnail-container {
	overflow: hidden;
	position: relative;
	width: 100%;
	height: 200px;
	display: block;
	margin: 0px auto;
}

.prod-thumbnail-container .prod-title {
	width: 100%;
	height: 30px;
	background-color: rgba(71, 71, 71, 0.58);
	position: absolute;
	top: 170px;
	text-align: center;
	color: white;
}

.prod-thumbnail-container .prod-title h4 {
	margin-top: 5px;
}

.prod-thumbnail-container .prod-price {
	height: 30px;
	text-shadow: 0px 0px 2px #969696;
	position: absolute;
	top: 0px;
	left: 0px;
	text-align: center;
	color: white;
	padding: 0 10px;
}

.prod-thumbnail-container .prod-thumbnail {
	width: 100%;
}

.prod-info-container {
	padding: 10px;
}

.prod-actions .action {
	display: inline-block;
}

.prod-actions .action button {
	font-size: 10px;
}

.glyphicon.subscript {
	font-size: 0.5em;
	position: relative;
	left: -7px;
	top: 3px;
}

.glyphicon-minus-sign.subscript {
	color: rgba(239, 87, 76, 1);
}

.glyphicon-plus-sign.subscript {
	color: rgba(63, 182, 26, 1);
}
</style>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/jquery.form.min.js"></script> 
		<script src="js/jquery.quicksand.js"></script> 
		<script type="text/javascript">
		userLoggedIn = ${not empty email};
		$('form.action').ajaxForm({
		  beforeSubmit: function() {	 	
		    if (!userLoggedIn) {
			    var conf = confirm("Sorry, you can't buy anything unless you are registered. Do you want to register?"); 
	  			if (conf){
	  			  window.location.replace("register");
	  			}
		    }
		    
		    // This prevents submission if the user is not logged in
		    return userLoggedIn;
		  },
		  success: function(response, statusText, xhr, $form) { 			        	
			// Update stock
			var initialStock = parseInt($form.closest('.prod-info-container').find('.prod-stock').data('initial-stock'));
			var oldStock = parseInt($form.closest('.prod-info-container').find('.prod-stock').html());
			var newStock;
			var formAction = $form.find('input[name="action"]').val();
			
			if ('increment' == formAction) {
			  newStock = oldStock > 0 ? oldStock - 1 : 0;
			} else if ('decrement' == formAction) {
			  newStock = oldStock + 1;
			}
			
			$form.closest('.prod-info-container').find('.prod-stock').html(newStock);
			
			// Update action enabled state
			var $incrementBtn = $form.closest('.prod-actions').find('.increment-unit > button');
			var $decrementBtn = $form.closest('.prod-actions').find('.decrement-unit > button');
			$incrementBtn.prop('disabled', newStock <= 0);
			$decrementBtn.prop('disabled', newStock >= initialStock);
		    
		    // Update cart badge
	        $(document).trigger({
	          type: 'cartupdate',
	          numItems: response
	        });
		  }
	    });
		
		// Custom sorting plugin
		(function($) {
		  $.fn.sorted = function(customOptions) {
		    var options = {
		      reversed: false,
		      by: function(a) { return a.text(); }
		    };
		    $.extend(options, customOptions);
		    $data = $(this);
		    arr = $data.get();
		    arr.sort(function(a, b) {
		      var valA = options.by($(a));
		      var valB = options.by($(b));
		      if (options.reversed) {
		        return (valA < valB) ? 1 : (valA > valB) ? -1 : 0;				
		      } else {		
		        return (valA < valB) ? -1 : (valA > valB) ? 1 : 0;	
		      }
		    });
		    return $(arr);
		  };
		})(jQuery);

		// DOMContentLoaded
		$(function() {

		  // bind radiobuttons in the form
		  var $sortButtons = $('.sort-action');

		  // get the items
		  var $catalog = $('#catalog');

		  // clone applications to get a second collection
		  var $catalogClone = $catalog.clone();

		  // Call Quicksand on every sort button click
		  $sortButtons.on('click', function(e) {
		    var $btn = $(this);
		    var sortBy = $btn.data('by');
		    var reverse = $btn.data('reverse');
			var $prodEntries = $catalogClone.find('.prod-entry-container');
			
			var $prodEntryCards = $('.prod-entry');
			$prodEntryCards.addClass('flying');

			var sortByFn;
			if (sortBy == 'name') {
			  sortByFn = function(v) {
		        return $(v).find('.prod-title h4').text().toLowerCase();
		      }
			} else if (sortBy == 'price') {
			  sortByFn = function(v) {
		        return parseFloat($(v).find('.prod-price-number').text());
		      }
			} else if (sortBy == 'type') {
			  sortByFn = function(v) {
		        return parseInt($(v).data('category'));
		      }
			} 			  
		    
		    var $sortedData = $prodEntries.sorted({
		      by: sortByFn,
		      reversed: reverse
		    });
		    
		    // finally, call quicksand
		    $catalog.quicksand($sortedData, {
		      duration: 800
		    }, function() {
		      $prodEntryCards.removeClass('flying');
		    });

		  });

		});
		</script>
	</jsp:attribute>
	<jsp:body>
		<!-- Page Content -->
		<div class="container">	
			<div class="row">	
				<div class="col-md-8 col-md-offset-2">	
		            <div class="row">
		            	<!-- Print the name of the category requested -->
		           		<h2 align="center">${category_name}</h2>
		           	</div>	
	           		<br>
	           		<div class="row">
	           			<form id="filter">
							<fieldset>
								<legend>Sort by</legend>
								<div class="btn-group" role="group"
									aria-label="Sort alphabetically">
									<button type="button" class="btn btn-primary sort-action"
										data-by="name" data-reverse="false">Alphabetically (A-Z)</button>
									<button type="button" class="btn btn-primary sort-action"
										data-by="name" data-reverse="true">Alphabetically (Z-A)</button>
								</div>
								<div class="btn-group" role="group" aria-label="Sort by price">
									<button type="button" class="btn btn-primary sort-action"
										data-by="price" data-reverse="false">Price (low to high)</button>
									<button type="button" class="btn btn-primary sort-action"
										data-by="price" data-reverse="true">Price (high to low)</button>
								</div>
								<div class="btn-group" role="group" aria-label="Sort by type">
									<button type="button" class="btn btn-primary sort-action"
										data-by="type" data-reverse="false">Type</button>
								</div>   
							</fieldset>
						</form>
	           		</div>
	           		<br>
	           		<div class="row" id="catalog">
	           			<!-- Go through the ArrayList inside the category attribute and print their values -->
		           		 <c:forEach items="${products}" var="product"
							varStatus="iterator">   
		           		 	<c:if test="${product.stock > 0}">
								<div class="col-sm-4 col-lg-4 col-md-4 prod-entry-container"
									data-category="${product.productCategory.id}"
									data-id="id-${iterator.index}">
									<div class="prod-entry">
										<a href="product?id=${product.id}"
											class="prod-thumbnail-container"> 
											<div class="wrapper-ribbon">
												<img src="${product.image}" alt="${product.name}"
													class="prod-thumbnail" />
												<c:if test="${product.onSale}">
													<div class="ribbon-wrapper-green">
														<div class="ribbon-green">Sale!</div>
													</div>
												</c:if>
											</div>

						           			<div class="prod-title">
												<h4>${product.name}</h4>
											</div>
											<div class="prod-price">
												<h4>$ <span class="prod-price-number">${product.retailPrice}</span>
												</h4>
											</div>
						           		</a>
						           		<div class="prod-info-container">
							           		<p>Units available: <span class="prod-stock"
													data-initial-stock="${product.stock}">${product.stock}</span>
											</p>
											<div class="prod-actions">
												<form class="action increment-unit" action="shoppingcart"
													method="post">
							                    	<input type="hidden" name="id"
														value="${product.id}">
													<input type="hidden" class="action" name="action"
														value="increment">
													<button type="submit" class="btn btn-default">
														<span class="glyphicon glyphicon-shopping-cart"></span>
														<span class="glyphicon glyphicon-plus-sign subscript"></span> Buy 1
													</button>
												</form>
												<form class="action decrement-unit pull-right"
													action="shoppingcart" method="post">
							                    	<input type="hidden" name="id"
														value="${product.id}">
													<input type="hidden" class="action" name="action"
														value="decrement">
													<button type="submit" class="btn btn-default" disabled>
														<span class="glyphicon glyphicon-shopping-cart"></span>
														<span class="glyphicon glyphicon-minus-sign subscript"></span> Remove 1
													</button>
												</form>	
											</div>
										</div>
										<div class="clearfix"></div>
									</div>
								</div>           	
		                    </c:if>
	                  	</c:forEach>
	                </div>
	            </div>
	        </div>
	    </div>			    
	</jsp:body>
</t:template>




