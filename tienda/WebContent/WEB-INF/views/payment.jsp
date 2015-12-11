<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<t:template>
	<jsp:attribute name="styles">
		<link href="css/payment.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript">
			$(function () {
			  $('[data-toggle="tooltip"]').tooltip();
			})
		</script>
	<script src="js/validator.js"></script>
	</jsp:attribute>
	<jsp:body>
  	<div class="container">
      <div class="row">
        <div class="col-xs-12 col-sm-8 col-md-8 col-sm-offset-2 col-md-offset-2">
			<c:if test="${not empty validation_error}">
				<script>
	         	 alert("${validation_error}");
	        	</script>
			</c:if>
          <h2>Payment Page <small>${email}</small>
					</h2>
          <hr class="colorgraph ">
          <div class="panel panel-default panel-body">
            <form method="post" class="form-horizontal " role="form"
							action="payment">
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">Your Purchase:</label>
                <div class="col-md-6">
                <c:forEach items="${purchaseItems}"
										var="currentPurchaseItem">
                <c:set var="i" value="${currentPurchaseItem.product}"></c:set>
                <c:set var="totalPrice" value="${i.retailPrice * currentPurchaseItem.quantity + totalPrice}"></c:set> 
                  <div class="row">
                  	<div class="col-md-3">
                  		<img src="${i.image}" width="100%" />
                  	</div>
                    <div class="description col-md-9">
                      <p>${i.description}</p>
                      <p>${currentPurchaseItem.quantity} units &times; $${i.retailPrice} = $${i.retailPrice*currentPurchaseItem.quantity}</p>
                    </div>
                  </div>
                 </c:forEach> 
                </div>
              </div>
              <hr class="colorgraph">
              <div class="form-group padding-top-ten-px">
                <img src="images/dollars.png" class="icon" />
                <label class="col-md-4 control-label input-lg">Total $ to pay: </label>
                <div class="col-md-6">
                  <input class="form-control input-lg" name="total-price" value="${totalPrice}" type="text" readonly>
                </div>
              </div>
              <div class="form-group padding-top-ten-px">
                <img src="images/dollars.png" class="icon" />
                <label class="col-md-4 control-label input-lg">Coupon Codes
                	<a data-toggle="tooltip" title="Only one coupon can be used per purchase. The coupon can be applied to up to 10% of the purchase value. Coupons expire a month from their expedition date.">(conditions)</a>
                	:
                </label>
                <div class="col-md-6">
                  	<select name="client-coupon" class="form-control">
                  		<option value="no_coupon">Select coupon</option>
                  	<c:forEach items="${coupons}" var="coupon">
                  		<option value="${coupon.id}">${coupon.toString()} - can be used to save $${coupon.getRedeemableValue(totalPrice)} </option>
                  	</c:forEach>
					</select>
                </div>
              </div>
              <hr class="colorgraph">
               <div class="form-group">
                <img src="images/address.png" class="icon" />
                <label class="col-md-4 control-label input-lg">Address: </label>
                <div class="col-md-6">
                  	<input class="form-control input-lg"
										name="client-address" placeholder="Street, number" type="text"
										value="${address}" required>
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">Postal Code: </label>
                <div class="col-md-6">
                    <input class="form-control input-lg"
										name="client-cp" type="text" placeholder="Postal Code"
										onkeypress="return isNumber(event)"
										onInput="postalCodeError(this)" value="${cp}"required>
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">City: </label>
                <div class="col-md-6">
                    <input class="form-control input-lg" 
                    					name="client-city" type="text" placeholder="City" 
                    					value="${city}" required>
                </div>
              </div>
              <hr class="colorgraph">
              <div class="form-group">
                <img src="images/creditCard.png" class="icon" />
                <label class="col-md-4 control-label input-lg">Name in Credit Card:</label>
                <div class="col-md-6"> 
                    <input class="form-control input-lg"
										name="client-account"
										placeholder="Name and Surname of Account Owner" type="text"
										required>
                </div>	  
              </div>
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">Credit Card number:</label>
                <div class="col-md-6"> 
                  	<input class="form-control input-lg"
										name="client-creditCard" placeholder="Credit Card Number"
										type="text" onInput="creditCardError(this)" required>                   
                </div>	  
              </div>
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">Expiration Date:</label>
                <div class="col-md-3"> 
                  	<select id='expireMM' name="client-expirationMonth" class="form-control"
										required>
					    <option value=''>Month</option>
					    <option value='1'>January</option>
					    <option value='2'>February</option>
					    <option value='3'>March</option>
					    <option value='4'>April</option>
					    <option value='5'>May</option>
					    <option value='6'>June</option>
					    <option value='7'>July</option>
					    <option value='8'>August</option>
					    <option value='9'>September</option>
					    <option value='10'>October</option>
					    <option value='11'>November</option>
					    <option value='12'>December</option>
					</select> 
				</div>
				<div class="col-md-3">
					<select class="form-control" name='client-expirationYear' id='expireYY' required>
					    <option value=''>Year</option>
					    <option value='14'>2014</option>
					    <option value='15'>2015</option>
					    <option value='16'>2016</option>
					    <option value='17'>2017</option>
					    <option value='18'>2018</option>
					</select>					
                </div>	  
              </div>
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">CVV (3 numbers):</label>
                <div class="col-md-6"> 
                  	<input class="cvv" name="client-cvv" type="text" onkeypress="return isNumber(event)" onInput="cvvError(this)" required></input>                
                </div>	  
              </div>
              <div class="form-group col-xs-12 col-md-12">
		        <input class="btn btn-primary btn-block btn-lg"
									value="Pay" type="submit">
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </jsp:body>
</t:template>