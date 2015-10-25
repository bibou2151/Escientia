<%@page import="business.Notes"%>
<%@page import="business.NotesEJB"%>
<%@page import="business.NotesCours"%>
<%@page import="java.util.Iterator"%>
<%@page import="business.UtilisateurEJB"%>
<%@page import="business.Utilisateur"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@page errorPage="compte.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link rel="SHORTCUT ICON" type="image/x-icon" href="images/icons/favicon.ico"/>
	<link rel="icon" type="image/x-icon" href="images/icons/favicon.ico" />
	<title>Profil - Escientia, un site pour etudier online</title>
	
	<script type="text/javascript">
	function show(cid) {
		var d = document.getElementById("c"+cid);
		if(d.style.display == 'none') {
			d.style.display = 'block';
		}else {
			d.style.display = 'none';
		}
		
		}
	</script>
	
</head>
<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");
%>
<%
boolean admin = (request.getSession().getAttribute("admin") != null) && 
						(request.getParameter("un") != null);

if(user == null){
	request.setAttribute("link", "editProfile.jsp");
	request.getRequestDispatcher("/login.jsp").forward(request, response);
}
Utilisateur u = user; 
if(admin) {
	UtilisateurEJB uej = new UtilisateurEJB(getServletContext().getRealPath("/resources/"));
	String un = request.getParameter("un");
	u = uej.getUtilisateur(un);
} else {
	u = user;
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
			<li <%if(!admin) {%>class="selected" <%} %>>
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
			<li <%if(admin) {%>class="selected" <%} %>>
				<a href="administrationUsers.jsp">Administration Utilisateurs</a>
			</li>
			<li>
				<a href="administrationCours.jsp">Administration Cours</a>
			</ul>
		</div>
		<% } %>
	</div>
	
	
	<div id="body">
		<div>
			<div class="featured">
				<h2>Modifier le Profil</h2>
				<h3><a href="<%=admin ? "administrationUsers.jsp" : "compte.jsp"%>">&lt;&lt;Retourner</a></h3>
				<p>
				<br><%if(!admin){ %>
				Modifier vos informations:</p><%} %>
				<h4 style=" color: red;">
				<%
					if(request.getAttribute("error") != null)
					out.print(request.getAttribute("error")); 
				%></h4>
				<form action="updateProfile" method="post">
				<table>
				<tr><td>
					<label for="username">Nom d'utilisateur: </label>
					<td>
					<label><%=u.getUsername() %></label>
					<input name="username" type="hidden" value="<%=u.getUsername()%>">
				<tr><td>
					<label for="prenom">Prenom<span>*</span>: </label>
					<td>
					<input name="prenom" type="text" required value="<%=u.getPrenom()%>">
				<tr><td>
					<label for="nom">Nom<span>*</span>: </label>
					<td>
					<input name="nom" type="text" required value="<%=u.getNom()%>">
				<tr><td>
					<label for="email">Email<span>*</span>: </label>
					<td>
					<input name="email" type="text" required value="<%=u.getEmail()%>">
				<tr><td>
					<label for="adresse">Adresse: </label>
					<td>
					<input name="adresse" type="text" value="<%=u.getAdresse()%>">
				<%if (!admin) {%>
				<tr><td>
					<label for="password">Mot de passe<span>*</span>: </label>
					<td>
					<input name="password" type="password" required>
				<%}%>
				<tr><td>
					<label style="font-size: 17px;" for="newpassword">Nouveau mot de passe: </label>
					<td>
					<input name="newpassword" type="password">
					</td>
				<tr><td><td><p>Laissez vide pour ne pas changer.</p>
				</table>
					<input class="button" type="submit" value="Modifier">
				
				</form>
			</div>
			<div class="section">
				<div class="article">
				
				<p></p>
				</div>
			</div>
		</div>
	</div>
	<%if(admin) {
		NotesEJB nej = new NotesEJB(getServletContext().getRealPath("/resources/"));
		Notes notes = nej.getNotes(u);
		%>
	<div id="header">
		<div class="article">
			<h1>Les Notes:</h1>
			<%
			Iterator<NotesCours> notesCours = notes.getNotes().iterator();
			
			while(notesCours.hasNext()) { 
				NotesCours nc = notesCours.next();
				double per = nc.getCompletedConcepts();
			%>
			<div class="article">
			<h1><a href="cours.jsp?cid=<%=nc.getCoursID()%>"><%=nc.getCours().getTitre()%></a></h1>
			<div class="progress"  title="Cliquez ici pour les r�sultats d�taill�es" onclick="show(<%=nc.getCoursID()%>)">
    			<div class="progress-bar" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" style="width:<%=per%>%">
				      <span class="sr-only"><%=per%>% Complet</span>
				
    		</div>
    		
    		</div>
    		<div class="list" id="c<%=nc.getCoursID() %>" style="display: none;">
					<table>
					<%
					Iterator<String> con = nc.getTitres().iterator();
					while (con.hasNext()) {
						String t = con.next();
						%>
						<tr><td><h5><a href="concept.jsp?cid=<%=nc.getCoursID()%>&con=<%=t%>"><%=t %></a></h5></td>
							<td><h5><%=nc.getNote(t) %>%</h5></td></tr>
						<%
					}
					%>
					</table>
				</div>
			</div>
			<%} %>
		</div>
	</div>
	<%} %>
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