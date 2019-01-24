
<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- <html>
<head>
<meta charset="ISO-8859-1">
<title>User Form</title>
</head>
<body>
<h1>This will be a user form</h1>
</body>
</html>  -->

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="sat, 01 Dec 2001 00:00:00 GMT">
<title>User Form</title>
 <!-- <link
	href="static/css/bootstrap.min.css" rel="stylesheet">   -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"> 
<!--<link
	href="C:\Users\Abhi\eclipse-workspace\userForm\src\main\webapp\static\css\style.css"
	rel="stylesheet">   -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

<link rel="stylesheet" href="static/css/mystyle3.css">
</head>

<body>
<div class="wrapper">
	<div role="navigation">
		<div class="navbar">
			<a href="/welcome" class="navbar_brand">User Form</a>
			<a href="/login" class="nav_right">Login</a>
			<a href="/register" class="nav_right">New Registration</a>
			<a href="/show-users" class="nav_right">All Users</a>
		</div>
	</div>

<c:choose>
<c:when test="${ mode=='MODE_HOME'}">
	<div class="container" id="homediv">
		<div class="jumbotron text-center">
			<h1>Welcome to User Form</h1>
			<h3>Click on New Registration tab to register</h3>
		</div>
	</div>
</c:when>

<c:when test="${ mode=='MODE_REGISTER'}">
	<div class="container text-center">
				<h3>New Registration</h3>
				<hr>
				<form class="userForm" method="POST" action="save-user">
					<input type="hidden" name="id" value="${user.id }" />
					<div class="formGroup">
						<label class="lab">Username</label>
						<br><br>
							<input type="text" class="formctl" name="username"
								value="${user.username }" />
						
					</div>
					<div class="formGroup">
						<label class="lab">First Name</label>
						<br><br>
							<input type="text" class="formctl" name="firstname"
								value="${user.firstname }" />
						
					</div>
					<div class="formGroup">
						<label class="lab">Last Name</label>
						<br><br>
							<input type="text" class="formctl" name="lastname"
								value="${user.lastname }" />
						
					</div>
					<div class="formGroup">
						<label class="lab">Age </label>
						<br><br>
							<input type="text" class="formctl" name="age"
								value="${user.age }" />
						
					</div>
					<div class="formGroup">
						<label class="lab">Password</label>
						<br><br>
							<input type="password" class="formctl" name="password"
								value="${user.password }" />
						
					</div>
					<div class="formGroup ">
						<input type="submit" class="sub" value="Register" />
					</div>
				</form>
			</div>
			
			</c:when>
			
			<c:when test="${mode=='ALL_USERS' }">
			<div class="container text-center" id="tasksDiv">
				<h3>All Users</h3>
				<hr>
				<div class="table-responsive">
					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th>Id</th>
								<th>UserName</th>
								<th>First Name</th>
								<th>LastName</th>
								<th>Age</th>
								<th>Delete</th>
								<th>Edit</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="user" items="${users }">
								<tr>
									<td>${user.id}</td>
									<td>${user.username}</td>
									<td>${user.firstname}</td>
									<td>${user.lastname}</td>
									<td>${user.age}</td>
									<td><a href="/delete-user?id=${user.id }"><span
											class="glyphicon glyphicon-trash"></span></a></td>
									<td><a href="/edit-user?id=${user.id }"><span class="glyphicon glyphicon-pencil"></span></a></td>
									
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:when>
		
		<c:when test="${mode=='MODE_UPDATE' }">
		<h2>Update User</h2>
		<hr>
		<form method="post" action="save-user">
			<input type="hidden" name="id" value="${user.id }"/>
			<div class="formGroup">
				<label class="lab">Username:</label><br>
				<input type="text" name="username" class="formctl" value="${user.username }"/>
			</div>
			<br>
			<div class="formGroup"> 
				<label class="lab">Firstname:</label><br>
				<input type="text" class="formctl" name="firstname" value="${user.firstname }"/>
			</div>
			<br>
			<div class="formGroup">
			<label class="lab">Lastname:</label><br>
			<input type="text" class="formctl" name="lastname" value="${user.lastname }"/>
			</div>
			<br>
			<div class="formGroup">
			<label class="lab">Age:</label><br>
			<input type="text" class="formctl" name="age" value="${user.age }"/>
			</div>
			<br>
			<div class="formGroup">
			<label class="lab">Password:</label><br>
			<input type="password" class="formctl" name="password" value="${user.password }"/>
			</div>
			<br>
			<div class="formGroup">
			<input type="submit" value="Update"/>
			</div>
			<br>
		</form>
		</c:when>
		
		<c:when test="${mode=='MODE_LOGIN' }">
			<h2>User Login</h2>
			<hr>
			<form method="post" action="login-user">
				
				<c:if test="${not empty error }">
					<div class="alert alert-danger">
					<c:out value="${error }"></c:out>
					</div>
				</c:if>
				
				<div class="formGroup">
				<label class="lab">Username:</label><br>
				<input type="text" name="username" class="fc" value="${user.username }"/>
				</div>
				<br>
				<div class="formGroup">
				<label class="lab">Password:</label><br>
				<input type="password" class="fc" name="password" value="${user.password }"/>
				</div>
				<br>
				<div class="formGroup">
				<input type="submit" value="Login"/>
				</div>
				<br>
				<label style="font-size:18px; padding-left:7px;">New user?</label>
				<a href="/register" style="font-size:18px;">Register here</a>
			</form>
		</c:when>
			
</c:choose>
<!-- 
</div>
	<script
		src="static/js/jquery-1.11.1.min.js"></script>
	<script
		src="static/js/bootstrap.min.js"></script>   --> 
</body>
</html>