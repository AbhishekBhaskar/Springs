<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Homepage</title>
<link rel="stylesheet" href="static/css/mystyle3.css">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>
<!--<c:choose>
	<c:when test="${mode=='MODE_LOGGEDIN' }">
		<h2>Welcome ${user.firstname }</h2>
		<h3>Id is ${user.id }</h3>
	</c:when>
</c:choose>-->


<wrapper>
	<div role="navigation">
			<div class="navbar">
				<a href="/welcome" class="navbar_brand">User Form</a>
				<a href="/login" class="nav_right">Login</a>
				<a href="/register" class="nav_right">New Registration</a>
				<a href="/show-users" class="nav_right">All Users</a>
			</div>
		</div>

<br>
	<h2>Welcome ${name}</h2>
	
	<h3>Values are</h3>
	
	<h4>${iD }</h4>
	<h4>${firstname }</h4>
	<h4>${lastname }</h4>
	<h4>${age }</h4>
	<h4>${password }</h4>
	<br>
	
	<c:forEach var="i" begin="1" end="5">
		<c:forEach var="j" begin="1" end="5">
			<i class="material-icons">event_seat</i>
		</c:forEach>
		<br>
	</c:forEach>
	
</wrapper>


</body>
</html>