<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="business.UtilisateurEJB"%>
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
	<title>Administration Utilisateurs - Escientia un site pour etudier online</title>
</head>
<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");

if(user == null) {
	request.setAttribute("link", "administrationUsers.jsp");
	request.getRequestDispatcher("login.jsp").forward(request, response);
} else {
	if(request.getSession().getAttribute("admin") == null) {
		response.sendRedirect("/index.jsp");
	}
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
			<li class="selected">
				<a href="administrationUsers.jsp">Administration Utilisateurs</a>
			</li>
			<li>
				<a href="administrationCours.jsp">Administration Cours</a>
			</ul>
		</div>
		<% } %>
	</div>
	
	<div id="body">
	<div class="featured">
		<div class="featured" style=" height : 300px;">
		<h2>Nouveau utilisateur</h2>
		<h4 style=" color: red;">
				<%
					if(request.getAttribute("error") != null)
					out.print(request.getAttribute("error")); 
				%></h4>
				<form action="AdmUser" method="post">
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
					
					<input name="action" type="hidden" value="ajouter">
				</table>
					<br>
					<input class="button" type="submit" value="Creér utilisateur">
				
				</form>
		</div>
		<div class="section">
				<div class="article">
				<h2>Bienvenue, Admin</h2>
				<p>Bienvenue à l'interface de l'administarion.<br>
				D'ici vou pouvez ajouter et supprimer les utilisateurs
				</p>
				</div>
			</div>
		</div>
		
		<%UtilisateurEJB ue = new UtilisateurEJB(getServletContext().getRealPath("/resources/"));
			Iterator<Utilisateur> list = ue.getUsers().iterator();
		%>
		
		<div id="header">
			<div class="article">
			<div class="content-table">
			<h1>Utilisateurs: </h1>
			<table>
			<tbody>
				<tr>
					<th>Nom d'utilisateur</th>
					<th>Nom</th>
					<th>Prénom</th>
					<th>Email</th>
					<th>Adresse</th>
					<th>Action</th>
				</tr>
				<%while(list.hasNext()){
					Utilisateur u = list.next();
					%>
				<tr>
					<td><a href="editProfile.jsp?un=<%= u.getUsername() %>" title="Cliquez pour modifier">
							<%= u.getUsername() %></a></td>
					<td><%= u.getNom()%></td>
					<td><%= u.getPrenom() %></td>
					<td><a href="mailto:<%= u.getEmail() %>"><%= u.getEmail() %></a></td>
					<td><%= u.getAdresse() %></td>
					
					<td style="height: 40px"><%if(!u.getUsername().equals("Admin") ) { %>
						<form action="AdmUser" method="post">
							<input name="username" type="hidden" value="<%= u.getUsername() %>">
							<input name="action" type="hidden" value="supprimer">
							<input class="button" type="submit" value="Supprimer">							
						</form>
						<%} %>
					</td>
				</tr>
				<%} %>
			</tbody>
			</table>
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