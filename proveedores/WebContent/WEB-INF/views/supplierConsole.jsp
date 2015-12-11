<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template>
	<jsp:attribute name="styles">
		<style type="text/css">
.center {
	text-align: center;
	font-style: oblique;
}

#addButton {
	background: #5a666e;
	background-image: -webkit-linear-gradient(top, #5a666e, #052336);
	background-image: -moz-linear-gradient(top, #5a666e, #052336);
	background-image: -ms-linear-gradient(top, #5a666e, #052336);
	background-image: -o-linear-gradient(top, #5a666e, #052336);
	background-image: linear-gradient(to bottom, #5a666e, #052336);
	-webkit-border-radius: 28;
	-moz-border-radius: 28;
	border-radius: 28px;
	font-family: Arial;
	color: #ffffff;
	font-size: 13px;
	padding: 10px 20px 10px 20px;
	text-decoration: none;
	float: right;
}

#addButton:hover {
	background: #3cb0fd;
	background-image: -webkit-linear-gradient(top, #3cb0fd, #1774a6);
	background-image: -moz-linear-gradient(top, #3cb0fd, #1774a6);
	background-image: -ms-linear-gradient(top, #3cb0fd, #1774a6);
	background-image: -o-linear-gradient(top, #3cb0fd, #1774a6);
	background-image: linear-gradient(to bottom, #3cb0fd, #1774a6);
	text-decoration: none;
}

td.center-aligned {
	text-align: center;
}

#product-name {
	max-width: 79px;
}

#product-name-input {
	max-width: 74px;
}

.description {
	margin: 30px 5px 7px;
	font-size: 12px;
	font-style: italic;
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
		<script type="text/javascript">
			$(".action-icon").on("click", function(e) {
		          var confirmDelete = confirm("Are you sure you want to delete this entry?");
		          if (confirmDelete) {
		            var $entry = $(this).closest(".entry");
		            var $id = $entry.find(".product-id").html();
		            $entry.fadeOut();
		            $.post("editproduct", {
		              id: $id,
		              action: "delete"
		            }, function(data) {
		              alert("Product deleted");
		            });
		          }
		        });
		      $(".entry input, .entry select").on("change", function(e) {
		        <c:choose>
			        <c:when test="${not empty ErrorAddingProduct}">
				       alert("${ErrorAddingProduct}"); 
				  	</c:when>
				  	<c:otherwise>
				        var $entry = $(this).closest(".entry");
				        var $id = $entry.find(".product-id").html();
				        var $select = $entry.find("select");
				        var $inputs = $entry.find("input");
				        var data = {};
				        $inputs.each(function(i) {
				          var name = $(this).attr("name");
				          data[name] = $(this).val();
				        });				        
				        data.category = $select.val();	
				        data.id = $id;
				        data.action = "edit";
				        $.post("editproduct", data, function(data) {
				          console.log("Changes saved");
				        });
		        	</c:otherwise>      
		      </c:choose>
   			});
    	</script>
	</jsp:attribute>
	<jsp:body>          
		<div class="container">
			<div class="row">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 main">
					<h1 class="page-header">Your Products</h1>		
					<!-- Nav pills -->
					<a class="btn btn-primary btn-lg" href="addproduct"><span
						class="glyphicon glyphicon-plus "></span> Add Product</a>
					<p class="description">Edits will be saved automatically</p>
						<!-- pill panes -->
						<div class="tab-content">
							<div class="tab-pane fade in active" id="clients">
								<div class="table-responsive">
									<table class="table table-hover">
									<thead>
										<tr>
											<th style="width: 30px">#</th>
											<th>Product</th>
											<th>Category</th>
											<th>Initial Stock</th>
											<th>Minimum Price</th>
											<th>Maximum Price</th>
											<th>Price</th>
											<th>Description</th>
											<th style="width: 40px">Del</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${products}" var="product">
										    <tr
												class="entry product-entry <c:if test="${product.stock <= stockThresHoldVeryLow }">danger</c:if>
										    								<c:if test="${product.stock > stockThresHoldVeryLow  && product.stock <= stockThresHoldLow }">warning</c:if>
										    								">
												<td class="center-aligned product-id ">
													${product.id}
												</td>
				
												<td class="center-aligned" id="product-name">
													<input id="product-name-input" type="text" name="name"
													required value="${product.name}">
												</td>
												<td class="center-aligned">
													<select class="form-control" name="category"
													id="select-category" required="required">
														<c:forEach items="${categories}" var="cat">
															<option value="${cat.id}"
																<c:if test="${product.productCategory.id == cat.id}">selected</c:if>>${cat.name}</option>
														</c:forEach>
													</select>
												</td>
												<td class="center-aligned">
													<input type="number" required min="0" name="stock"
													value="${product.stock}">
												</td>
												<td class="center-aligned">
													<input type="number" required name="min_price" min="0.01"
													step="0.01" value="${product.minPrice}">
												</td>
												<td class="center-aligned">
													<input type="number" required name="max_price" min="0.01"
													step="0.01" value="${product.maxPrice}">
												</td>
												<td class="center-aligned">
													<input type="number" required name="final_price" min="0.01"
													step="0.01" value="${product.finalPrice}">
												</td>
												<td class="center-aligned">
													<input type="text" required name="description"
													value="${product.description}">
												</td>
												<td class="center-aligned">
													<span class="glyphicon glyphicon-trash action-icon"></span>
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

