<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<t:template>
	<jsp:attribute name="styles">
		<style type="text/css">
#submit-row {
	margin-top: 40px;
	margin-bottom: 20px;
}
</style>
	</jsp:attribute>
	<jsp:attribute name="scripts">	
		<script src="js/validator.js"></script> 	
		<script type="text/javascript">
     	</script>	
	</jsp:attribute>
	<jsp:body>	
		<div class="col-sm-6 col-sm-offset-3">
    		<h2 class="text-center">Product search</h2>
    	</div>		
		<form id="search-form" action="search" method="post">
		    <div class="form-group">
		        <div class="row">
		            <div class="col-sm-6 col-sm-offset-3">
		                <label class="control-label">Product name</label>
		                <input type="text" class="form-control" name="name" />
		            </div>
		        </div>
		    </div>
		    <div class="form-group">    
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3">
						<label class="control-label">Category</label>
						<div class="input-group">
							<div class="input-group-btn" data-toggle="buttons">
								<label class="btn btn-primary active">
									<input type="radio" name="category_op" value="AND"
									autocomplete="off" checked> AND
								</label>
								<label class="btn btn-primary">
									<input type="radio" name="category_op" value="OR"
									autocomplete="off"> OR
								</label>
							</div>
			                <select class="form-control" name="category">
			                	<option value="-1" selected>Any</option>
			                    <c:forEach items="${categories}" var="cat">
			                    	<option value="${cat.id}">${cat.name}</option>
			                    </c:forEach>
			                </select>
						</div>
		            </div>
		        </div>
		    </div>
		    
		    <div class="form-group">    
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3">
						<label class="control-label">Brand (supplier)</label>
						<div class="input-group">
							<div class="input-group-btn" data-toggle="buttons">
								<label class="btn btn-primary active">
									<input type="radio" name="supplier_op" value="AND"
									autocomplete="off" checked> AND
								</label>
								<label class="btn btn-primary">
									<input type="radio" name="supplier_op" value="OR"
									autocomplete="off"> OR
								</label>
							</div>
			                <select class="form-control" name="supplier">
			                	<option value="-1" selected>Any</option>
			                    <c:forEach items="${suppliers}" var="sup">
			                    	<option value="${sup.id}">${sup.companyName}</option>
			                    </c:forEach>
			                </select>
						</div>
		            </div>
		        </div>
		    </div>
		    
		    <div class="form-group">    
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3">
						<label class="control-label">Price ($)</label>
						<div class="input-group">
							<div class="input-group-btn" data-toggle="buttons">
								<label class="btn btn-primary">
									<input type="radio" name="price_op" value="&lt;"
									autocomplete="off"> Lower than
								</label>
								<label class="btn btn-primary">
									<input type="radio" name="price_op" value="&gt;"
									autocomplete="off"> Higher than 
								</label>
								<label class="btn btn-primary">
									<input type="radio" name="price_op" value="&#61;"
									autocomplete="off"> Equal to
								</label>
								<label class="btn btn-primary active">
									<input type="radio" name="price_op" value="ANY"
									autocomplete="off" checked> Any
								</label>
							</div>
							<input type="number" name="price" class="form-control" id="max"
								min="0" step="0.01" value="0">
						</div>
		            </div>
		        </div>
		    </div>
		       
	    	<div id="submit-row" class="row">
		    	<div class="col-sm-2 col-sm-offset-5">
    				<button type="submit" class="btn btn-primary btn-block btn-lg">Search</button>
		    	</div>
		    </div>
		    
		</form>
	</jsp:body>
</t:template>
