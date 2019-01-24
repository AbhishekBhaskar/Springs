<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Homepage</title>
<link rel="stylesheet" href="static/css/mystyle3.css">
</head>

<body>
<!--<c:choose>
	<c:when test="${mode=='MODE_LOGGEDIN' }">
		<h2>Welcome ${user.firstname }</h2>
		<h3>Id is ${user.id }</h3>
	</c:when>
</c:choose>-->



<h2>Welcome ${name}</h2>

<h3>Values are</h3>
<c:forEach var="val" items="${val}">
<h4>${val.id }</h4>
<h4>${val.username }</h4>
<h4>${val.firstname }</h4>
<h4>${val.lastname }</h4>
<h4>${val.age}</h4>
<h4>${val.password }</h4>
</c:forEach>

</body>
</html>