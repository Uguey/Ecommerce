<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template>
	<jsp:attribute name="styles">
	</jsp:attribute>
	<jsp:attribute name="scripts"></jsp:attribute>
	<jsp:body>
		<div class="container">	
			<div class="row">
				<div class="col-md-6 col-sm-12">	
		            <img alt="No results" src="images/no_results.png">
	            </div>	
				<div class="col-md-6 col-sm-12">	
		            <div class="row">		            	
			            <h2 class="center-text">Your query brought no results</h2>
			            <br />
			            <br />
			            <div class="row">
			            	<div class="col-md-2 col-md-offset-5 col-sm-12">
			            		<a href="search" class="btn btn-primary btn-lg"><span
									class="glyphicon glyphicon-search" aria-hidden="true"></span> Try again</a>
			            	</div>
			            </div>       
	                </div>
	            </div>
	        </div>
	    </div>			    
	</jsp:body>
</t:template>