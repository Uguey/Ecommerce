<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template>
	<jsp:attribute name="styles">
		<link href="css/shop-homepage.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript">
			$(document).ready(function(){
			  var opacity;
			  $(".opacityClass").on("mouseover", function() {
			    opacity = $(this).css("opacity");
			    $(this).fadeTo("slow", 0.5);
			  });
			  $(".opacityClass").on("mouseout", function() {
			    $(this).fadeTo("fast", opacity);
			  });
			});
		</script>	
	</jsp:attribute>
	<jsp:body>
		<div class="container">	
			<div class="row">	
				<div class="col-md-8 col-md-offset-2">	
					<!-- Offers section -->
					<h2 class="titles center">
						<a href="offers">On Sale</a>
					</h2>
					<br>
					<div class="row carousel-holder">
					      <div class="col-md-offset-4 col-md-4">        	
		                    <div id="carousel-offers" class="carousel slide"
								data-ride="carousel">
		                        <!-- Products in offer -->                            
		                        <div class="carousel-inner">
	                                <c:forEach items="${productsOnSale}"
										var="product" varStatus="iterator">
	                                	<div class="item <c:if test="${iterator.first}">active</c:if>">  
											<div class="prod-entry-container">
												<div class="prod-entry">
													<a href="product?id=${product.id}"
														class="prod-thumbnail-container"> 
														<img src="${product.image}" alt="${product.name}"
																class="prod-thumbnail" />
														<div class="prod-title">
															<h4>${product.name}</h4>
														</div>
														<div class="prod-price">
															<h4>$ <span class="prod-price-number">${product.retailPrice}</span>
															</h4>
														</div>
									           		</a>
													<div class="clearfix"></div>
												</div>
											</div>           	
		                                </div>
				                  	</c:forEach>
	                            </div>
		                        <!-- Arrows at lateral sites -->    
	                            <a class="left carousel-control"
									href="#carousel-offers" data-slide="prev">
	                                <span
									class="glyphicon glyphicon-chevron-left"></span>
	                            </a>
	                            <a class="right carousel-control"
									href="#carousel-offers" data-slide="next">
	                                <span
									class="glyphicon glyphicon-chevron-right"></span>
	                            </a>
	                        </div>
	                    </div>
			        </div>	
			        <!-- Products category section -->
			        <br>
			        <h2 class="titles center">Catalog</h2>	        
			        <br>
			        <div class="row">
			        	<c:forEach items="${categories}" var="cat">
			        		<div class="col-sm-4 col-lg-4 col-md-4">
		                        <div class="thumbnail"> 
		                        	<div class="opacityClass">  
		                        		<a href="products?cid=${cat.id}">        
		                            	<img src="${cat.image}" alt="${cat.name}">
		                            	</a>
		                            </div>
		                            <div class="caption">
		                                <h4>
											<a href="products?cid=${cat.id}">${cat.name}</a>
										</h4>                                		
		                            </div>
		                        </div>
		                    </div>
			        	</c:forEach>			
		            </div>
		        </div>
		    </div>
		 </div>			    
	</jsp:body>
</t:template>

