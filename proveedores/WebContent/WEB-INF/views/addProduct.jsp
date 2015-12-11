<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<t:template>
	<jsp:attribute name="styles">
		<link href="css/fileinput.min.css" rel="stylesheet">
		<link href="css/register.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">	
		<script src="js/fileinput.min.js"></script>
		<script src="js/validator.js"></script> 	
		<script type="text/javascript">
       	 <c:if test="${not empty ErrorAddingProduct}">
		  	$(document).ready(function(){
		  	  alert("${ErrorAddingProduct}");
		  	});    			
		</c:if>	
	      function pricesError(textbox) {
	       	  var minPrice = parseFloat(document.getElementById("min").value);
	       	  var maxPrice = parseFloat(document.getElementById("max").value);
	       	  if (maxPrice < minPrice) {
	       	    textbox.setCustomValidity('The maximum price must be higher than the minimum price');
	       	  } else if (textbox.value == '') {
	           	textbox.setCustomValidity('Please fill in this field');
	          } else {
	            textbox.setCustomValidity('');
	          }
	       	  return true;
	      };
	      function pricesErrorFinalPrice(textbox) {
       	  var minPrice = parseFloat(document.getElementById("min").value);
       	  var maxPrice = parseFloat(document.getElementById("max").value);
       	  var finalPrice = parseFloat(document.getElementById("final-price").value);
       	 	if (maxPrice < finalPrice || minPrice > finalPrice) {
       	    textbox.setCustomValidity('The price must be between the minimum and maximum ones');
       	  } else if (textbox.value == '') {
           	textbox.setCustomValidity('Please fill in this field');
          } else {
            textbox.setCustomValidity('');
          }
       	  return true;
      };
     	</script>	
	</jsp:attribute>
	<jsp:body>	
		<div class="col-sm-6 col-sm-offset-3">
    		<h3 class="text-center">Add product to your catalog</h3>
    	</div>		
		<form style="margin-left: 5px; margin-right: 5px;" id="addProductForm"
			action="addproduct" method="post" enctype="multipart/form-data">			
			<div class="form-group">
			    <div class="row">
			    	<div class="col-sm-6 col-sm-offset-3">
			    		<label class="control-label">Product Image</label>
		    			<input name="image" id="input-id" type="file" class="file"
							required data-show-upload="false" data-preview-file-type="text">
			    	</div>
			    </div>
		    </div>
		    <div class="col-sm-6 col-sm-offset-3" id="require-add-product">
			  The image must have the .jpg extension
			</div>
		    
		    <div class="form-group">
		        <div class="row">
		            <div class="col-sm-3 col-sm-offset-3">
		                <label class="control-label">Product name</label>
		                <input type="text" class="form-control" name="name"
							onInput="standardError(this)" required
							onInvalid="standardError(this)" />
		            </div>
		
		            <div class="col-sm-2 selectContainer">
		                <label class="control-label">Category</label>
		                <select class="form-control" name="category"
							onInvalid="standardError(this)" required
							onInput="standardError(this)">
		                    <c:forEach items="${categories}" var="cat">
		                    	<option value="${cat.id}">${cat.name}</option>
		                    </c:forEach>
		                </select>
		            </div>
		            
		            <div class="col-sm-1">
		                <label class="control-label">Initial Stock</label>
		                <input type="number" class="form-control" name="stock"
							value="1" onInvalid="standardError(this)" required min="1"
							onInput="standardError(this)" />
		            </div>
		        </div>
		    </div>
		    
		    <div class="form-group">
		        <div class="row">
		            <div class="col-sm-2 col-sm-offset-3">
		                <label class="control-label">Max Price</label>
		                <div class="input-group">
						  <span class="input-group-addon">$</span>
						  <input type="number" name="max_price" class="form-control"
								id="max" required onInput="pricesError(this)" min="0.02"
								step="0.01" value="15">
						</div>
		            </div>
		
		            <div class="col-sm-2">
		                <label class="control-label">Min Price</label>
		                <div class="input-group">
						  <span class="input-group-addon">$</span>
						  <input type="number" name="min_price" class="form-control"
								id="min" required onInput="pricesError(this)" min="0.01"
								step="0.01" value="5">
						</div>
		            </div>
		
		            <div class="col-sm-2">
		                <label class="control-label">Price</label>
		                <div class="input-group">
						  <span class="input-group-addon">$</span>
						  <input type="number" name="final_price" class="form-control"
								id="final-price" required onInput="pricesErrorFinalPrice(this)"
								min="0.01" step="0.01" value="10">
						</div>
		            </div>
		        </div>
		    </div>
	
		    <div class="form-group">
			    <div class="row">
			    	<div class="col-sm-6 col-sm-offset-3">
			    		<label class="control-label">Description</label>
			        	<textarea class="form-control" name="description" rows="3"
							onInvalid="standardError(this)" required
							onInput="standardError(this)"></textarea>
			    	</div>
			    </div>
		    </div>
		    <div class="row">
				<div class="col-sm-6 col-sm-offset-3" id="require-add-product">
				   All the fields are required
				</div>
			</div>
		    
	    	<div class="row">
		    	<div class="col-sm-6 col-sm-offset-3">
    				<button type="submit" class="btn btn-primary btn-block btn-lg">Add product</button>
		    	</div>
		    </div>
		    
		</form>
	</jsp:body>
</t:template>
