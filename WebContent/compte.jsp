<%@page import="business.NotesCours"%>
<%@page import="java.util.Iterator"%>
<%@page import="business.Notes"%>
<%@page import="business.NotesEJB"%>
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
	
	<title>Votre Compte - Escientia un site pour etudier online</title>
</head>
<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");
if(request.getSession().getAttribute("user") == null) {
	request.setAttribute("link", "compte.jsp");
	request.getRequestDispatcher("/login.jsp").forward(request, response);
}
%>
<%
NotesEJB nej = new NotesEJB(getServletContext().getRealPath("/resources/"));
Notes notes= nej.getNotes(user);

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
			<li class="selected">
				<a href="compte.jsp">Compte</a>
			</li>
			<li>
				<a href="logout">Déconnectez</a>
			</li>
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
		<img src="images/Books-and-Computer-Fotolia.jpg" alt="">
		<h1>Bienvenue, <%if(user != null) out.print(user.getPrenom());%> &agrave; Escientia.
		</h1>
		</div>
		
	</div>
	
	<div id="header">
		<div class="article">
			<h1>Votre cours:</h1>
			<%
			Iterator<NotesCours> notesCours = notes.getNotes().iterator();
			
			while(notesCours.hasNext()) { 
				NotesCours nc = notesCours.next();
				double per = nc.getCompletedConcepts();
			%>
			<div class="article">
			<h1><a href="cours.jsp?cid=<%=nc.getCoursID()%>"><%=nc.getCours().getTitre()%></a></h1>
			<%if (nc.getTarget() != null || nc.getTarget() == "") {%>
			<h4>Continuer à étudier <a href="start?cid=<%=nc.getCoursID()%>&con=<%=nc.getTarget()%>"><%=nc.getTarget() %></a>.</h4>
			<%} %>
			<div class="progress"  title="Cliquez ici pour les résultats détaillées" onclick="show(<%=nc.getCoursID()%>)">
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
	<div id="body">
		<div>
			<div class="featured">
				<h2>Votre Profil</h2>
				<h3><a href="editProfile.jsp">Modifier le profil</a></h3>
				<p></p>
				<br>
				<table>
				<tr><td>
					<label for="username">Nom d'utilisateur: </label>
					<td>
					<label><%=user.getUsername() %></label>
				<tr><td>
					<label for="prenom">Prenom: </label>
					<td>
					<label><%=user.getPrenom()%></label>
				<tr><td>
					<label for="nom">Nom: </label>
					<td>
					<label><%=user.getNom()%></label>
				<tr><td>
					<label for="email">Email: </label>
					<td>
					<label><%=user.getEmail()%></label>
				<tr><td>
					<label for="adresse">Adresse: </label>
					<td>
					<label><%=user.getAdresse()%></label>
				</table>
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