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
	<title>Escientia, un site pour etudier online</title>
</head>
<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");
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
			<li>				
				<a href="login.jsp">Connexion</a>
			</li>
			<%} %>
			<li>
				<a href="about.jsp">A propos</a>
			</li>
			</ul>
		</div>
		<%if(request.getSession().getAttribute("admin") != null){ %>
		<div class="section">
			<ul>
			<li>
				<a href="administrationUsers.jsp">Administration Utilisateurs</a>
			</li>
			<li>
				<a href="administrationCours.jsp">Administration Cours</a>
			</ul>
		</div>
			<% } %>
		<div class="article">
		<img src="images/404.png" alt="">
		<h1 style="font-size: 180px; color: #f00;">404</h1>
		<h1 style="font-size: 30px; font-weight: normal;">Oops, il semble que cette page s'est enfuie... <br><br>
		Je m'en fiche, j'ai encore beaucoup d'autre, plus jolies pages.<br>
		Vous pouvez naviguer à <a href="index.jsp">l'acceuil</a> et découvrer le site.
		</h1>
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