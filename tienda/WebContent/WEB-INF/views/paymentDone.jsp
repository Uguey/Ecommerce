<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<t:template>
	<jsp:attribute name="styles">
		<link href="css/common.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript">
			$(function () {
			  $('[data-toggle="tooltip"]').tooltip();
			})
		</script>
	</jsp:attribute>
	<jsp:body>
  	<div class="container">
      <div class="row">
        <div class="col-xs-12 col-sm-8 col-md-8 col-sm-offset-2 col-md-offset-2">
          <h2>Payment Page <small>${client.email}</small></h2>
          <c:if test="${coupon > 0}">
          	<div class="alert alert-success alert-dismissible" role="alert">
			  <button type="button" class="close" data-dismiss="alert">
			  	<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
			  </button>
			 <strong>Congratulations!</strong> You have been awarded a discount coupon of $${coupon} for your next purchase, redeemable a month from now. 
			 <a data-toggle="tooltip" 
			 	title="Only one coupon can be used per purchase. The coupon can be applied to up to 10% of the purchase value. Coupons expire a month from their expedition date.">
			 	Conditions apply
			 </a>. 
			</div>
          </c:if>
          <h3>Your payment has been received 
          	<img id="paymentDoneIcon" src="images/paymentDone.png"
							class="icon" />
          </h3>
          <hr class="colorgraph ">
          <div id="myDivToPrint" class="panel panel-default panel-body">
            <form id="printablediv" class="form-horizontal " role="form"
							action="profile">
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">You Have Purchased:</label>
                <div class="col-md-6">
                  
                  <table class="table table-hover table-bordered"
										id="t01" style="width: 100%">
                   	  <tr>
					    <th>Name</th>
					    <th>Supplier</th>
					    <th>Price ($/unit)</th>
					    <th>Quantity</th>					    
					    <th>Total</th>
					  </tr>
					 <c:forEach items="${purchaseItems}" var="i">
					  <tr>
					    <td>${i.productName}</td>
					    <td>${i.supplierCompany}</td>	
					    <td>${i.productPrice}</td>
					    <td>${i.quantity}</td>
					    <td>${i.productPrice * i.quantity}</td>					    
					  </tr>							  
				  </c:forEach> 
				</table>
                </div>
              </div>
              <div class="form-group padding-top-ten-px">
                <label class="col-md-4 control-label input-lg">Discounts:</label>
                <div class="col-md-6">
                  <input class="form-control input-lg" value="$ ${purchase.discount}" type="text" readonly>
                </div>
              </div>
              <div class="form-group padding-top-ten-px">
                <label class="col-md-4 control-label input-lg">Total Paid: (with discounts applied) </label>
                <div class="col-md-6">
                  <input class="form-control input-lg" value="$ ${purchase.totalPrice}" type="text" readonly>
                </div>
               <input id="printButton" type="button" value="Print" onclick="javascript:printDiv('printablediv')"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </jsp:body>
</t:template>