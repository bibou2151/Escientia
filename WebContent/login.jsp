<%@page import="business.Utilisateur"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link rel="SHORTCUT ICON" type="image/x-icon" href="images/icons/favicon.ico"/>
	<link rel="icon" type="image/x-icon" href="images/icons/favicon.ico" />
	<title>Connectez vous - Escientia, un site pour etudier online</title>
</head>
<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");
%>
<%
if(user != null){
	request.getRequestDispatcher("/compte.jsp").forward(request, response);
}
%>
<body>
	<div id="header">
		<div class="section">
			<div class="logo">
				<a href="index.jsp">Escientia</a>
			</div>
			<ul>
			<li>
				<a href="index.jsp">Acceuil</a>
			</li>
			<li>
				<a href="cours.jsp">Cours</a>
			</li>
			<%if(user != null){ %>
			<li>
				<a href="compte.jsp">Compte</a>
			</li>
			<li>
				<a href="logout">Déconnectez</a>
			</li>
			<%}else{ %>
			<li class="selected">				
				<a href="login.jsp">Connexion</a>
			</li>
			<%} %>
			<li>
				<a href="about.jsp">A propos</a>
			</li>
			</ul>
		</div>
	</div>
	
	<div id="body">
		<div>
			<div class="featured">
				<h2>Login</h2>
				<p>
				<br>
					Ce site vous aide à apprendre nouvelles aptitudes et d'acquérir de nouvelles connaissances. 
				<br>
				Connecter à votre compte:</p>
				<h4 style=" color: red;">
				<%
					if(request.getAttribute("error") != null)
					out.print(request.getAttribute("error")); 
				%></h4>
				<form action="login" method="post">
				<table>
				<tr><td>
					<label for="username">Nom d'utilisateur: </label>
					<td>
					<input name="username" type="text" required>
				<tr><td>
					<label for="password">Mot de passe: </label>
					<td>
					<input name="password" type="password" required>
					<%if(request.getAttribute("link")!= null) {%>
					<input name="link" type="hidden" value="<%= request.getAttribute("link") %>"><%} %>
				</table>
					<p>
					<br>
					N'avez pas un compte? <a href="inscrire.jsp">inscrivez vous</a></p>
					<input class="button" type="submit" value="Connexion">
				
				</form>
			</div>
			<div class="section">
				<div class="article">
				
				<p>Pour utiliser notre site, il vous faut un compte pour suivre vos progrés avec les leçons.<br>
				Si vous n'avez pas encore un compte, <a href="inscrire.jsp">inscrivez-vous maintenant</a>.</p>
				</div>
			</div>
		</div>
	</div>
	<div id="footer">
		<div>
			<div class="connect">
				<a href="http://stackoverflow.com/users/4824308/robert-f" id="twitter">twitter</a>
				<a href="http://stackoverflow.com/users/4824308/robert-f" id="facebook">facebook</a>
				<a href="https://github.com/bibou2151" id="googleplus">googleplus</a>
				<a href="https://github.com/bibou2151" id="pinterest">pinterest</a>
			</div>
			<p>
				&copy; copyright 2015 | all rights reserved.
			</p>
		</div>
	</div>
</body>
</html>