<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template>
	<jsp:attribute name="styles">
	   <link href="css/common.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/validator.js"></script>
		<script>
		  function postalCodeError(textbox) {
		    if (textbox.value.length == 0) {
		      textbox.setCustomValidity('');
		      return true;
		    } else if (textbox.value.length != 5) {
		      textbox.setCustomValidity('Please enter a postal code (5 numbers)');
		    } else {
		      textbox.setCustomValidity('');
		    }
		    return true;
		  };
		</script>	
	</jsp:attribute>
	<jsp:body>
  	<div class="container">
      <div class="row">
        <div
					class="col-xs-12 col-sm-8 col-md-8 col-sm-offset-2 col-md-offset-2">
          	<c:if test="${not empty validation_error}">
				<script>
          			alert("${validation_error}")
        		</script>
			</c:if>

          <h2>Edit Profile of <small>${client.email}</small>
					</h2>
          <hr class="colorgraph ">
          <div class="panel panel-default panel-body">
            <form class="form-horizontal " role="form" action="profile"
							method="post">
            	<div class="form-group padding-top-ten-px">            	
            	<label class="col-md-2 control-label input-lg">Name:</label>
					 <div class="col-md-3">
						 <input class="form-control input-lg" name="name" id="name"
										value="${client.name}" type="text" required="required">
					 </div>
					 <label class="col-md-2 control-label input-lg">Surname:</label>
					 <div class="col-md-4">
						 <input class="form-control input-lg" name="surname" id="surname"
										value="${client.surname}" type="text" required="required">
					 </div>
				</div>
					
				<div class="form-group padding-top-ten-px">            	
                	<label class="col-md-2 control-label input-lg">E-mail:</label>
                <div class="col-md-9">

	                  <input class="form-control input-lg" name="email"
										id="email" value="${client.email}" type="email"
										onInvalid="emailError(this);" onInput="emailError(this);"
										required="required">
                </div>
              </div>
	          <div class="form-group">
	                	<label class="col-md-2 control-label input-lg">Password:</label>
                	<div class="col-md-3">
		                  <input class="form-control input-lg" name="password"
										id="password_first" value="" type="password">
	                </div>
	             
                	<label class="col-md-2 control-label input-lg">Confirm password:</label>
                <div class="col-md-4">

	                  <input class="form-control input-lg"
										name="password_confirmation" onInput="passwordError(this);"
										required="required" id="password_confirmation" value=""
										type="password">
	                </div>
              </div>
              <hr>
	          <div class="form-group">
	                	<label class="col-md-2 control-label input-lg">Address:</label>
                	<div class="col-md-3">
		                  <input class="form-control input-lg" name="address"
										value="${address}" type="text">
	                </div>
	             
                	<label class="col-md-2 control-label input-lg">Postal Code:</label>
                	<div class="col-md-4">
	                  <input class="form-control input-lg"
										name="postalCode" value="${postalCode}" type="text"
										pattern="[0-9]{5}" onkeypress="return isNumber(event)"
										onInput="postalCodeError(this)">
	                </div>
	                </div>
	               	<div class="form-group">	                
	                <label class="col-md-2 control-label input-lg">City:</label>
                	<div class="col-md-4">
	                  <input class="form-control input-lg" name="city"
										value="${city}" type="text">
	                </div>
              </div>
              <div class="form-groupcol-xs-12 col-md-12">
                <label class="col-md-3 control-label"></label>
    			        <input class="btn btn-primary btn-block btn-lg"
									value="Save Changes" type="submit">
              </div>
            </form>
          </div>
          
          <h2>Options</h2>
          <div class="col-md-12">
	          <a href="orderhistory" type="button"
							class="btn btn-default btn-lg">
	 			 <span class="glyphicon glyphicon-barcode" aria-hidden="true"></span>  Order history
			  </a>
			  <a href="watchlist" type="button" class="btn btn-default btn-lg">
	 			 <span class="glyphicon glyphicon-star" aria-hidden="true"></span>  Starred Products
			  </a>
          </div>
        </div>
      </div>
    </div>
  </jsp:body>
</t:template>