<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template>
	<jsp:attribute name="styles">
		<link href="css/register.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/validator.js"></script>	
	</jsp:attribute>
	<jsp:body>
		<div class="container">
			  <div class="row">
			    <div
					class="col-xs-12 col-sm-8 col-md-6 col-sm-offset-2 col-md-offset-3">
			    					
					<c:if test="${result eq 'ok'}">
							<script type="text/javascript">
							alert("Supplier registered successfully!");
							window.location.href=".";
							</script>
					</c:if>
					<c:if test="${result eq 'oldSupplier'}">
							<script type="text/javascript">
							alert("There is already a supplier with the same email. Please try again");
							</script>
					</c:if>
					
					<c:if test="${result eq 'error'}">
							<script type="text/javascript">
							$(document).ready(function(){
							  alert("${badCredentials}");
							});							
							</script>
					</c:if>
					
					<form action="register" method="post" role="form">
						<h2>Supplier Sign Up <small>Ceramic won't be the same</small>
						</h2>
						
						<hr class="colorgraph">
						
						<!-- Sign Up form -->
						<div class="form-group">
							<input type="email" name="email" id="email"
								onInvalid="emailError(this);" onInput="emailError(this);"
								required class="form-control input-lg"
								placeholder="Email Address" tabindex="4" />
						</div>
						<div class="form-group">
							<input name="company" id="company" class="form-control input-lg"
								onInvalid="standardError(this)" required
								onInput="standardError(this)" placeholder="Company Name"
								tabindex="4">
						</div>
						<div class="row">
							<div class="col-xs-12 col-sm-6 col-md-6">
								<div class="form-group">
									<input type="password" name="password" id="password_first"
										onInvalid="standardError(this)" onInput="standardError(this)"
										required class="form-control input-lg" placeholder="Password"
										tabindex="5" />
								</div>
							</div>
							<div class="col-xs-12 col-sm-6 col-md-6">
								<div class="form-group">
									<input type="password" name="password_confirmation"
										onInvalid="passwordError(this);"
										onInput="passwordError(this);" required
										id="password_confirmation" class="form-control input-lg"
										placeholder="Confirm Password" tabindex="6" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12" id="require">
							   All the fields are required
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12">
								 By clicking <strong class="label label-primary">Register</strong>, you agree to the <a
									href="#" data-toggle="modal" data-target="#t_and_c_m">Terms and Conditions</a> set out by this site, including our Cookie Use.
							</div>
						</div>						
						<hr class="colorgraph">
						<div class="row">
							<div class="col-xs-12 col-md-12">
								<input type="submit" value="Register"
									class="btn btn-primary btn-block btn-lg" tabindex="7">
							</div>
						</div>
					</form>
				</div>
			</div>
			<!-- Modal -->
			<div class="modal fade" id="t_and_c_m" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-lg">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">×</button>
							<h4 class="modal-title" id="myModalLabel">Terms and Conditions</h4>
						</div>
						<div class="modal-body">
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique, itaque, modi, aliquam nostrum at sapiente consequuntur natus odio reiciendis perferendis rem nisi tempore possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique, itaque, modi, aliquam nostrum at sapiente consequuntur natus odio reiciendis perferendis rem nisi tempore possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique, itaque, modi, aliquam nostrum at sapiente consequuntur natus odio reiciendis perferendis rem nisi tempore possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique, itaque, modi, aliquam nostrum at sapiente consequuntur natus odio reiciendis perferendis rem nisi tempore possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique, itaque, modi, aliquam nostrum at sapiente consequuntur natus odio reiciendis perferendis rem nisi tempore possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique, itaque, modi, aliquam nostrum at sapiente consequuntur natus odio reiciendis perferendis rem nisi tempore possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique, itaque, modi, aliquam nostrum at sapiente consequuntur natus odio reiciendis perferendis rem nisi tempore possimus ipsa porro delectus quidem dolorem ad.</p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								data-dismiss="modal">I Agree</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
		</div>
	</jsp:body>
</t:template>