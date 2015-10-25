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
	<title>Inscrivez vous - Escientia, un site pour etudier online</title>
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
				<h2>Inscription</h2>
				<p>
				<br>
					Ce site vous aide à apprendre nouvelles aptitudes et d'acquérir de nouvelles connaissances. 
				<br>
				Creer un nouveau compte:</p>
				<h4 style=" color: red;">
				<%
					if(request.getAttribute("error") != null)
					out.print(request.getAttribute("error")); 
				%></h4>
				<form action="inscrire" method="post">
				<table>
				<tr><td>
					<label for="username">Nom d'utilisateur<span>*</span>: </label>
					<td>
					<input name="username" type="text" required>
				<tr><td>
					<label for="password">Mot de passe<span>*</span>: </label>
					<td>
					<input name="password" type="password" required>
				<tr><td>
					<label for="prenom">Prenom<span>*</span>: </label>
					<td>
					<input name="prenom" type="text" required>
				<tr><td>
					<label for="nom">Nom<span>*</span>: </label>
					<td>
					<input name="nom" type="text" required>
				<tr><td>
					<label for="email">Email<span>*</span>: </label>
					<td>
					<input name="email" type="text" required>
				<tr><td>
					<label for="adresse">Adresse: </label>
					<td>
					<input name="adresse" type="text" >
					
				</table>
					<br>
					<p>Avez-vous déjà un compte? <a href="login.jsp">connectez vous</a></p>
					<input class="button" type="submit" value="Creér mon compte">
				
				</form>
			</div>
			<div class="section">
				<div class="article">
				
				<p>Pour utiliser notre site, il vous faut un compte pour suivre vos progrés avec les le�ons.<br>
				Si vous avez d�j� un compte, <a href="login.jsp">connectez-vous maintenant.</a></p>
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