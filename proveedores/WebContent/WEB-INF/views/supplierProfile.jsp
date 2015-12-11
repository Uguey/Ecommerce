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
	<c:if test="${not empty badCredentials}">
		<script>
		$(document).ready(function(){
		  alert("${badCredentials}");
		});
		</script>
	</c:if>	
  </jsp:attribute>
	<jsp:body>
  	<div class="container">
      <div class="row">
        <div
					class="col-xs-12 col-sm-8 col-md-8 col-sm-offset-2 col-md-offset-2">
          <h2>Edit Profile of <small>${supplier.email}</small>
					</h2>
          <hr class="colorgraph ">
          <div class="panel panel-default panel-body">
            <form class="form-horizontal " role="form" action="profile"
							method="post">
            	<div class="form-group padding-top-ten-px">
                <label class="col-md-4 control-label input-lg">E-mail:</label>
                <div class="col-md-6">
                  <input class="form-control input-lg" name="email"
										id="email" value="${supplier.email}" type="email"
										onInvalid="emailError(this);" required
										onInput="emailError(this);">
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">Company name:</label>
                <div class="col-md-6">
                  <input class="form-control input-lg" name="company"
										id="company" value="${supplier.companyName}" type="text"
										onInvalid="standardError(this)" required
										onInput="standardError(this)">
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">Password:</label>
                <div class="col-md-6">
                  <input class="form-control input-lg" name="password"
										id="password_first" value="" type="password"
										onInvalid="standardError(this)" required
										onInput="standardError(this)">
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-4 control-label input-lg">Confirm password:</label>
                <div class="col-md-6">
                  <input class="form-control input-lg"
										name="password_confirmation" id="password_confirmation"
										value="" type="password" required
										onInvalid="passwordError(this);"
										onInput="passwordError(this);">
                </div>
              </div>
              <div class="form-groupcol-xs-12 col-md-12">
                <label class="col-md-3 control-label"></label>
    			        <input class="btn btn-primary btn-block btn-lg"
									value="Save Changes" type="submit">
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </jsp:body>
</t:template>