<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template>
	<jsp:attribute name="styles">
			<link href="css/ribbon.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/jquery.form.min.js"></script> 
		<script type="text/javascript">
			
		$('.add-to-cart').ajaxForm({
		  beforeSubmit: function() {
		    var userLoggedIn = ${not empty email}; 
		 	
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
		  	//Update stock
		  	var stockBefore = $form.parent().find(".stock").html();
			var newStock = stockBefore-1;
			$form.parent().find(".stock").text(newStock);	
			        	
			// Update cart badge
	        $(document).trigger({
	          type: 'cartupdate',
	          numItems: response
	        });
		  }
	    });
		</script>
	</jsp:attribute>
	<jsp:body>
		<!-- Page Content -->
		<div class="container">	
			<div class="row">	
				<div class="col-md-12">	
		            <div class="row">
			           	<h2 align="center">${product.name}</h2>	
	           			<br />
	           		</div>
	           		<div class="row">
	                    <div class="col-md-2 wrapper-ribbon">      
                       		<img src="${product.image}" alt="" width="100%">
							<c:if test="${product.onSale}">
								<div class="ribbon-wrapper-green">
									<div class="ribbon-green">Sale!</div>
								</div>
							</c:if>
	                    </div>
	                    <div class="col-md-10">
                        	<h3 style="color: blue; font-size: 170%">$${product.retailPrice}</h3> 
                            <p>Units left: <span class="stock">${product.stock}</span></p> 
                            <c:if test="${(product.stock > 0)}">
	                            <form class="add-to-cart" name="button"
									action="shoppingcart" method="post">
	                               	<input type="hidden" name="id"
										value="${product.id}">
	                               	<input type="hidden" class="action"
										name="action" value="add">
	                                <input type="submit"
										class="btn btn-default cart" value="Add to cart">
	                            </form>	                            
                                <c:if test="${not empty email}">
                                	<form action="watchlist" method="post">
	                            		<input type="hidden" name="id"
											value="${product.id}">
                                		<c:if test="${subscription}">
                                			<input type="hidden"
												name="action-subscription" value="delete">											
                     						<input type="submit" class="btn btn-default"
												value="Delete from watch list">											
                                		</c:if>
                               			<c:if test="${!subscription}">
                               			   	<input type="hidden"
												name="action-subscription" value="create">											
                               				<input type="submit"
												class="btn btn-default" value="Add to watch list">		
                               			</c:if>
                                	</form>                                			
								</c:if>
                            </c:if>
                            <hr />
                            <p>${product.description}</p>	                                               		
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>			    
	</jsp:body>
</t:template>
